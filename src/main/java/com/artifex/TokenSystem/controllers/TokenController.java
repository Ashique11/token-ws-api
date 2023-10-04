package com.artifex.TokenSystem.controllers;

import com.artifex.TokenSystem.*;
import com.artifex.TokenSystem.dto.FileDetails;
import com.artifex.TokenSystem.dto.UpFile;
import com.artifex.TokenSystem.entity.AudioProperties;
import com.artifex.TokenSystem.entity.Token;
import com.artifex.TokenSystem.enums.TokenEnum;
import com.artifex.TokenSystem.repository.SystemPropRepo;
import com.artifex.TokenSystem.repository.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class TokenController {

    @Autowired
    TokenRepo tokenRepo;

    @Autowired
    SystemPropRepo systemPropRepo;

    @Autowired
    AudioPlayer audioPlayer;

    @Autowired
    AudioConfiguration audioConfiguration;

    @Autowired
    private HttpServletRequest request;

    @Value("${audio.play.script}")
    String audioPy;

    @Value("${audio.files.path}")
    String audioPath;

    @Value("${upload.dir}")
    private String uploadDir;

    private final int COMPLETED_TOKEN_STATUS = 1;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @GetMapping("/getAllTokens")
    public List<Token> getAllTokens(){
        return getAllTokensFromRepo();
    }

    @GetMapping("/getTokenById")
    public Token getTokenById(@RequestParam(value = "tokenId",defaultValue = "-1") String tokenId){
        int token_id = Integer.parseInt(tokenId);

        for(Token token : getAllTokensFromRepo()){

            if(token.getTokenId() == token_id)
                return token;

        }
        return new Token(-1,-1);
    }

    @PostMapping("/createToken")
    public Token createToken(@RequestParam(value = "tokenId",defaultValue = "-1") String tokenId){
        int token_id = Integer.parseInt(tokenId);
        Token token = new Token(-1,-1);
        if(token_id != -1) {
            token = new Token(token_id, TokenEnum.IN_PROGRESS.getId());
            tokenRepo.save(token);
        }
        return token;
    }

    @PostMapping("/completeOrder")
    public Token completeOrder(@RequestParam(value = "tokenId") String tokenId){
        int token_id = Integer.parseInt(tokenId);
        for(Token token : tokenRepo.findAll()){

            if(token.getTokenId() == token_id)
            {
                token.setTokenStatus(TokenEnum.COMPLETED.getId());
                tokenRepo.save(token);
                return token;
            }

        }
        return new Token(-1,-1);
    }

    @PostMapping("/playAudio")
    public ResponseEntity playAudio(@RequestParam(value = "tokenId",defaultValue = "") String tokenId){
        System.out.println("Playing audio for "+tokenId);
        for(int i =0; i < tokenId.length(); i++)
            audioPlayer.playAudio(String.valueOf(tokenId.charAt(i)));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAudio")
    public ResponseEntity<Resource> getAudio(@RequestParam(value = "tokenId",defaultValue = "") String tokenId) {
        // Load your audio file as a Resource (e.g., from the classpath)
        System.out.println("Retreiving: "+tokenId);
        Resource audioResource = new ClassPathResource(audioConfiguration.getAudioVoice(tokenId));

        // Return the audio file with appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + audioResource.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioResource);
    }

    @PostMapping("/uploadFile")
    public UpFile uploadFile(@RequestParam("file")MultipartFile file){
        String fileName = file.getOriginalFilename();
        Path destPath = Path.of(uploadDir, fileName);
        boolean status = Boolean.TRUE;
        try {
            Files.copy(file.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            status = Boolean.FALSE;
            logger.warning("File upload failed");
        }
        return new UpFile(fileName, file.getContentType(), status);
    }

    @PostMapping("/setAudioOptions")
    public AudioProperties setAudioOptions(@RequestParam(value = "gender", defaultValue = "female")String gender,
                                           @RequestParam(value = "language", defaultValue = "arabic") String lang){
        System.out.println("Gender: "+gender);
        System.out.println("Language: "+lang);
        AudioProperties audioProperties =
                new AudioProperties(gender, lang);
        systemPropRepo.save(audioProperties);
        audioConfiguration.setAudioVoice(gender);
        audioConfiguration.setAudioLanguage(lang);
        return new AudioProperties(gender, lang);
    }

    @PostMapping("/keysPressed")
    public Token keysPressed(@RequestParam(value = "tokenId", defaultValue = "-1")String tokenId){
        return completeOrder(tokenId);
    }

    @GetMapping("/getCompletedTokens")
    public List<Token> getCompletedTokens(){
        List<Token> tokenList = findCompletedTokensFromRepo();
        if(tokenList.isEmpty())
            return new ArrayList<>();
        List<Token> singleElementList = new ArrayList<Token>();
        singleElementList.add(tokenList.get(0));
        return singleElementList;

    }

    @PostMapping("/clearCompletedOrder")
    public Token clearCompletedOrder(@RequestParam(value = "tokenId", defaultValue = "-1") String tokenId){
        for(Token t : getAllTokensFromRepo() ) {
            if (t.getTokenId() == Integer.parseInt(tokenId) ) {
                System.out.println("Clearing "+tokenId);
                deleteToken(t);
            }
        }
        return new Token(0,0);
    }

    @GetMapping("/getAllFileNames")
    private List<FileDetails> listFilesWithExtension() {
        String type;
        Map<String, String> filesDet = new HashMap<>();
        List<FileDetails> fileNames = new ArrayList<>();
        File directory = new File(uploadDir);
        List<String> extensions = new ArrayList<>();
        extensions.add("mp4");
        extensions.add("jpg");
        extensions.add("png");
        extensions.add("jpeg");
        extensions.add("mov");
        if (directory.exists() && directory.isDirectory()) {
            for(String extension: extensions) {
                if(extension.equals("mov") || extension.equals("mp4"))
                    type = "video";
                else
                    type = "img";
                System.out.println("Searching for "+ extension+ " in "+uploadDir);
                File[] files = directory.listFiles((dir, name) -> name.endsWith("." + extension));

                if (files != null) {
                    for (File file : files) {
                        FileDetails fileDetails = new FileDetails(type, file.getPath());
                        fileNames.add(fileDetails);
                    }
                }
            }
        }

        return fileNames;
    }

    @GetMapping("/getImage")
    public  ResponseEntity<Resource> getImage(@RequestParam(value = "imgPath", defaultValue = "") String imgPath){
        File file = new File(imgPath);

        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] fileData = new byte[0];
        try {
            fileData = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + imgPath)
                .body(resource);

    }

    @DeleteMapping("/deleteFile")
    public String deleteFile(@RequestParam(value = "filename" , defaultValue = "") String fileName){
        boolean deleted = Boolean.FALSE;
        File filetoDelete = new File(uploadDir+"/"+fileName);
        if(filetoDelete.exists())
            deleted = filetoDelete.delete();
        return String.valueOf(deleted);
    }

    @MessageMapping("/completedOrder")
    @SendTo("/topic/OrderCompleted")
    public ShowPopupMessage greeting(CompletedOrder message) throws Exception {
        System.out.println("**************************************"+" CompletedORder"+message.getTokenId());
        return new ShowPopupMessage(HtmlUtils.htmlEscape(message.getTokenId()));
    }

    @MessageMapping("/addOrder")
    @SendTo("/topic/OrderAdded")
    public ShowPopupMessage addOrder(OrderAdd message) throws Exception {
        System.out.println("-----------------------------------"+" order added from order added"+message.getTokenId());
        return new ShowPopupMessage(HtmlUtils.htmlEscape(message.getTokenId()));
    }

    private synchronized List<Token> getAllTokensFromRepo(){
       return tokenRepo.findAll();
    }

    private synchronized void deleteToken(Token t){
        tokenRepo.delete(t);
    }

    private synchronized List<Token> findCompletedTokensFromRepo(){
        return tokenRepo.findByTokenStatus(COMPLETED_TOKEN_STATUS);
    }

}

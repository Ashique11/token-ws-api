package com.artifex.TokenSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class AudioPlayer {
    @Value("${audio.play.script}")
    String audioPy;
    @Autowired
    AudioConfiguration audioConfiguration;
    ProcessBuilder processBuilder;
    Process process;

    public synchronized void playAudio(String tokenId)
    {
        try {
            processBuilder = new ProcessBuilder("python", audioPy,audioConfiguration.getAudioVoice(tokenId));
            process = processBuilder.start();
            int exitCode = process.waitFor();
            process.destroy();
            System.out.println("Python script exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

package com.artifex.TokenSystem;

import com.artifex.TokenSystem.enums.Gender;
import com.artifex.TokenSystem.enums.Language;
import org.springframework.stereotype.Component;

@Component
public class AudioConfiguration {

    private String audioVoice = Gender.FEMALE.getGender();
    private String audioLanguage = Language.ARABIC.getLanguage();

    private final String audioPathArabicFemale = "/arabic/female/";
    private final String audioPathArabicMale = "/arabic/male/";
    private final String audioPathEnglishMale = "/english/male/";
    private final String audioPathEnglishFemale = "/english/female/";

    public AudioConfiguration(){

    }

    public void setAudioVoice(String gender){
        this.audioVoice = gender;
    }

    public void setAudioLanguage(String language){
        this.audioLanguage = language;
    }

    public String getAudioVoice(String tokenId){
        String audioVoicePath = "";
        System.out.println(audioVoice);
        System.out.println(audioLanguage);
        if(audioLanguage.equals(Language.ENGLISH.getLanguage())){
            if (audioVoice.equals(Gender.MALE.getGender()))
                audioVoicePath = audioPathEnglishMale;
            else
                audioVoicePath = audioPathEnglishFemale;
        }
        else {
            if (audioVoice.equals(Gender.MALE.getGender()))
                audioVoicePath = audioPathArabicMale;
            else
                audioVoicePath = audioPathArabicFemale;
        }

        return audioVoicePath+tokenId+".mp3";
    }

}

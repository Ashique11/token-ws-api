package com.artifex.TokenSystem.enums;

public enum Language {

    ENGLISH("english"),
    ARABIC("arabic");

    private String language;
    private Language(String lang){
        this.language = lang;
    }

    public String getLanguage(){
        return this.language;
    }
}

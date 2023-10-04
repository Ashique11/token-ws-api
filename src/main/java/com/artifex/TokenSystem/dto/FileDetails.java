package com.artifex.TokenSystem.dto;

public class FileDetails {

    private String type;
    private String url;
    public FileDetails(String type, String url){
        this.type = type;
        this.url = url;
    }
    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}

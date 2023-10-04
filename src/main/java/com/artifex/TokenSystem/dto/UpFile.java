package com.artifex.TokenSystem.dto;

public class UpFile {

    String fileName;
    String fileType;
    boolean status;

    public UpFile(String fileName, String fileType, boolean status){
        this.fileName = fileName;
        this.fileType = fileType;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public boolean isStatus() {
        return status;
    }

}

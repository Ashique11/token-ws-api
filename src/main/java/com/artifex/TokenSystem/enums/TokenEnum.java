package com.artifex.TokenSystem.enums;

public enum TokenEnum {
    IN_PROGRESS(0),
    COMPLETED(1);

    private int tokenStatus;
    private TokenEnum(int tokenStatus){
        this.tokenStatus = tokenStatus;
    }

    public int getId(){
        return this.tokenStatus;
    }
}

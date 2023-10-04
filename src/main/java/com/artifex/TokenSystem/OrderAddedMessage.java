package com.artifex.TokenSystem;

public class OrderAddedMessage {
    private String tokenId;

    public OrderAddedMessage(String tokenId){
        this.tokenId = tokenId;
    }

    public String getTokenId(){
        return tokenId;
    }
}

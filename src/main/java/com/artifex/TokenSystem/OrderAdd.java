package com.artifex.TokenSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderAdd {
    String tokenId;

    @JsonCreator
    public OrderAdd(@JsonProperty("tokenId") String tokenId){
        this.tokenId = tokenId;
    }

    public String getTokenId(){
        return tokenId;
    }
}

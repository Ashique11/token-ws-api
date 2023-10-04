package com.artifex.TokenSystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CompletedOrder {

    String tokenId;

    @JsonCreator
    public CompletedOrder(@JsonProperty("tokenId") String tokenId){
        this.tokenId = tokenId;
    }

    public String getTokenId(){
        return tokenId;
    }

}

package com.artifex.TokenSystem.entity;

import com.artifex.TokenSystem.enums.TokenEnum;
import jakarta.persistence.*;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    int tokenId = 0;
    int tokenStatus = 0;
    public Token(int tokenId, int tokenStatus){
        this.tokenId = tokenId;
        this.tokenStatus = tokenStatus;
    }

    public Token(){

    }

    public int getTokenId(){
        return tokenId;
    }

    public int getTokenStatus(){
        return tokenStatus;
    }

    public void setTokenStatus(int tokenStatus){
        if(tokenStatus == TokenEnum.IN_PROGRESS.getId()
                || tokenStatus == TokenEnum.COMPLETED.getId())
            this.tokenStatus = tokenStatus;
    }

}

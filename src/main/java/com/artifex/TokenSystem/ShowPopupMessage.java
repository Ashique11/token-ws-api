package com.artifex.TokenSystem;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ShowPopupMessage {

    private String popupId;
    public ShowPopupMessage(String popupId){
        this.popupId = popupId;
    }

    public String getPopupId(){
        return popupId;
    }

}

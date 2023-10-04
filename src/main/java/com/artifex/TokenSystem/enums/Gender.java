package com.artifex.TokenSystem.enums;

public enum Gender {

    MALE("male"),
    FEMALE("female");

    private String gender;
    private Gender(String gender){
        this.gender = gender;
    }

    public String getGender(){
        return this.gender;
    }
}

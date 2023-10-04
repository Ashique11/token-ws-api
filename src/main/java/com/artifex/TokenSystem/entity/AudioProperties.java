package com.artifex.TokenSystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AudioProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private final String gender;
    private final String language;

    public AudioProperties(String gender, String language){
        this.gender = gender;
        this.language = language;
    }

    public String getGender(){
        return this.gender;
    }

    public String getLanguage(){
        return this.language;
    }

}

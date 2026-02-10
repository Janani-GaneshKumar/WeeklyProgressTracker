package com.janani.contentrecommendation.exception;

public class ContentNotFoundException extends RuntimeException{
    public ContentNotFoundException(String message){
        super(message);
    }
}

package com.janani.contentrecommendation.exception;

import java.io.File;

public class FileStorageException extends RuntimeException{
    public FileStorageException(String message){
        super(message);
    }
    public  FileStorageException(String message,Throwable cause){
       super(message,cause);
    }
}

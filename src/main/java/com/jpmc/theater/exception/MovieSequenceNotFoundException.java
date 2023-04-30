package com.jpmc.theater.exception;

public class MovieSequenceNotFoundException extends RuntimeException{
    String exceptionMsg;
    public MovieSequenceNotFoundException(String s) {
        this.exceptionMsg = s;
    }
}

package com.thiagomf.interviewschedulerapi.exception;

public class InvalidInterviewStateException extends RuntimeException {

    public InvalidInterviewStateException(String message) {
        super(message);
    }
}
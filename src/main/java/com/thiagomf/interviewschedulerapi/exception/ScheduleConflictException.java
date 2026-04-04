package com.thiagomf.interviewschedulerapi.exception;

public class ScheduleConflictException extends RuntimeException {

    public ScheduleConflictException(String message) {
        super(message);
    }
}
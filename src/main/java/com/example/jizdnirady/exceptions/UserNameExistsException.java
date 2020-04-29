package com.example.jizdnirady.exceptions;
public class UserNameExistsException extends Throwable {
    public UserNameExistsException(String errorMessage) {
        super(errorMessage);
    }
}
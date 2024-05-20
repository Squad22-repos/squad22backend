package com.example.squad22backend.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String id) {
        super(String.format("User with id %d not found.", id));
    }

}
package com.example.squad22backend.exceptions;

public class CommunityNotFoundException extends RuntimeException {

    public CommunityNotFoundException(String id) {
        super(String.format("Community with id %d not found.", id));
    }

}
package com.lockcrypt.mongodbdatabase.service;

public class UserDataNotFoundException extends RuntimeException {

    public UserDataNotFoundException(String id) {
        super("No user data found with id " + id);
    }
}

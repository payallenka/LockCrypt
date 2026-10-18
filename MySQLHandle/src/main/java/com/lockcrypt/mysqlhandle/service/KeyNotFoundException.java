package com.lockcrypt.mysqlhandle.service;

public class KeyNotFoundException extends RuntimeException {

    public KeyNotFoundException(Integer id) {
        super("No key found with id " + id);
    }
}

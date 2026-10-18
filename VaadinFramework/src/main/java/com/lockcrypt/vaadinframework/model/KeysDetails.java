package com.lockcrypt.vaadinframework.model;

import lombok.Data;

// what MySQLHandle stores
@Data
public class KeysDetails {

    private Integer id;
    private String owner;
    private String passwordKey;
}

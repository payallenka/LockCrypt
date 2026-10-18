package com.lockcrypt.vaadinframework.model;

import lombok.Data;

// what MongoDBDatabase stores, password here is always the encrypted value
@Data
public class UserData {

    private String id;
    private String owner;
    private String accountFor;
    private String username;
    private String password;
    private Integer keyId;
}

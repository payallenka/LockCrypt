package com.lockcrypt.mongodbdatabase.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userData")
public class UserData {

    @Id
    private String id;

    @Indexed
    private String owner;

    private String accountFor;
    private String username;

    // always the encrypted value, the original password never reaches this service
    private String password;

    // id of the matching key stored by MySQLHandle
    private Integer keyId;
}

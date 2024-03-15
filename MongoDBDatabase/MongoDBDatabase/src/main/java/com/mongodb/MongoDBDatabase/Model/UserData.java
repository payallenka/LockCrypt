package com.mongodb.MongoDBDatabase.Model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user1")
public class UserData {
    public static final String SEQUENCE_NAME = "1" ;
    @Id
//    private Long id; // Change type to Long

    private String accountFor;
    private String username;
    private String password;
}

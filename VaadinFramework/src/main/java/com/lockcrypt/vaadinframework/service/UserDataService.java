package com.lockcrypt.vaadinframework.service;

import com.lockcrypt.vaadinframework.model.UserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Talks to the MongoDBDatabase service, which keeps the encrypted passwords.
 */
@Service
public class UserDataService {

    private final RestTemplate restTemplate;
    private final String mongoDbDatabaseUrl;

    public UserDataService(RestTemplate restTemplate,
                           @Value("${lockcrypt.services.mongodb-database}") String mongoDbDatabaseUrl) {
        this.restTemplate = restTemplate;
        this.mongoDbDatabaseUrl = mongoDbDatabaseUrl;
    }

    public List<UserData> retrieveData(String owner) {
        UserData[] userData = restTemplate.getForObject(
                mongoDbDatabaseUrl + "/userData?owner={owner}", UserData[].class, owner);
        return userData == null ? List.of() : List.of(userData);
    }

    public UserData sendData(UserData userData) {
        return restTemplate.postForObject(mongoDbDatabaseUrl + "/userData", userData, UserData.class);
    }

    public void deleteData(String id, String owner) {
        restTemplate.delete(mongoDbDatabaseUrl + "/userData/{id}?owner={owner}", id, owner);
    }
}

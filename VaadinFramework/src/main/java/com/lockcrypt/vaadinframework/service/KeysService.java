package com.lockcrypt.vaadinframework.service;

import com.lockcrypt.vaadinframework.model.KeysDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Talks to the MySQLHandle service, which keeps the encryption keys.
 */
@Service
public class KeysService {

    private final RestTemplate restTemplate;
    private final String mySqlHandleUrl;

    public KeysService(RestTemplate restTemplate,
                       @Value("${lockcrypt.services.mysql-handle}") String mySqlHandleUrl) {
        this.restTemplate = restTemplate;
        this.mySqlHandleUrl = mySqlHandleUrl;
    }

    public List<KeysDetails> retrieveKeys(String owner) {
        KeysDetails[] keys = restTemplate.getForObject(
                mySqlHandleUrl + "/keys?owner={owner}", KeysDetails[].class, owner);
        return keys == null ? List.of() : List.of(keys);
    }

    // the key goes in the request body, never in the url where it would end up in access logs
    public KeysDetails sendKey(String owner, String passwordKey) {
        KeysDetails key = new KeysDetails();
        key.setOwner(owner);
        key.setPasswordKey(passwordKey);
        return restTemplate.postForObject(mySqlHandleUrl + "/keys", key, KeysDetails.class);
    }

    public void deleteKey(Integer id, String owner) {
        restTemplate.delete(mySqlHandleUrl + "/keys/{id}?owner={owner}", id, owner);
    }
}

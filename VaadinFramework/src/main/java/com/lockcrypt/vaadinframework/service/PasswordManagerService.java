package com.lockcrypt.vaadinframework.service;

import com.lockcrypt.vaadinframework.model.KeysDetails;
import com.lockcrypt.vaadinframework.model.PasswordEntry;
import com.lockcrypt.vaadinframework.model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Ties the two storage services together: the key goes to MySQLHandle,
 * the masked password goes to MongoDBDatabase, linked by the key id.
 */
@Service
public class PasswordManagerService {

    private static final Logger log = LoggerFactory.getLogger(PasswordManagerService.class);

    private final UserDataService userDataService;
    private final KeysService keysService;
    private final MaskingService maskingService;

    public PasswordManagerService(UserDataService userDataService, KeysService keysService, MaskingService maskingService) {
        this.userDataService = userDataService;
        this.keysService = keysService;
        this.maskingService = maskingService;
    }

    public void savePassword(String owner, String accountFor, String username, String password) {
        String key = maskingService.generateKey();
        KeysDetails savedKey = keysService.sendKey(owner, key);

        UserData userData = new UserData();
        userData.setOwner(owner);
        userData.setAccountFor(accountFor);
        userData.setUsername(username);
        userData.setPassword(maskingService.encrypt(password, key));
        userData.setKeyId(savedKey.getId());

        try {
            userDataService.sendData(userData);
        } catch (RuntimeException e) {
            // a key without its password is useless, remove it so the two stores stay in sync
            removeKeyQuietly(savedKey.getId(), owner);
            throw e;
        }
    }

    public List<PasswordEntry> getPasswords(String owner) {
        Map<Integer, String> keysById = keysService.retrieveKeys(owner).stream()
                .collect(Collectors.toMap(KeysDetails::getId, KeysDetails::getPasswordKey));

        List<PasswordEntry> entries = new ArrayList<>();
        for (UserData userData : userDataService.retrieveData(owner)) {
            String key = keysById.get(userData.getKeyId());
            if (key == null) {
                log.warn("No key found for entry {} of user {}, skipping it", userData.getId(), owner);
                continue;
            }
            String originalPassword = maskingService.decryptPassword(key, userData.getPassword());
            entries.add(new PasswordEntry(userData.getId(), userData.getKeyId(),
                    userData.getAccountFor(), userData.getUsername(), originalPassword));
        }
        return entries;
    }

    public void deletePassword(String owner, PasswordEntry entry) {
        userDataService.deleteData(entry.id(), owner);
        removeKeyQuietly(entry.keyId(), owner);
    }

    private void removeKeyQuietly(Integer keyId, String owner) {
        try {
            keysService.deleteKey(keyId, owner);
        } catch (RuntimeException e) {
            log.warn("Could not remove key {} of user {}", keyId, owner, e);
        }
    }
}

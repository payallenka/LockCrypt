package com.lockcrypt.vaadinframework.service;

import com.lockcrypt.vaadinframework.model.KeysDetails;
import com.lockcrypt.vaadinframework.model.PasswordEntry;
import com.lockcrypt.vaadinframework.model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasswordManagerServiceTest {

    private final UserDataService userDataService = mock(UserDataService.class);
    private final KeysService keysService = mock(KeysService.class);
    private final MaskingService maskingService = new MaskingService();

    private PasswordManagerService passwordManagerService;

    @BeforeEach
    void setUp() {
        passwordManagerService = new PasswordManagerService(userDataService, keysService, maskingService);
    }

    @Test
    void savesKeyAndMaskedPasswordLinkedByKeyId() {
        when(keysService.sendKey(eq("payal"), anyString())).thenReturn(key(7, "unused"));

        passwordManagerService.savePassword("payal", "github", "payal-dev", "My$ecret");

        ArgumentCaptor<String> sentKey = ArgumentCaptor.forClass(String.class);
        verify(keysService).sendKey(eq("payal"), sentKey.capture());
        ArgumentCaptor<UserData> sentData = ArgumentCaptor.forClass(UserData.class);
        verify(userDataService).sendData(sentData.capture());

        UserData userData = sentData.getValue();
        assertThat(userData.getOwner()).isEqualTo("payal");
        assertThat(userData.getKeyId()).isEqualTo(7);
        assertThat(userData.getPassword()).isNotEqualTo("My$ecret");
        assertThat(maskingService.decryptPassword(sentKey.getValue(), userData.getPassword())).isEqualTo("My$ecret");
    }

    @Test
    void removesTheKeyAgainWhenSavingThePasswordFails() {
        when(keysService.sendKey(eq("payal"), anyString())).thenReturn(key(7, "unused"));
        when(userDataService.sendData(any())).thenThrow(new ResourceAccessException("MongoDBDatabase is down"));

        assertThatThrownBy(() -> passwordManagerService.savePassword("payal", "github", "payal-dev", "My$ecret"))
                .isInstanceOf(ResourceAccessException.class);

        verify(keysService).deleteKey(7, "payal");
    }

    @Test
    void decryptsEachEntryWithItsOwnKeyRegardlessOfOrder() {
        String githubKey = maskingService.generateKey();
        String gmailKey = maskingService.generateKey();

        // keys come back in a different order than the entries
        when(keysService.retrieveKeys("payal")).thenReturn(List.of(key(2, gmailKey), key(1, githubKey)));
        when(userDataService.retrieveData("payal")).thenReturn(List.of(
                userData("a", 1, "github", maskingService.encrypt("githubPass", githubKey)),
                userData("b", 2, "gmail", maskingService.encrypt("gmailPass", gmailKey))));

        List<PasswordEntry> entries = passwordManagerService.getPasswords("payal");

        assertThat(entries).extracting(PasswordEntry::accountFor, PasswordEntry::password)
                .containsExactly(
                        tuple("github", "githubPass"),
                        tuple("gmail", "gmailPass"));
    }

    @Test
    void skipsEntriesWhoseKeyIsMissing() {
        String githubKey = maskingService.generateKey();
        when(keysService.retrieveKeys("payal")).thenReturn(List.of(key(1, githubKey)));
        when(userDataService.retrieveData("payal")).thenReturn(List.of(
                userData("a", 1, "github", maskingService.encrypt("githubPass", githubKey)),
                userData("b", 99, "orphan", "c29tZXRoaW5n")));

        assertThat(passwordManagerService.getPasswords("payal"))
                .extracting(PasswordEntry::accountFor)
                .containsExactly("github");
    }

    @Test
    void deletesBothTheEntryAndItsKey() {
        passwordManagerService.deletePassword("payal", new PasswordEntry("a", 1, "github", "payal-dev", "pass"));

        verify(userDataService).deleteData("a", "payal");
        verify(keysService).deleteKey(1, "payal");
    }

    private static KeysDetails key(int id, String passwordKey) {
        KeysDetails key = new KeysDetails();
        key.setId(id);
        key.setOwner("payal");
        key.setPasswordKey(passwordKey);
        return key;
    }

    private static UserData userData(String id, int keyId, String accountFor, String maskedPassword) {
        UserData userData = new UserData();
        userData.setId(id);
        userData.setOwner("payal");
        userData.setAccountFor(accountFor);
        userData.setUsername("payal-dev");
        userData.setPassword(maskedPassword);
        userData.setKeyId(keyId);
        return userData;
    }
}

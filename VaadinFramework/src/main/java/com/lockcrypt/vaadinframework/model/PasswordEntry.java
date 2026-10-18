package com.lockcrypt.vaadinframework.model;

/**
 * One decrypted row shown on the "Your Passwords" page.
 */
public record PasswordEntry(String id, Integer keyId, String accountFor, String username, String password) {

    // keep the original password out of logs
    @Override
    public String toString() {
        return "PasswordEntry[id=" + id + ", accountFor=" + accountFor + ", username=" + username + "]";
    }
}

package com.lockcrypt.vaadinframework.service;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordGeneratorServiceTest {

    private final PasswordGeneratorService passwordGeneratorService = new PasswordGeneratorService();

    @RepeatedTest(50)
    void generatesTwentyCharactersWithEveryKindOfCharacter() {
        String password = passwordGeneratorService.generatePassword();

        assertThat(password).hasSize(20);
        assertThat(password).containsPattern("[a-z]");
        assertThat(password).containsPattern("[A-Z]");
        assertThat(password).containsPattern("[0-9]");
        assertThat(password).containsPattern("[^a-zA-Z0-9]");
        assertThat(password).doesNotContain(" ");
    }

    @Test
    void doesNotRepeatPasswords() {
        Set<String> passwords = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            passwords.add(passwordGeneratorService.generatePassword());
        }

        assertThat(passwords).hasSize(1000);
    }
}

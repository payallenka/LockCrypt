package com.lockcrypt.vaadinframework.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordGeneratorService {

    private static final int PASSWORD_LENGTH = 20;

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{};:,.?";

    private static final List<String> CHARACTER_GROUPS = List.of(LOWERCASE, UPPERCASE, NUMBERS, SPECIAL_CHARACTERS);
    private static final String ALL_CHARACTERS = String.join("", CHARACTER_GROUPS);

    // SecureRandom instead of Random, passwords must not be predictable
    private final SecureRandom random = new SecureRandom();

    public String generatePassword() {
        List<Character> password = new ArrayList<>(PASSWORD_LENGTH);

        // one from every group so each password has all four kinds of characters
        for (String group : CHARACTER_GROUPS) {
            password.add(randomCharFrom(group));
        }
        while (password.size() < PASSWORD_LENGTH) {
            password.add(randomCharFrom(ALL_CHARACTERS));
        }
        Collections.shuffle(password, random);

        StringBuilder result = new StringBuilder(PASSWORD_LENGTH);
        password.forEach(result::append);
        return result.toString();
    }

    private char randomCharFrom(String characters) {
        return characters.charAt(random.nextInt(characters.length()));
    }
}

package com.vaadin.VaadinFramework.Service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

@Service
public class PasswordGeneratorService {

    private static final int lengthOfPassword = 20;

    public String getContent() {
        return "Displaying content from a class";
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder();

        while (password.length() < lengthOfPassword) {
            char randomChar = generateRandomChar();
            password.append(randomChar);
        }

        return password.toString();
    }

    private char generateRandomChar() {
        Random random = new Random();
        int choice = random.nextInt(4);

        switch (choice) {
            case 0: // Lowercase letters
                return (char) (random.nextInt(26) + 'a');
            case 1: // Uppercase letters
                return (char) (random.nextInt(26) + 'A');
            case 2: // Numbers
                return (char) (random.nextInt(10) + '0');
            case 3: // Special characters
                Hashtable<Integer, Character> specialChar = new Hashtable<>();
                specialChar.put(1,'@');
                specialChar.put(2,'#');
                specialChar.put(3,'%');
                specialChar.put(4,'&');
                specialChar.put(5,'*');
                specialChar.put(6,'!');
                Integer randChar = random.nextInt(6)+1;


                return (specialChar.get(randChar));
            default:
                return ' '; // Return space in case of unexpected choice
        }
    }
}

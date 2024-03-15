package com.vaadin.VaadinFramework.Service;

import org.springframework.stereotype.Service;

@Service
public class MaskingService {

    private static final int PASSWORD_LENGTH = 20;

    public static String encrypt(String orgPassword, String key2){


        String maskedValue = "";
        //you won't have key1
        for(char c: orgPassword.toCharArray()){
            System.out.print((int)c+"\t");
        }
        for(char c: key2.toCharArray()){
            System.out.print((int)c+"\t");
        }

        for(int i=0; i<20;i++){
            int xorValue = (int)orgPassword.charAt(i)^(int)key2.charAt(i);
            maskedValue+= (char)(xorValue+97);

        }
        return maskedValue;
    }

    public static String decryptPassword(String key2, String maskedPassword){
        String originalPassword = "";

        for(int i=0; i<20;i++){
            int xorValue = (int)key2.charAt(i)^(int)(maskedPassword.charAt(i)-97);
            originalPassword += (char)xorValue;
        }

        return originalPassword;

    }

//        return originalPassword.toString().trim(); // Trim any leading or trailing placeholder characters

}

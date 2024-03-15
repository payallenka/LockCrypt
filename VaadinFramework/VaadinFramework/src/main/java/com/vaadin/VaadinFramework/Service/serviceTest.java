package com.vaadin.VaadinFramework.Service;


import java.util.Random;


public class serviceTest{

    private String matrixGenerated;
    private static String passwordGenerated = "";

    private static final int lenghtOfpassword = 16;

    private static String creator(){

        Random random = new Random();

        int counter = 0;

        int lowercase = random.nextInt(25)+98;
        int uppercase = random.nextInt(25)+65;
        int number = random.nextInt(9)+48;
        int specialCharacter;
        int specialCharacterChoice = random.nextInt(3)+1;
        switch(specialCharacterChoice){
            case 1:
                specialCharacter = random.nextInt(15)+32;
                break;
            case 2:
                specialCharacter = random.nextInt(6)+58;
                break;
            case 3:
                specialCharacter = random.nextInt(5)+91;
                break;
            default:
                specialCharacter = random.nextInt(3)+123;

        }


        passwordGenerated = (String.valueOf((char)lowercase) + String.valueOf((char)uppercase) + String.valueOf((char)number) + String.valueOf((char) specialCharacter));
        return passwordGenerated;
    }

    public static String generatedPassword(){
        System.out.println(lenghtOfpassword);
        String password = creator();

        while(password.length() <=16){
            String addition = creator();
            password += addition;
        }

        return password;
    }

    public static String encrypt(String key1, String key2){


        String maskedValue = "";
        //you won't have key1
        for(char c: key1.toCharArray()){
            System.out.print((int)c+"\t");
        }
        for(char c: key2.toCharArray()){
            System.out.print((int)c+"\t");
        }

        for(int i=0; i<20;i++){
            int xorValue = (int)key1.charAt(i)^(int)key2.charAt(i);
            maskedValue+= (char)(xorValue+97);

        }
        return maskedValue;
    }


    public static String decrypt(String key2, String maskedPassword){
        String originalPassword = "";

        for(int i=0; i<20;i++){
            int xorValue = (int)key2.charAt(i)^(int)(maskedPassword.charAt(i)-97);
            originalPassword += (char)xorValue;
        }

        return originalPassword;

    }
    public static void main(String[] args){

        String key1 = generatedPassword();
        System.out.println(key1);

        String key2 = generatedPassword();
        System.out.println(key2);

        String maskedPassword = encrypt(key1,key2);
        String originalPassword = decrypt(key2,maskedPassword);

        System.out.println("\noriginal Password "+key1);
        System.out.println("key 2 "+ key2);
        System.out.println("masked/encrypted password "+ maskedPassword);
        System.out.println("demasked/decrypted password " + originalPassword);
    }
}

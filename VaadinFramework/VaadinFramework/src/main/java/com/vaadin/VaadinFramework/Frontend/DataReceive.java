package com.vaadin.VaadinFramework.Frontend;

import com.vaadin.VaadinFramework.Model.UserData;
import com.vaadin.VaadinFramework.Service.MaskingService;
import com.vaadin.VaadinFramework.Service.PasswordGeneratorService;
import com.vaadin.VaadinFramework.Service.UserDetails;
import com.vaadin.VaadinFramework.Service.allKeys;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.logging.Logger;

@Route("user")
public class DataReceive extends VerticalLayout {

    private final PasswordGeneratorService passwordGeneratorService;
    private final UserDetails userDetails;
    private final MaskingService maskingService;
    private final allKeys allKeys;
    private static final Logger LOGGER = Logger.getLogger(DataReceive.class.getName());

    // Declare the generated password variable
    private String generatedPassword;

    public DataReceive(MaskingService masking, allKeys allKeys, UserDetails userDetails, PasswordGeneratorService passwordGeneratorService) {
        this.passwordGeneratorService = passwordGeneratorService;
        this.userDetails = userDetails;
        this.maskingService = masking;
        this.allKeys = allKeys;

        TextField accountFor = new TextField("AccountFor");
        TextField username = new TextField("Username");

        Button submitBtn = new Button("Submit");
        submitBtn.addClickListener(event -> {
            String accountForValue = accountFor.getValue();
            String usernameValue = username.getValue();

            final String key = passwordGeneratorService.generatePassword();

            String maskedPassword = maskingService.encrypt(generatedPassword, key);

            LOGGER.info("Generated password: " + generatedPassword);
            LOGGER.info("Key used: " + key);
            LOGGER.info("Encrypted password: " + maskedPassword);

            UserData userData = new UserData();
            userData.setPassword(maskedPassword);
            userData.setAccountFor(accountForValue);
            userData.setUsername(usernameValue);
            userDetails.DataSend(userData);
            allKeys.KeySend(key); // Send the key, not the key.toString()
        });

        Button btn = new Button("Generate");
        btn.addClickListener(event -> {
            String accountForValue = accountFor.getValue();
            String usernameValue = username.getValue();

            LOGGER.info("Account For: " + accountForValue + ", Username: " + usernameValue);
            Notification.show("Generating password for you!");

            // Generate password only if it's not generated yet
            if (generatedPassword == null) {
                generatedPassword = passwordGeneratorService.generatePassword();
            }

            add("Generated password is " + generatedPassword);
            add(submitBtn);
        });

        add(accountFor, username, btn);
    }
}

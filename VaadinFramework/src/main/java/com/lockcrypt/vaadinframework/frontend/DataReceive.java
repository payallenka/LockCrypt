package com.lockcrypt.vaadinframework.frontend;

import com.lockcrypt.vaadinframework.service.PasswordGeneratorService;
import com.lockcrypt.vaadinframework.service.PasswordManagerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("user")
@PageTitle("Create a Password | LockCrypt")
@PermitAll
public class DataReceive extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DataReceive.class);

    private final PasswordManagerService passwordManagerService;
    private final String owner;

    private final TextField accountFor = new TextField("Account for");
    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");

    public DataReceive(PasswordManagerService passwordManagerService,
                       PasswordGeneratorService passwordGeneratorService,
                       AuthenticationContext authenticationContext) {
        this.passwordManagerService = passwordManagerService;
        this.owner = authenticationContext.getPrincipalName().orElseThrow();

        accountFor.setRequiredIndicatorVisible(true);
        accountFor.setPlaceholder("e.g. GitHub");
        username.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);
        password.setHelperText("Type your own or let LockCrypt generate one");

        Button generateBtn = new Button("Generate", event -> {
            password.setValue(passwordGeneratorService.generatePassword());
            Notification.show("Generated a strong password for you!");
        });
        HorizontalLayout passwordRow = new HorizontalLayout(password, generateBtn);
        passwordRow.setAlignItems(FlexComponent.Alignment.BASELINE);

        Button submitBtn = new Button("Submit", event -> submit());
        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new H2("Create a Password"), accountFor, username, passwordRow,
                new HorizontalLayout(submitBtn, new RouterLink("Back", MainView.class)));
    }

    private void submit() {
        if (accountFor.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all the fields");
            return;
        }

        try {
            passwordManagerService.savePassword(owner, accountFor.getValue().trim(),
                    username.getValue().trim(), password.getValue());
        } catch (RuntimeException e) {
            log.error("Could not save password for user {}", owner, e);
            showError("Could not save the password, please try again later");
            return;
        }

        Notification.show("Password for " + accountFor.getValue() + " saved")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        resetForm();
    }

    private void resetForm() {
        for (TextField field : new TextField[]{accountFor, username}) {
            field.clear();
            field.setInvalid(false);
        }
        password.clear();
        password.setInvalid(false);
    }

    private void showError(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

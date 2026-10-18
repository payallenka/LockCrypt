package com.lockcrypt.vaadinframework.frontend;

import com.lockcrypt.vaadinframework.service.AppUserService;
import com.lockcrypt.vaadinframework.service.RegistrationException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Register | LockCrypt")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final AppUserService appUserService;

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm password");

    public RegisterView(AppUserService appUserService) {
        this.appUserService = appUserService;

        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Button registerBtn = new Button("Create account", event -> register());
        registerBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(new H2("Create your LockCrypt account"), username, password, confirmPassword, registerBtn,
                new RouterLink("Already have an account? Log in", LoginView.class));
    }

    private void register() {
        if (!password.getValue().equals(confirmPassword.getValue())) {
            showError("Passwords do not match");
            return;
        }

        try {
            appUserService.register(username.getValue().trim(), password.getValue());
        } catch (RegistrationException e) {
            showError(e.getMessage());
            return;
        }

        Notification.show("Account created, you can log in now").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    }

    private void showError(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

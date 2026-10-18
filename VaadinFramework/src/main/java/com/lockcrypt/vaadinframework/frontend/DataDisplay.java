package com.lockcrypt.vaadinframework.frontend;

import com.lockcrypt.vaadinframework.model.PasswordEntry;
import com.lockcrypt.vaadinframework.service.PasswordManagerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("view")
@PageTitle("Your Passwords | LockCrypt")
@PermitAll
public class DataDisplay extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(DataDisplay.class);

    private final PasswordManagerService passwordManagerService;
    private final String owner;
    private final Grid<PasswordEntry> passwordGrid = new Grid<>();

    public DataDisplay(PasswordManagerService passwordManagerService, AuthenticationContext authenticationContext) {
        this.passwordManagerService = passwordManagerService;
        this.owner = authenticationContext.getPrincipalName().orElseThrow();

        initializeGrid();
        setSizeFull();
        add(new H2("Your Passwords"), passwordGrid, new RouterLink("Back", MainView.class));
        refreshGrid();
    }

    private void initializeGrid() {
        passwordGrid.addColumn(PasswordEntry::accountFor).setHeader("Account for").setSortable(true);
        passwordGrid.addColumn(PasswordEntry::username).setHeader("Username");
        // hidden by default, the eye icon reveals it
        passwordGrid.addColumn(new ComponentRenderer<>(entry -> {
            PasswordField passwordField = new PasswordField();
            passwordField.setValue(entry.password());
            passwordField.setReadOnly(true);
            passwordField.setWidthFull();
            return passwordField;
        })).setHeader("Password").setFlexGrow(2);
        passwordGrid.addComponentColumn(entry -> {
            Button deleteBtn = new Button("Delete", event -> delete(entry));
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return deleteBtn;
        });
    }

    private void refreshGrid() {
        try {
            passwordGrid.setItems(passwordManagerService.getPasswords(owner));
        } catch (RuntimeException e) {
            log.error("Could not load passwords for user {}", owner, e);
            showError("Could not load your passwords, please try again later");
        }
    }

    private void delete(PasswordEntry entry) {
        try {
            passwordManagerService.deletePassword(owner, entry);
            Notification.show("Deleted password for " + entry.accountFor());
        } catch (RuntimeException e) {
            log.error("Could not delete {} for user {}", entry, owner, e);
            showError("Could not delete the password, please try again later");
        }
        refreshGrid();
    }

    private void showError(String message) {
        Notification.show(message).addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

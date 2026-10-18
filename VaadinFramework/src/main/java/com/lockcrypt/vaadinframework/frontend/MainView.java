package com.lockcrypt.vaadinframework.frontend;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@Route("")
@PageTitle("LockCrypt")
@PermitAll
public class MainView extends VerticalLayout {

    public MainView(AuthenticationContext authenticationContext) {
        // Header
        Image logo = new Image("images/logo.png", "LockCrypt logo");
        logo.setWidth("100px");
        Div header = new Div(logo, new H1("Welcome to LockCrypt: Password Security Solution"));
        header.getStyle().set("text-align", "center");

        Span loggedInAs = new Span("Logged in as " + authenticationContext.getPrincipalName().orElse(""));

        // Navigation bar
        HorizontalLayout navBar = new HorizontalLayout();
        navBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navBar.setWidthFull();
        navBar.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "10px")
                .set("padding", "10px 20px")
                .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)")
                .set("justify-content", "center");

        Button logoutButton = new Button("Log out", event -> authenticationContext.logout());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        navBar.add(navLink("Your Passwords", DataDisplay.class),
                navLink("Create a Password", DataReceive.class),
                logoutButton);

        // Footer
        Div footer = new Div(new Span("Thank you"));
        footer.getStyle().set("text-align", "center")
                .set("margin-top", "20px")
                .set("color", "#666")
                .set("font-size", "20px");

        // Main layout
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
        getStyle().set("background-color", "#f0f0f0");

        add(header, loggedInAs, navBar, footer);
    }

    private RouterLink navLink(String text, Class<? extends Component> view) {
        RouterLink link = new RouterLink(text, view);
        link.getStyle()
                .set("color", "#333")
                .set("text-decoration", "none")
                .set("margin-right", "20px")
                .set("font-size", "18px");
        return link;
    }
}

package com.vaadin.VaadinFramework.Frontend;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;

@Route("")
//@PWA(name = "LockCrypt", shortName = "LockCrypt")
//@CssImport("./frontend/styles/shared-styles.css")
public class MainView extends VerticalLayout {

    public MainView() {
        // Header
        Div header = new Div(new H1("Welcome to LockCrypt: Password Security Solution"));
        header.getStyle().set("text-align", "center");

        // Navigation bar
        RouterLink yourPasswordsLink = new RouterLink("Your Passwords", DataDisplay.class);
        yourPasswordsLink.getStyle().set("margin-right", "20px");
        RouterLink createPasswordLink = new RouterLink("Create a Password", DataReceive.class);

        HorizontalLayout navBar = new HorizontalLayout(yourPasswordsLink, createPasswordLink);
        navBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navBar.setWidthFull();
        navBar.getStyle().set("background-color", "#f0f0f0");
        navBar.setPadding(true);
// Footer
        Div footer = new Div(new H1("Thank you "));
        footer.getStyle().set("text-align", "center");
        footer.getStyle().set("margin-top", "20px");

// Main layout
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
        setSizeFull();

// Add components to the layout
        add(header, navBar, footer); // Add footer instead of header twice

    }
}

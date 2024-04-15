package com.vaadin.VaadinFramework.Frontend;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

//changed
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        // Header
        Div header = new Div(new H1("Welcome to LockCrypt: Password Security Solution"));
        header.getStyle().set("text-align", "center");

        // Navigation bar
        HorizontalLayout navBar = new HorizontalLayout();
        navBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navBar.setWidthFull();
        navBar.getStyle()
                .set("background-color", "#f0f0f0")
                .set("border-radius", "10px")
                .set("padding", "10px 20px")
                .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)")
                .set("justify-content", "center"); // Center links

        RouterLink yourPasswordsLink = new RouterLink("Your Passwords", DataDisplay.class);
        yourPasswordsLink.getStyle()
                .set("color", "#333")
                .set("text-decoration", "none")
                .set("margin-right", "20px")
                .set("font-size", "18px");

        RouterLink createPasswordLink = new RouterLink("Create a Password", DataReceive.class);
        createPasswordLink.getStyle()
                .set("color", "#333")
                .set("text-decoration", "none")
                .set("margin-right", "20px")
                .set("font-size", "18px");

        // Footer
        Div footer = new Div(new H1("Thank you"));
        footer.getStyle().set("text-align", "center")
                .set("margin-top", "20px")
                .set("color", "#fff")
                .set("font-size", "20px");

        // Main layout
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        setPadding(true);
        setSpacing(true);
        setSizeFull();
        getStyle().set("background-color", "#f0f0f0")
                .set("background-image", "url('logo.png')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("min-height", "100vh")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "center")
                .set("align-items", "center");

        // Add components to the navigation bar
        navBar.add(yourPasswordsLink, createPasswordLink);

        // Add components to the layout
        add(header, navBar, footer);
    }
}

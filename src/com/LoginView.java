package com;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.swing.*;

/**
 * Created by Maciej on 2014-10-18.
 */
public class LoginView extends CustomComponent implements View, Button.ClickListener {

    public static final String NAME = "login";
    private final TextField user;
    private final Button loginButton;

    public LoginView() {
        setSizeFull();

        // Create thr user input field
        user = new TextField("Name:");
        user.setRequired(true);
        user.setInputPrompt("Your unique username");

        // Create login button
        loginButton = new Button("Login", this);

        // Add to a panel
        VerticalLayout fields = new VerticalLayout(user, loginButton);
        fields.setCaption("Please enter username to access the application.");
        fields.setSpacing(true);;
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        // The view root layout
        VerticalLayout viewLayout = new VerticalLayout(fields);
        viewLayout.setSizeFull();
        viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(viewLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // focus the username field when user arrives to the login view
        user.focus();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        String username = user.getValue();
        getSession().setAttribute("user", username);
        getUI().getNavigator().navigateTo(LoginMainView.NAME);
    }
}

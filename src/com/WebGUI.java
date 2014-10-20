package com;

import com.vaadin.data.Validator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import static com.vaadin.ui.Button.*;

/**
 * Created by Maciej on 2014-10-18.
 */
public class WebGUI extends UI {

    @Override
    public void init(VaadinRequest request) {
        // Create a new instance of the navigator. The navigator will attach itself automatically to this view
        new Navigator(this, this);

        // The initial log view where the user can login to the application
        getNavigator().addView(LoginView.NAME, LoginView.class);

        getNavigator().addView(CreateGameView.NAME, CreateGameView.class);
        // Add the main view of the application
        getNavigator().addView(LoginMainView.NAME, LoginMainView.class);
        getNavigator().addView(LobbyView.NAME, LobbyView.class);
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                //Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = viewChangeEvent.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in, then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {

            }
        });

//        VerticalLayout layout = new VerticalLayout();
//        setContent(layout);
//        layout.addComponent(new Label("Hello, world!"));
    }
}

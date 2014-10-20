package com;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.lang.reflect.Array;

//import java.awt.*;

/**
 * Created by Maciej on 2014-10-20.
 */
public class CreateGameView extends CustomComponent implements View {

    public static final String NAME = "createGame";
    private MenuBar menu = new MenuBar();
    private MenuBar.MenuItem currentUser;
    private MenuBar.MenuItem logout;

    public CreateGameView() {
        setSizeFull();
        Panel panel = new Panel();
        GridLayout panelLayout = new GridLayout(1,4);
        panelLayout.setWidth("100%");

        Component menu = buildHeader();
        Component gameForm = buildGameForm();

        panelLayout.addComponent(menu,0,0);
        panelLayout.addComponent(gameForm,0,1);
//        panelLayout.setComponentAlignment(gameForm, Alignment.MIDDLE_CENTER);
        panel.setContent(panelLayout);
        setCompositionRoot(panel);
    }

    private Component buildHeader() {
        menu.setWidth("100%");

//        MenuBar.MenuItem createGame = menu.addItem("Create Game", null, null);
        MenuBar.MenuItem cancel = menu.addItem("List of games", null, null);
        cancel.setCommand(new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getNavigator().navigateTo(LoginMainView.NAME);
            }
        });
        return menu;
    }

    private Component buildGameForm() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);

        final ComboBox gameType = new ComboBox("Game type");
        gameType.addItems("N*", "N+", "Poker");

        TextField playersNumber = new TextField("Number of players");
        TextField botsNumber = new TextField("Number of bots");
        final ComboBox level = new ComboBox("Game level");
        level.addItems("Easy", "Hard");

//        gameType.addValueChangeListener();

        final Button createGame = new Button("Create game");
        final Button cancel = new Button("Cancel");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getNavigator().navigateTo(LoginMainView.NAME);
            }
        });

        fields.addComponents(gameType, playersNumber, botsNumber, level, createGame, cancel);
        return fields;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), null, null);
        logout = currentUser.addItem("Logout", null, null);
        logout.setCommand(new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                // "Logout" the user
                getSession().setAttribute("user", null);
                // Refresh this view, should redirect to login view
                getUI().getNavigator().navigateTo(NAME);
            }
        });
    }
}

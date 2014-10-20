package com;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by lukasz on 20.10.14.
 */
public class LobbyView extends CustomComponent implements View {

    public static final String NAME = "lobby";
    Table users = new Table("Users in lobby");
    Panel panel = new Panel();
    Label info = new Label();
    Button leave = new Button ("Leave lobby");
    GridLayout panelLayout = new GridLayout(1,3);
    public LobbyView() {
        setSizeFull();
        users.addContainerProperty("User", String.class, null);
        users.setPageLength(users.size());
        users.addItem(new Object[]{"X"}, 1);
        users.addItem(new Object[]{"Y"} , 2);
        users.addItem(new Object[]{"Z"}, 3);
        panelLayout.setWidth("100%");
        panelLayout.addComponent(info, 0, 0);
        panelLayout.addComponent(users,0,1);
        panelLayout.addComponent(leave,0,2);
        panel.setContent(panelLayout);
        leave.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().getNavigator().navigateTo(LoginMainView.NAME);
            }
        });
        setCompositionRoot(panel);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
       info.setValue(String.valueOf(getSession().getAttribute("gt")) + " @ " +
               String.valueOf(getSession().getAttribute("ad")) + " - Lobby"); // get server info
        users.addItem(new Object[]{String.valueOf(getSession().getAttribute("user"))},users.size()+1);
    }
}


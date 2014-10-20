package com;


import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.*;


/**
 * Created by Maciej on 2014-10-18.
 */
public class LoginMainView extends CustomComponent implements View {

    public static final String NAME = "";
    Panel panel = new Panel();
    GridLayout panelLayout = new GridLayout(1,4);
    MenuBar menu = new MenuBar();
    MenuItem currentUser;
    MenuItem logout;
    MenuItem refresh;
    MenuItem createGame;
    Button join = new Button("Join Now!");
    Label selected = new Label("Selected: ");
    Table servers = new Table("List of servers");
    public LoginMainView() {
       setSizeFull();
       servers.setSelectable(true);
       servers.setImmediate(true);
       servers.addContainerProperty("Address", String.class, null);
       servers.addContainerProperty("Game type", String.class, null);
       servers.addContainerProperty("Players", Integer.class, null);
       servers.setPageLength(servers.size());
       servers.addValueChangeListener(new Property.ValueChangeListener() {
           @Override
           public void valueChange(Property.ValueChangeEvent event) {
               Object rowID = servers.getValue();
               String gameType = (String)servers.getContainerProperty(rowID,"Game type").getValue();
               String address = (String)servers.getContainerProperty(rowID,"Address").getValue();
               selected.setValue("Selected: " + gameType + " @ " + address);
           }
       });
        // Add rows the hard way
        servers.addItem(new Object[]{"193.193.80.0",        "N+", 10}, 1);
        servers.addItem(new Object[]{"193.193.80.1",       "N*", 5}, 2);
        servers.addItem(new Object[]{"193.193.80.2", "Poker", 20}, 3);

       menu.setWidth("100%");
       createGame = menu.addItem("Create Game", null, null);
       refresh = menu.addItem("Refresh", null, null);
       panelLayout.setWidth("100%");
       panelLayout.addComponent(join, 0, 3);
       panelLayout.addComponent(selected,0,2);
       panelLayout.addComponent(servers,0,1);
       panelLayout.addComponent(menu,0,0);
       panel.setContent(panelLayout);
       setCompositionRoot(panel);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), null, null);
        logout = currentUser.addItem("Logout", null, null);
        logout.setCommand(new Command() {
            @Override
            public void menuSelected(MenuItem selectedItem) {
                // "Logout" the user
                getSession().setAttribute("user", null);
                // Refresh this view, should redirect to login view
                getUI().getNavigator().navigateTo(NAME);
            }
        });
    }
}

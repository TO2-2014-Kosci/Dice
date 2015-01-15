package pl.edu.agh.to2.webgui.view;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import org.apache.xpath.operations.Bool;
import pl.edu.agh.to2.webgui.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maciej on 2014-12-01.
 */
public class MainView extends CustomComponent
        implements IMainView, View, Button.ClickListener, MenuBar.Command, Property.ValueChangeListener {
    public static final String NAME = "";
    public static final String LOGOUT_TEXT = "Logout";
    public static final String CREATE_TEXT = "Create game";
    public static final String REFRESH_TEXT = "Refresh";

    public Button join = new Button("Join now!", this);
    private MenuBar menu = new MenuBar();
    private Table servers = new Table("List of servers");
    private MenuBar.MenuItem currentUser;

    public MainView() {
//        MainPresenter mainPresenter = new MainPresenter(this);
        GridLayout panelLayout = new GridLayout(1, 4);
        Label selected = new Label("Selected: ");

        setSizeFull();
        join.setEnabled(false);

        panelLayout.setWidth("100%");
        panelLayout.addComponent(join, 0, 3);
        panelLayout.addComponent(selected, 0, 2);
        panelLayout.addComponent(buildGamesList(), 0, 1);
        panelLayout.addComponent(buildMenu(), 0, 0);
        setCompositionRoot(panelLayout);
    }

    private Component buildGamesList() {
        servers.addValueChangeListener(this);

        servers.setSelectable(true);
        servers.setImmediate(true);
        servers.addContainerProperty("Game name", String.class, null);
        servers.addContainerProperty("Players", String.class, null);
        servers.addContainerProperty("Game type", String.class, null);
        servers.addContainerProperty("Is started", Boolean.class, null);
        servers.setPageLength(servers.size());

        return servers;
    }

    private Component buildMenu() {
        menu.setWidth("100%");
        MenuBar.MenuItem createGame = menu.addItem(CREATE_TEXT, FontAwesome.PLUS_SQUARE, this);
        MenuBar.MenuItem refresh = menu.addItem(REFRESH_TEXT, FontAwesome.REFRESH, this);


        return menu;
    }

    @Override
    public void showNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
    }

    public void refreshGamesList(List<Object[]> games) {
        servers.removeAllItems();
//        servers.addItem(new Object[] {"mockup", "5/10", "NT"}, null);
        for (Object[] o : games) {
            servers.addItem(o, null);
        }
    }

    List<MainViewListener> listeners = new ArrayList<MainViewListener>();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (MainViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        for (MainViewListener listener : listeners) {
            listener.menuSelected(menuItem.getText());
        }
    }


    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Object rowID = valueChangeEvent.getProperty().getValue();
        if(rowID != null) {
            String gameName = (String) servers.getContainerProperty(rowID, "Game name").getValue();
            Boolean isStarted = (Boolean) servers.getContainerProperty(rowID, "Is started").getValue();
            for (MainViewListener listener : listeners) {
                listener.valueChange(gameName, isStarted);
            }
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (currentUser == null) {
            currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), FontAwesome.USER, null);
            MenuBar.MenuItem logout = currentUser.addItem(LOGOUT_TEXT, FontAwesome.SIGN_OUT, this);
        }
        else {
            currentUser.setText((String)getSession().getAttribute("user"));
        }
        for (MainViewListener listener : listeners) {
            listener.menuSelected(REFRESH_TEXT);
        }
    }

    @Override
    public void addListener(MainViewListener listener) {
        listeners.add(listener);
    }

}

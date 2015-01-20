package pl.edu.agh.to2.webgui.view;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import javafx.geometry.Pos;
import org.apache.xpath.operations.Bool;
import pl.edu.agh.to2.webgui.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Maciej on 2014-12-01.
 */
public class MainView extends VerticalLayout
        implements IMainView, View, Button.ClickListener, MenuBar.Command, Property.ValueChangeListener {
    public static final String NAME = "";
    public static final String LOGOUT_TEXT = "Logout";
    public static final String CREATE_TEXT = "Create game";
    public static final String REFRESH_TEXT = "Refresh";

    public Button join = new Button("Join now!", this);
    private MenuBar menu = new MenuBar();
    private Table servers = new Table();
    private MenuBar.MenuItem currentUser;

    public MainView() {
        setSizeFull();
        setStyleName("main-background");

        buildMenu();

        Component gamesList = buildGamesList();
        addComponent(gamesList);
        setComponentAlignment(gamesList, Alignment.TOP_CENTER);
    }

    private Component buildGamesList() {
        VerticalLayout content = new VerticalLayout();

        content.addComponent(menu);

        Label serversLabel = new Label("List of games");
        serversLabel.addStyleName("huge bold");
        serversLabel.setSizeUndefined();
        content.addComponent(serversLabel);
        content.setComponentAlignment(serversLabel, Alignment.TOP_CENTER);

        servers.addValueChangeListener(this);
        servers.setSelectable(true);
        servers.setSortEnabled(true);
        servers.setImmediate(true);
        servers.addContainerProperty("Game name", String.class, null);
        servers.addContainerProperty("Players", String.class, null);
        servers.addContainerProperty("Game type", String.class, null);
        servers.addContainerProperty("Is started", Boolean.class, null);
        servers.addContainerProperty("Rounds to win", Integer.class, null);
        servers.setPageLength(servers.size());
        content.addComponent(servers);
        content.setComponentAlignment(servers, Alignment.MIDDLE_CENTER);

        join.setEnabled(false);
        join.addStyleName(ValoTheme.BUTTON_PRIMARY);
        content.addComponent(join);
        content.setComponentAlignment(join, Alignment.BOTTOM_CENTER);

        content.setSpacing(true);
        return content;
    }

    private Component buildMenu() {
        menu.setWidth("100%");
        menu.setStyleName("normal");
        menu.addItem(CREATE_TEXT, FontAwesome.PLUS_SQUARE, this);
        menu.addItem(REFRESH_TEXT, FontAwesome.REFRESH, this);

        return menu;
    }

    @Override
    public void showNotification(String message, String style) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setStyleName(style);
        notification.setDelayMsec(1000);
        notification.show(Page.getCurrent());
    }

    public void refreshGamesList(List<Object[]> games) {
        servers.removeAllItems();
        for (Object[] o : games) {
            servers.addItem(o, null);
        }
        servers.setPageLength(Math.min(15, servers.size()));
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
        join.setEnabled(false);
        for (MainViewListener listener : listeners) {
            listener.menuSelected(menuItem.getText());
        }
    }


    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        Object rowID = valueChangeEvent.getProperty().getValue();
        if(rowID != null) {
            String gameName = (String) servers.getContainerProperty(rowID, "Game name").getValue();
            for (MainViewListener listener : listeners) {
                listener.valueChange(gameName);
            }
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        join.setEnabled(false);
        if (currentUser == null) {
            currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), FontAwesome.USER, null);
            currentUser.addItem(LOGOUT_TEXT, FontAwesome.SIGN_OUT, this);
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

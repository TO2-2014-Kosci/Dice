package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-11-28.
 * Edited by Lukasz on 2014-12-01
 */
public class LobbyView extends VerticalLayout
    implements ILobbyView, View,Button.ClickListener {
    public static final String NAME = "lobby";
    public static final String LEAVE_TEXT = "Leave lobby";
    public static final String SIT_DOWN_TEXT = "Sit down";
    public static final String STAND_UP_TEXT = "Stand up";

    List<LobbyViewListener> listeners = new ArrayList<LobbyViewListener>();
    Table users = new Table();
    Panel panel = new Panel();
    Label info = new Label();
    Button leave = new Button(LEAVE_TEXT,this);
    Button sitdown = new Button(SIT_DOWN_TEXT, this);
    GridLayout panelLayout = new GridLayout(1,5);

    public LobbyView() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);

        Component usersList = buildUsersList();
        addComponent(usersList);
        setComponentAlignment(usersList, Alignment.TOP_CENTER);

    }

    private Component buildUsersList() {
        GridLayout layout = new GridLayout(2, 1);
        layout.setSpacing(true);

        //users list
        VerticalLayout usersLayout = new VerticalLayout();
        usersLayout.setMargin(true);
        usersLayout.setSpacing(true);
        usersLayout.setSizeUndefined();
        users.addContainerProperty("User", String.class, null);
        usersLayout.addComponent(users);

        //buttons
        VerticalLayout buttons = new VerticalLayout();
        buttons.setMargin(true);
        buttons.setSpacing(true);
        buttons.setSizeUndefined();
//        buttons.setStyleName("well");
        Label header = new Label("Users in lobby");
        header.setStyleName("huge bold");
        buttons.addComponent(header);
        sitdown.setStyleName(ValoTheme.BUTTON_PRIMARY);
        buttons.addComponent(sitdown);
        buttons.setComponentAlignment(sitdown, Alignment.TOP_CENTER);
        buttons.addComponent(leave);
        buttons.setComponentAlignment(leave, Alignment.TOP_CENTER);

        layout.addComponent(buttons, 0, 0);
        layout.addComponent(usersLayout, 1, 0);
        return layout;
    }

    @Override
    public void showNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
    }

    public void setPlayersList(List<String> playersList) {
        users.removeAllItems();
        for(String playerName : playersList) {
            users.addItem(new Object[] {playerName}, null);
        }
        if(users.size() > 10) users.setPageLength(10);
        else users.setPageLength(users.size());
    }

    public void sitDown() {
        sitdown.setCaption(STAND_UP_TEXT);
    }

    public void standUp() {
        sitdown.setCaption(SIT_DOWN_TEXT);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        standUp();
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for(LobbyViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
    }

    @Override
    public void addListener(LobbyViewListener listener) {
        listeners.add(listener);
    }
}

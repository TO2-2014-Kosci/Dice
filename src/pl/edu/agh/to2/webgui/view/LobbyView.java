package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-11-28.
 * Edited by Lukasz on 2014-12-01
 */
public class LobbyView extends CustomComponent
    implements ILobbyView, View,Button.ClickListener {
    public static final String NAME = "lobby";
    public static final String LEAVE_TEXT = "Leave lobby";
    public static final String SIT_DOWN_TEXT = "Sit down";
    public static final String STAND_UP_TEXT = "Sit down";
    public static final String START_TEXT = "Start game (test)";

    List<LobbyViewListener> listeners = new ArrayList<LobbyViewListener>();
    Table users = new Table("Users in lobby");
    Panel panel = new Panel();
    Label info = new Label();
    Button leave = new Button (LEAVE_TEXT,this);
    Button sitdown = new Button(SIT_DOWN_TEXT, this);
    Button start = new Button(START_TEXT, this); //temporary button
    GridLayout panelLayout = new GridLayout(1,5);

    public LobbyView() {
        setSizeFull();
        users.addContainerProperty("User", String.class, null);
        users.setPageLength(users.size());
//        users.addItem(new Object[]{"X"}, 1);
//        users.addItem(new Object[]{"Y"} , 2);
//        users.addItem(new Object[]{"Z"}, 3);
        panelLayout.setWidth("100%");
        panelLayout.addComponent(info, 0, 0);
        panelLayout.addComponent(users,0,1);
        panelLayout.addComponent(leave,0,2);
        panelLayout.addComponent(sitdown,0,3);
        panelLayout.addComponent(start, 0, 4); //temporary button
        panel.setContent(panelLayout);
        setCompositionRoot(panel);
    }

    public void showNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
    }

    public void setPlayersList(List<String> playersList) {
        users.removeAllItems();
        for(String playerName : playersList) {
            users.addItem(new Object[] {playerName});
        }
    }

    public void sitDown() {
        sitdown.setCaption(STAND_UP_TEXT);
    }

    public void standUp() {
        sitdown.setCaption(SIT_DOWN_TEXT);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for(LobbyViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
//        if(clickEvent.getButton().getCaption().equalsIgnoreCase(SIT_DOWN_TEXT)) {
//            clickEvent.getButton().setCaption(STAND_UP_TEXT);
//        }
//        else if(clickEvent.getButton().getCaption().equalsIgnoreCase(STAND_UP_TEXT)){
//            clickEvent.getButton().setCaption(SIT_DOWN_TEXT);
//        }
    }

    @Override
    public void addListener(LobbyViewListener listener) {
        listeners.add(listener);
    }
}

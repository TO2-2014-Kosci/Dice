package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
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
    List<LobbyViewListener> listeners = new ArrayList<LobbyViewListener>();
    Table users = new Table("Users in lobby");
    Panel panel = new Panel();
    Label info = new Label();
    Button leave = new Button ("Leave lobby",this);
    Button sitdown = new Button("Sit down", this);
    Button start = new Button("Start game", this); //temporary button
    GridLayout panelLayout = new GridLayout(1,5);
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
        panelLayout.addComponent(sitdown,0,3);
        panelLayout.addComponent(start, 0, 4); //temporary button
        panel.setContent(panelLayout);
        setCompositionRoot(panel);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for(LobbyViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
        if(clickEvent.getButton().getCaption().equalsIgnoreCase("sit down")) {
            clickEvent.getButton().setCaption("Stand Up");
        }
        else if(clickEvent.getButton().getCaption().equalsIgnoreCase("stand up")){
            clickEvent.getButton().setCaption("Sit down");
        }
    }

    @Override
    public void addListener(LobbyViewListener listener) {
        listeners.add(listener);
    }
}

package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.ui.Notification;
import pl.edu.agh.to2.webgui.view.ILobbyView;
import pl.edu.agh.to2.webgui.view.LobbyView;

/**
 * Created by Maciej on 2014-11-28.
 * Edited by Lukasz on 2014-12-01
 */
public class LobbyPresenter implements ILobbyView.LobbyViewListener {
    LobbyView lobbyView;
    public LobbyPresenter(LobbyView lobbyView){
        this.lobbyView = lobbyView;
        lobbyView.addListener(this);
    }
    public void buttonClick(String operation) {
        if(operation.equalsIgnoreCase("leave lobby")) {
            lobbyView.getUI().getNavigator().navigateTo(MainView.NAME);
        }
        else if(operation.equalsIgnoreCase("sit down")){
            Notification.show("You've sat down");
        }
        else if(operation.equalsIgnoreCase("stand up")){
            Notification.show("You've stood up");
        }

    }

}

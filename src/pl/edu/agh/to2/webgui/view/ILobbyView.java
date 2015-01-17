package pl.edu.agh.to2.webgui.view;

import java.util.List;

/**
 * Created by Maciej on 2014-11-28.
 */
public interface ILobbyView {
    interface LobbyViewListener {
        void buttonClick(String operation);
    }

    public void addListener(LobbyViewListener listener);
    public void showNotification(String message, String style);
}

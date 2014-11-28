package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-11-28.
 */
public interface ILobbyView {
    interface LobbyViewListener {
        void buttonClick(char operation);
    }

    public void addListener(LobbyViewListener listener);
}

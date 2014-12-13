package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-12-02.
 */
public interface ICreateGameView {
    interface CreateGameViewListener {
        void buttonClick(String operation);
        void menuSelected(String operation);
    }

    public void addListener(CreateGameViewListener listener);
    public void showNotification(String message);
}

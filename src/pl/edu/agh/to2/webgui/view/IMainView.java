package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-12-01.
 */
public interface IMainView {
    interface MainViewListener {
        void buttonClick(String operation);
        void menuSelected(String command);
        void valueChange(String gameName, Boolean isStarted);
    }

    public void addListener(MainViewListener listener);
    public void showNotification(String message);
}

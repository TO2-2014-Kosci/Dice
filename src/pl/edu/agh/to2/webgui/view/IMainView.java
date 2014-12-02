package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-12-01.
 */
public interface IMainView {
    interface MainViewListener {
        void buttonClick(String operation);
        void menuSelected(String command);
    }

    public void addListener(MainViewListener listener);
}

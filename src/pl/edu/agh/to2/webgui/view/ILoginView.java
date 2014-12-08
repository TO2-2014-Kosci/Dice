package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-11-28.
 */
public interface ILoginView {
    interface LoginViewListener {
        void buttonClick(String username);
    }

    public void addListener(LoginViewListener listener);
    public void showNotification(String message);
}

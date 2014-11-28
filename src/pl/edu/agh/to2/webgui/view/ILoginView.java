package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-11-28.
 */
public interface ILoginView {
    interface LoginViewListener {
        void buttonClick(char operation);
    }

    public void addListener(LoginViewListener listener);
}

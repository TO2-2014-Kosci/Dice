package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.view.ILoginView;
import pl.edu.agh.to2.webgui.view.LoginView;

/**
 * Created by Maciej on 2014-11-28.
 */
public class LoginPresenter implements ILoginView.LoginViewListener {
    private LoginView view;


    public LoginPresenter(LoginView view) {
        this.view = view;
        view.addListener(this);
    }

    @Override
    public void buttonClick(String username) {
//        VaadinSession.getCurrent().setAttribute("user", username);
        view.navigateToMainView(username);
    }
}

package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import pl.edu.agh.to2.webgui.view.ILoginView;
import pl.edu.agh.to2.webgui.view.LoginView;
import pl.edu.agh.to2.webgui.view.MainView;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

/**
 * Created by Maciej on 2014-11-28.
 */
public class LoginPresenter implements ILoginView.LoginViewListener {
    private LoginView view;
    private LocalConnectionProxy lcp;


    public LoginPresenter(LoginView view, LocalConnectionProxy lcp) {
        this.view = view;
//        this.lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
        view.addListener(this);
    }

    @Override
    public void buttonClick(String username) {
        if(!username.equals("")) {
            Response response = lcp.login(username);
            if (response.isSuccess()) {
                System.out.println("User logged in: " + username);
                VaadinSession.getCurrent().setAttribute("user", username);
                view.getUI().getNavigator().navigateTo(MainView.NAME);
            }
            else {
                view.showNotification(response.message);
            }
        }
        else {
            view.showNotification("Enter valid username!");
        }

    }
}

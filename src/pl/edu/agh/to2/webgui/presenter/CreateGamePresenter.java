package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.view.CreateGameView;
import pl.edu.agh.to2.webgui.view.ICreateGameView;
import pl.edu.agh.to2.webgui.view.LoginView;
import pl.edu.agh.to2.webgui.view.MainView;
import sun.applet.Main;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGamePresenter implements ICreateGameView.CreateGameViewListener {
    private CreateGameView view;

    public CreateGamePresenter(CreateGameView view) {
        this.view = view;
        this.view.addListener(this);
    }

    @Override
    public void buttonClick(String operation) {
        if(operation != null) {
            if (operation.equals(CreateGameView.CREATE_TEXT)) {
//                view.getUI().getNavigator().navigateTo(LobbyView.NAME);
            }
            else if (operation.equals(CreateGameView.CANCEL_TEXT)) {
                view.getUI().getNavigator().navigateTo(MainView.NAME);
            }
        }
    }

    @Override
    public void menuSelected(String operation) {
        if(operation != null) {
            if (operation.equals(CreateGameView.LOGOUT_TEXT)) {
                VaadinSession.getCurrent().setAttribute("user", null);
                view.getUI().getNavigator().navigateTo(LoginView.NAME);
            } else if (operation.equals(CreateGameView.CANCEL_TEXT)) {
                view.getUI().getNavigator().navigateTo(MainView.NAME);
            }
        }
    }
}

package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import pl.edu.agh.to2.webgui.view.IMainView;
import pl.edu.agh.to2.webgui.view.LoginView;
import pl.edu.agh.to2.webgui.view.MainView;

/**
 * Created by Maciej on 2014-12-02.
 */
public class MainPresenter implements IMainView.MainViewListener {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
        view.addListener(this);

    }
    @Override
    public void buttonClick(String username) {
//        view.getUI().getNavigator().navigateTo(LobbyView.NAME);
    }

    @Override
    public void menuSelected(String command) {
        if(command != null) {
            if (command.equals(MainView.LOGOUT_TEXT)) {
                VaadinSession.getCurrent().setAttribute("user", null);
                view.getUI().getNavigator().navigateTo(LoginView.NAME);
            } else if (command.equals(MainView.CREATE_TEXT)) {
//                view.getUI().getNavigator().navigateTo(CreateGameView.NAME);
            }
        }
    }
}

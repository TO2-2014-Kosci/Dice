package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.*;
import to2.dice.game.GameInfo;
import to2.dice.game.GameState;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

import java.util.List;

/**
 * Created by Maciej on 2014-12-02.
 */
public class MainPresenter implements IMainView.MainViewListener {
    private MainView view;
    private LocalConnectionProxy lcp;
    private String gameName;

    public MainPresenter(MainView view) {
        this.view = view;
        view.addListener(this);
        this.lcp = WebGUI.lcp;
    }
    @Override
    public void buttonClick(String username) {
        Response response = lcp.joinRoom(gameName, username);
        if (response.isSuccess()) {
            view.getUI().getNavigator().navigateTo(LobbyView.NAME);
        }
        else {
            view.showNotification(response.message);
        }

    }

    @Override
    public void menuSelected(String command) {
        if(command != null) {
            if (command.equals(MainView.LOGOUT_TEXT)) {
//                Response response = lcp.l TODO dodac logout
                VaadinSession.getCurrent().setAttribute("user", null);
                view.getUI().getNavigator().navigateTo(LoginView.NAME);
            } else if (command.equals(MainView.CREATE_TEXT)) {
                view.getUI().getNavigator().navigateTo(CreateGameView.NAME);
            } else if (command.equals(MainView.REFRESH_TEXT)) {
                List<GameInfo> gamesList = lcp.getRoomList();
                // TODO odswiezyc liste gier w widoku
            }
        }
    }

    @Override
    public void valueChange(String gameName) {
        this.gameName = gameName;
        view.join.setEnabled(true);
    }
}

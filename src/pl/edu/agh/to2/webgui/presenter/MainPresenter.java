package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import pl.edu.agh.to2.webgui.MessageListener;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.*;
import to2.dice.game.GameInfo;
import to2.dice.game.GameState;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Maciej on 2014-12-02.
 */
public class MainPresenter implements IMainView.MainViewListener {
    private MainView view;
    private LocalConnectionProxy lcp;
    private String gameName;

    public MainPresenter(MainView view, LocalConnectionProxy lcp) {
        this.view = view;
        view.addListener(this);
        this.lcp = lcp;
    }
    @Override
    public void buttonClick(String username) {
        Response response = lcp.joinRoom(gameName);
        if (response.isSuccess()) {
            view.getUI().getSession().setAttribute("gameName", gameName);
//            if(isStarted) {
//                view.getUI().getSession().setAttribute("state", GameView.NAME);
//                view.getUI().getNavigator().navigateTo(GameView.NAME);
//            }
//            else {
                view.getUI().getSession().setAttribute("state", LobbyView.NAME);
                view.getUI().getNavigator().navigateTo(LobbyView.NAME);
//            }
        }
        else {
            view.showNotification(response.message, "failure");
        }
    }

    @Override
    public void menuSelected(String command) {
        if(command != null) {
            switch (command) {
                case MainView.LOGOUT_TEXT:
                    try {
                        Response response = lcp.logout();
                        if (response.isSuccess()) {
                            VaadinSession.getCurrent().setAttribute("user", null);
                            view.showNotification("You have successfully logged out", "success");
                            view.getUI().getSession().setAttribute("state", LoginView.NAME);
                            view.getUI().getNavigator().navigateTo(LoginView.NAME);
                        } else {
                            view.showNotification(response.message, "failure");
                        }
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    break;
                case MainView.CREATE_TEXT:
                    view.getUI().getSession().setAttribute("state", CreateGameView.NAME);
                    view.getUI().getNavigator().navigateTo(CreateGameView.NAME);
                    break;
                case MainView.REFRESH_TEXT:
                    List<GameInfo> gamesList = lcp.getRoomList();
                    List<Object[]> games = new ArrayList<Object[]>();
                    for (GameInfo gi : gamesList) {
                        games.add(new Object[]{gi.getSettings().getName(), gi.getPlayersNumber() + "/" + gi.getSettings().getMaxPlayers(), gi.getSettings().getGameType().toString(), gi.isGameStarted(), gi.getSettings().getRoundsToWin()});
                    }
                    view.refreshGamesList(games);
                    view.showNotification("Games list refreshed", "success");
                    break;
            }
        }
    }

    @Override
    public void valueChange(String gameName) {
        this.gameName = gameName;
        view.join.setEnabled(true);
    }
}

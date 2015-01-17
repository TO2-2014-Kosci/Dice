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
    private Boolean isStarted;

    public MainPresenter(MainView view, LocalConnectionProxy lcp) {
        this.view = view;
        view.addListener(this);
        this.lcp = lcp;
//        this.lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
    }
    @Override
    public void buttonClick(String username) {
        Response response = lcp.joinRoom(gameName);
        if (response.isSuccess()) {
            view.getUI().getSession().setAttribute("gameName", gameName);
            if(isStarted) { //TODO chyba mozna wywalic
                view.getUI().getSession().setAttribute("state", GameView.NAME);
                view.getUI().getNavigator().navigateTo(GameView.NAME);
            }
            else {
                view.getUI().getSession().setAttribute("state", LobbyView.NAME);
                view.getUI().getNavigator().navigateTo(LobbyView.NAME);
            }
        }
        else {
            view.showNotification(response.message, "failure");
        }
    }

    @Override
    public void menuSelected(String command) {
        if(command != null) {
            if (command.equals(MainView.LOGOUT_TEXT)) {
//                Response response = lcp.l TODO dodac logout
                VaadinSession.getCurrent().setAttribute("user", null);
                view.getUI().getSession().setAttribute("state", LoginView.NAME);
                view.getUI().getNavigator().navigateTo(LoginView.NAME);
            } else if (command.equals(MainView.CREATE_TEXT)) {
                view.getUI().getSession().setAttribute("state", CreateGameView.NAME);
                view.getUI().getNavigator().navigateTo(CreateGameView.NAME);
            } else if (command.equals(MainView.REFRESH_TEXT)) {
                List<GameInfo> gamesList = lcp.getRoomList();
                List<Object[]> games = new ArrayList<Object[]>();
                for (GameInfo gi : gamesList) {
                    games.add(new Object[] {gi.getSettings().getName(), gi.getPlayersNumber() + "/" + gi.getSettings().getMaxPlayers(), gi.getSettings().getGameType().toString(), gi.isGameStarted(), gi.getSettings().getRoundsToWin()});
                }
                view.refreshGamesList(games);
                view.showNotification("Games list refreshed", "success");
            }
        }
    }

    @Override
    public void valueChange(String gameName, Boolean isStarted) {
        this.gameName = gameName;
        this.isStarted = isStarted;
        view.join.setEnabled(true);
    }
}

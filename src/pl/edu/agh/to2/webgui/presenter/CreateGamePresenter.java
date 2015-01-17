package pl.edu.agh.to2.webgui.presenter;

import com.google.gwt.i18n.client.NumberFormat;
import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.*;
import to2.dice.game.BotLevel;
import to2.dice.game.GameInfo;
import to2.dice.game.GameSettings;
import to2.dice.game.GameType;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

import javax.validation.constraints.Null;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGamePresenter implements ICreateGameView.CreateGameViewListener {
    private CreateGameView view;
    private LocalConnectionProxy lcp;

    public CreateGamePresenter(CreateGameView view, LocalConnectionProxy lcp) {
        this.view = view;
        this.view.addListener(this);
        this.lcp = lcp;
//        this.lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
    }

    @Override
    public void buttonClick(String operation) {
        if(operation != null) {
            if (operation.equals(CreateGameView.CREATE_TEXT)) {
                Response response = null;
                GameSettings gs = null;
                try {
                    gs = buildGameSettings();
                } catch (NumberFormatException | NullPointerException e) {
                    view.showNotification("Please put valid settings");
                    return;
                }
                System.out.println(lcp.toString());
                response = lcp.createRoom(gs);
                if (response.isSuccess()) {
                    view.getUI().getSession().setAttribute("gameName", gs.getName());
                    view.getUI().getSession().setAttribute("state", LobbyView.NAME);
                    view.getUI().getNavigator().navigateTo(LobbyView.NAME);
                }
                else {
                    view.showNotification(response.message);
                }
            }
            else if (operation.equals(CreateGameView.CANCEL_TEXT)) {
                view.getUI().getSession().setAttribute("state", MainView.NAME);
                view.getUI().getNavigator().navigateTo(MainView.NAME);
            }
        }
    }

    @Override
    public void menuSelected(String operation) {
        if(operation != null) {
            switch (operation) {
                case CreateGameView.LOGOUT_TEXT:
                    VaadinSession.getCurrent().setAttribute("user", null);
                    view.getUI().getSession().setAttribute("state", LoginView.NAME);
                    view.getUI().getNavigator().navigateTo(LoginView.NAME);
                    break;
                case CreateGameView.CANCEL_TEXT:
                    view.getUI().getSession().setAttribute("state", MainView.NAME);
                    view.getUI().getNavigator().navigateTo(MainView.NAME);
                    break;
                case CreateGameView.RANDOM_TEXT:
                    view.setRandom();
                    break;
            }
        }
    }

    private GameSettings buildGameSettings() throws NumberFormatException, NullPointerException {
        GameType gameType = view.getGameType();
        int diceNumber;
        if (gameType.equals(GameType.POKER)) {
            diceNumber = 5;
        }
        else {
            diceNumber = view.getDiceNumber();
        }
        String gameName = view.getGameName();
        int maxHumanPlayers = view.getPlayersNumber();
        int timeForMove = view.getTimeForMove();
        int maxInactiveTurns = view.getMaxInactiveTurns();
        int roundsToWin = view.getRoundsToWin();

        Map<BotLevel,Integer> bots = view.getBots();

        return new GameSettings(gameType, diceNumber, gameName, maxHumanPlayers, timeForMove, maxInactiveTurns, roundsToWin, bots);
    }
}

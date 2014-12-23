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

import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGamePresenter implements ICreateGameView.CreateGameViewListener {
    private CreateGameView view;
    private LocalConnectionProxy lcp;

    public CreateGamePresenter(CreateGameView view) {
        this.view = view;
        this.view.addListener(this);
//        this.lcp = WebGUI.lcp;
        this.lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
    }

    @Override
    public void buttonClick(String operation) {
        if(operation != null) {
            if (operation.equals(CreateGameView.CREATE_TEXT)) {
                Response response = null;
                GameSettings gs = null;
                try {
                    gs = buildGameSettings();
                } catch (NumberFormatException e) {
                    view.showNotification("Please put valid settings");
                    return;
                }
                response = lcp.createRoom(gs);
                if (response.isSuccess()) {
                    view.getUI().getNavigator().navigateTo(LobbyView.NAME);
                }
                else {
                    view.showNotification(response.message);
                }
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

    private GameSettings buildGameSettings() throws NumberFormatException {
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

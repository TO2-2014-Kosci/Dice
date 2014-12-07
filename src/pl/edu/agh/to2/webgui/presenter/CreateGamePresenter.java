package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.*;
import to2.dice.game.GameInfo;
import to2.dice.game.GameSettings;
import to2.dice.game.GameType;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGamePresenter implements ICreateGameView.CreateGameViewListener {
    private CreateGameView view;
    private LocalConnectionProxy lcp;

    public CreateGamePresenter(CreateGameView view) {
        this.view = view;
        this.view.addListener(this);
        this.lcp = WebGUI.lcp;
    }

    @Override
    public void buttonClick(String operation) {
        if(operation != null) {
            if (operation.equals(CreateGameView.CREATE_TEXT)) {
                Response response = lcp.createRoom(buildGameSettings(), (String) VaadinSession.getCurrent().getAttribute("user"));
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

    private GameSettings buildGameSettings() {
        GameType gameType = view.getGameType();
        int diceNumber = 5; // TODO ogarnac co z tym zrobic
        String gameName = view.getGameName();
        int maxHumanPlayers = view.getPlayersNumber();
        int timeForMove = view.getTimeForMove();
        int maxInactiveTurns = view.getMaxInactiveTurns();
        int roundsToWin = view.getRoundsToWin();
        // TODO zrobic pobieranie botow

        return new GameSettings(gameType, diceNumber, gameName, maxHumanPlayers, timeForMove, maxInactiveTurns, roundsToWin, null);
    }
}

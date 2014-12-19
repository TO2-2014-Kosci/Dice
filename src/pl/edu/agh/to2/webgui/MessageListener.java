package pl.edu.agh.to2.webgui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import pl.edu.agh.to2.webgui.presenter.GamePresenter;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;
import pl.edu.agh.to2.webgui.view.GameView;
import pl.edu.agh.to2.webgui.view.LobbyView;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.server.ServerMessageListener;



import java.util.List;

/**
 * Created by Maciej on 2014-12-07.
 */
public class MessageListener implements ServerMessageListener {
    private GamePresenter gamePresenter;
    private LobbyPresenter lobbyPresenter;
    private boolean gameStarted = false;

    @Override
    public void onGameStateChange(GameState gameState) {
        String username = (String) VaadinSession.getCurrent().getAttribute("user");
        System.out.println("Incomming message to:\t" + username);
        System.out.println(this.toString());
        LocalConnectionProxy lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
        System.out.println(((LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp")).getLoggedInUser());
        if (gamePresenter != null && lobbyPresenter != null) {
            if (gameState.isGameStarted() && !gameStarted) { //rozpoczecie gry
                System.out.println("Starting game...");
                gameStarted = true;
                UI.getCurrent().getNavigator().navigateTo(GameView.NAME);
            }
            else if(!gameState.isGameStarted()) { //lobby
                lobbyPresenter.updateGameState(gameState);
            }
        }
    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void setLobbyPresenter(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
    }
}

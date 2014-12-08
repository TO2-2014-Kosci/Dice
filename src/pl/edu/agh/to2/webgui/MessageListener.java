package pl.edu.agh.to2.webgui;

import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.presenter.GamePresenter;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;
import pl.edu.agh.to2.webgui.view.GameView;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.server.ServerMessageListener;

import java.util.List;

/**
 * Created by Maciej on 2014-12-07.
 */
public class MessageListener implements ServerMessageListener {
    private GamePresenter gamePresenter;
    private LobbyPresenter lobbyPresenter;
    private boolean gameStarted = false; // TODO inaczej to zrobic

    @Override
    public void onGameStateChange(GameState gameState) {
        List<Player> players = gameState.getPlayers();
        String username = (String) VaadinSession.getCurrent().getAttribute("user");
        for(Player player : players) {
            if (player.getName().equals(username) && gamePresenter != null && lobbyPresenter != null) {
                if (gameState.isGameStarted() && !gameStarted) {
                    lobbyPresenter.startGame();
                    gameStarted = true;
                }
                if (gameState.isGameStarted()) { //Game presenter
                    gamePresenter.updateGameState(gameState);
                }
                else {
                    lobbyPresenter.updateGameState(gameState);
                    gameStarted = false;
                }
                break;
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

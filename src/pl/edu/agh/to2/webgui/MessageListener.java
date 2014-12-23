package pl.edu.agh.to2.webgui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import pl.edu.agh.to2.webgui.presenter.GamePresenter;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;
import pl.edu.agh.to2.webgui.presenter.ScorePresenter;
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
    private final WebGUI ui;
    private GamePresenter gamePresenter;
    private LobbyPresenter lobbyPresenter;
    private ScorePresenter scorePresenter;
    private boolean gameStarted = false;

    public MessageListener(WebGUI ui) {
        this.ui = ui;
    }

    @Override
    public void onGameStateChange(GameState gameState) {
        System.out.println("Incomming message to:\t" + this.toString());
//        System.out.println(Page.getCurrent().);

        if (lobbyPresenter != null && gameState.isGameStarted() && !gameStarted) { //rozpoczecie gry
            gameStarted = true;
            ui.access(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Starting game...");
                    lobbyPresenter.startGame();
                }
            });
        }
        ui.access(new GameStateFeeder(gameState));
    }

    private class GameStateFeeder implements Runnable {
        private final GameState gameState;

        public GameStateFeeder(GameState gameState) {
            this.gameState = gameState;
        }

        @Override
        public void run() {
            if (lobbyPresenter != null && !gameState.isGameStarted() && !gameStarted) { //aktualizacja lobby
                gameStarted = false;
                lobbyPresenter.updateGameState(gameState);
            }
            else if(gamePresenter != null && gameState.isGameStarted() && gameStarted) { //aktualizacja gry
                gamePresenter.updateGameState(gameState);
            }
            else if(gamePresenter != null && !gameState.isGameStarted() && gameStarted) {
                gamePresenter.endGame();
            }
            if(scorePresenter != null && !gameState.isGameStarted() && gameStarted) {
                gameStarted = false;
                scorePresenter.updateGameState(gameState);
            }
        }
    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void setLobbyPresenter(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
    }

    public void setScorePresenter(ScorePresenter scorePresenter) {
        this.scorePresenter = scorePresenter;
    }

    public void setGameStarted(Boolean gameStarted) { //TODO zmienic to
        this.gameStarted = gameStarted;
    }
}

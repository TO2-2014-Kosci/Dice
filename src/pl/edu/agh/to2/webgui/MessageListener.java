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
import pl.edu.agh.to2.webgui.view.ScoreView;
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
//    private boolean finished = false;

    public MessageListener(WebGUI ui) {
        this.ui = ui;
    }

    @Override
    public void onGameStateChange(GameState gameState) {
        ui.access(new GameStateFeeder(gameState));
    }

    private class GameStateFeeder implements Runnable {
        private final GameState gameState;

        public GameStateFeeder(GameState gameState) {
            this.gameState = gameState;
        }

        @Override
        public void run() {
            if (gameState.isGameStarted() && ui.getNavigator().getState().equals(LobbyView.NAME)) { // jestesmy w lobby i rozpoczynamy gre
                lobbyPresenter.startGame();
                gamePresenter.updateGameState(gameState);
            }
            if (!gameState.isGameStarted() && ui.getNavigator().getState().equals(LobbyView.NAME)) { // jestesmy w lobby i aktualizujemy liste graczy
                lobbyPresenter.updateGameState(gameState);
            }
            else if(gameState.isGameStarted() && ui.getNavigator().getState().equals(GameView.NAME)) { // jestesmy w grze, gra trwa i aktualizujemy widok
                gamePresenter.updateGameState(gameState);
//                finished = false;
            }
            else if(!gameState.isGameStarted() && ui.getNavigator().getState().equals(GameView.NAME)) { // jestesmy w grze, gra sie konczy
                gamePresenter.endGame();
                scorePresenter.updateGameState(gameState);
            }
//            if(!gameState.isGameStarted() && ui.getNavigator().getState().equals(ScoreView.NAME) && !finished) {
//                scorePresenter.updateGameState(gameState);
//                finished = true;
//            }
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
}

package pl.edu.agh.to2.webgui;

import pl.edu.agh.to2.webgui.presenter.GamePresenter;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;
import pl.edu.agh.to2.webgui.view.GameView;
import to2.dice.game.GameState;
import to2.dice.server.ServerMessageListener;

/**
 * Created by Maciej on 2014-12-07.
 */
public class MessageListener implements ServerMessageListener {
    private GamePresenter gamePresenter;
    private LobbyPresenter lobbyPresenter;

    @Override
    public void onGameStateChange(GameState gameState) {
        if(gamePresenter != null && gameState.isGameStarted()) { //Game presenter
            gamePresenter.updateGameState(gameState);
        }
        else if (lobbyPresenter != null) {
            lobbyPresenter.updateGameState(gameState);
        }
    }

    public void setGamePresenter(GamePresenter gamePresenter) {
        this.gamePresenter = gamePresenter;
    }

    public void setLobbyPresenter(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
    }
}

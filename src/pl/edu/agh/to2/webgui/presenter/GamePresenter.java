package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.GameView;
import pl.edu.agh.to2.webgui.view.IGameView;
import pl.edu.agh.to2.webgui.view.MainView;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lukasz on 01.12.14.
 */
public class GamePresenter implements IGameView.GameViewListener {
    private GameView view;
    private LocalConnectionProxy lcp;
    private String username;

    public GamePresenter(GameView gameView) {
        this.view = gameView;
        gameView.addListener(this);
        this.lcp = WebGUI.lcp;
        this.username = (String) VaadinSession.getCurrent().getAttribute("user");
    }
    public void buttonClick(String operation) {
        if(operation.equalsIgnoreCase(GameView.LEAVE_TEXT)) {
            Response response = lcp.leaveRoom(username);
            if(response.isSuccess()) {
                view.showNotification("You left game");
                view.getUI().getNavigator().navigateTo(MainView.NAME);
            }
            else {
                view.showNotification(response.message);
            }
        }
        else if(operation.equalsIgnoreCase(GameView.REROLL_TEXT)) {
            boolean[] dicesToReroll = view.getDices();
            Response response = lcp.reroll(dicesToReroll, username);
            if(response.isSuccess()) {
                view.showNotification("Dices rerolled");
            }
            else {
                view.showNotification(response.message);
            }
        }
    }

    public void updateGameState(GameState gameState) {
        List<Player> players = gameState.getPlayers();
        List<Object[]> updatedPlayersList = new ArrayList<Object[]>();
        for (Player p : players) {
            String playerName = p.getName();
            Integer playerScore = p.getScore();
            int[] playerDices = p.getDice().getDice();
            updatedPlayersList.add(new Object[]{playerName, playerScore, Arrays.toString(playerDices)});
            if (playerName == username) {
                view.setDices(playerDices);
            }
        }
        view.updatePlayersList(updatedPlayersList);
    }
}

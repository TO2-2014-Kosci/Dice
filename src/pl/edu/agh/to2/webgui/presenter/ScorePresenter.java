package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.server.VaadinSession;
import pl.edu.agh.to2.webgui.MessageListener;
import pl.edu.agh.to2.webgui.view.IScoreView;
import pl.edu.agh.to2.webgui.view.MainView;
import pl.edu.agh.to2.webgui.view.ScoreView;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-12-23.
 */
public class ScorePresenter implements IScoreView.ScoreViewListener {
    private ScoreView view;
//    private LocalConnectionProxy lcp;

    public ScorePresenter(ScoreView scoreView) {
        this.view = scoreView;
        scoreView.addListener(this);
//        this.lcp = (LocalConnectionProxy) VaadinSession.getCurrent().getAttribute("lcp");
//        ((MessageListener) VaadinSession.getCurrent().getAttribute("listener")).setScorePresenter(this);
    }

    @Override
    public void buttonClick(String operation) {
        if(operation.equals(ScoreView.EXIT_TEXT)) {
//            Response response = lcp.leaveRoom();
//            if(response.isSuccess()) {
            view.getUI().getSession().setAttribute("state", MainView.NAME);
                view.getUI().getNavigator().navigateTo(MainView.NAME);
//            }
//            else {
//                view.showNotification(response.message);
//            }
        }

    }

    public void updateGameState(GameState gameState) {
        List<Player> players = gameState.getPlayers();
        List<Object[]> updatedPlayersList = new ArrayList<Object[]>();
        for (Player p : players) {
            String playerName = p.getName();
            Integer playerScore = p.getScore();
            updatedPlayersList.add(new Object[]{playerName, playerScore});
        }
        view.updatePlayersList(updatedPlayersList);
    }
}

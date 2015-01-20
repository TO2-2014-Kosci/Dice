package pl.edu.agh.to2.webgui.presenter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Timer;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.apache.commons.lang3.time.StopWatch;
import pl.edu.agh.to2.webgui.MessageListener;
import pl.edu.agh.to2.webgui.WebGUI;
import pl.edu.agh.to2.webgui.view.GameView;
import pl.edu.agh.to2.webgui.view.IGameView;
import pl.edu.agh.to2.webgui.view.MainView;
import pl.edu.agh.to2.webgui.view.ScoreView;
import to2.dice.game.GameInfo;
import to2.dice.game.GameState;
import to2.dice.game.NGameState;
import to2.dice.game.Player;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.messaging.Response;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by lukasz on 01.12.14.
 */
public class GamePresenter implements IGameView.GameViewListener {
    private GameView view;
    private LocalConnectionProxy lcp;
    private int moveTime;
    private boolean gotGameInfo = false;
    private java.util.Timer timer = new java.util.Timer();
    private UpdateThread updateThread;
    private boolean left = false;

    public GamePresenter(GameView gameView, LocalConnectionProxy lcp) {
        this.view = gameView;
        gameView.addListener(this);
        this.lcp = lcp;

    }

    public void buttonClick(String operation) {
        if (operation.equalsIgnoreCase(GameView.LEAVE_TEXT)) {
            Response response = lcp.leaveRoom();
            if (!response.isSuccess()) {
                view.showNotification(response.message, "failure", Position.BOTTOM_CENTER);
            }
            gotGameInfo = false;
            view.showNotification("You left game", "success", Position.BOTTOM_CENTER);
            view.getUI().getSession().setAttribute("state", MainView.NAME);
            view.getUI().getNavigator().navigateTo(MainView.NAME);
        } else if (operation.equalsIgnoreCase(GameView.REROLL_TEXT)) {
            boolean[] dicesToReroll = view.getDices();
            Response response = null;
            response = lcp.reroll(dicesToReroll);
            if (response.isSuccess()) {
                view.showNotification("Dices rerolled", "success", Position.BOTTOM_CENTER);
                view.enableReroll(false);
            } else {
                view.showNotification(response.message, "failure", Position.BOTTOM_CENTER);
            }
        } else if (operation.equalsIgnoreCase(GameView.STAND_UP_TEXT)) {
            Response response = lcp.standUp();
            if (response.isSuccess()) {
                left = true;
                view.showNotification("You stood up", "success", Position.BOTTOM_CENTER);
                view.enablePlayerUI(false);
            }
        }

    }

    public void updateGameState(GameState gameState) {
        boolean exists;

        view.resetProgressBar();
        if (updateThread != null) {
            updateThread.interrupt();
        }
        if(gameState.getCurrentPlayer() != null) {
            updateThread = new UpdateThread();
            updateThread.start();
        }

        if (!gotGameInfo) {
            buildInfo();
            gotGameInfo = true;
        }
        if (gameState.getClass().equals(NGameState.class)) {
            view.setHeader("NGame: " + ((NGameState) gameState).getWinningNumber());
        }
        if (gameState.getCurrentPlayer() != null) {
            view.setInfo("Current player: " + gameState.getCurrentPlayer().getName());
        }
        else {
            view.setInfo("End of round");
        }
        List<Player> players = gameState.getPlayers();
        List<Object[]> updatedPlayersList = new ArrayList<Object[]>();
        exists = false;
        for (Player p : players) {
            String playerName = p.getName();
            Integer playerScore = p.getScore();
            int[] playerDices = p.getDice().getDiceArray();
            updatedPlayersList.add(new Object[]{playerName, playerScore, Arrays.toString(playerDices).replace("[", "").replace("]", "")});
            if (playerName.equals(VaadinSession.getCurrent().getAttribute("user"))) {
                view.enablePlayerUI(true);
                view.setDices(playerDices);
                exists = true;
                view.setNotificationFlag(false);
            }
        }
        if(!exists) {
            if(left) {
                left = false;
                view.setNotificationFlag(true);
            }
            else if(!view.getNotificationFlag()) {
                view.showNotification("You've been kicked for prolonged inactivity", "system failure", Position.TOP_CENTER);
                view.setNotificationFlag(true);
            }
            view.enablePlayerUI(false);
        }
        view.updatePlayersList(updatedPlayersList);

        String username = (String) VaadinSession.getCurrent().getAttribute("user");

        if (gameState.getCurrentPlayer() != null && gameState.getCurrentPlayer().getName().equals(username)) {
            view.enableReroll(true);
            view.showNotification("Your turn", "dark", Position.MIDDLE_CENTER);
        }
        else {
            view.enableReroll(false);
        }



    }

    public void endGame() {
        view.getUI().getSession().setAttribute("state", ScoreView.NAME);
        view.getUI().getNavigator().navigateTo(ScoreView.NAME);
        lcp.leaveRoom();
    }

    private void buildInfo() {

        List<GameInfo> gameInfoList = lcp.getRoomList();

        for (GameInfo gi : gameInfoList) {
            if (gi.getSettings().getName().equals(view.getUI().getSession().getAttribute("gameName"))) {
                int roundsCount = gi.getSettings().getRoundsToWin();
                view.setRoundInfo(roundsCount + " rounds to win ");
                moveTime = gi.getSettings().getTimeForMove();
                return;
            }
        }

    }

    private class UpdateThread extends Thread {

        int count = 0;

        @Override
        public void run() {
            try {
                // Update the data for a while
                while (count < moveTime) {

                    if (view.getUI() != null) {
                        view.getUI().access(new Runnable() {
                            @Override
                            public void run() {
                                float progress = (float) (count + 1) / (float) moveTime;
                                view.updateProgressBar(progress);

                                count++;
                            }

                        });
                    }
                    Thread.sleep(1000);
                }

            } catch (InterruptedException e) {
                //System.err.println("Closed thread " + this.getId());
            }
        }
    }
}

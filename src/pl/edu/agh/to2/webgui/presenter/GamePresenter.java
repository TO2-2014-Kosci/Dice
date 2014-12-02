package pl.edu.agh.to2.webgui.presenter;

import com.vaadin.ui.Notification;
import pl.edu.agh.to2.webgui.view.GameView;
import pl.edu.agh.to2.webgui.view.IGameView;
import pl.edu.agh.to2.webgui.view.MainView;

/**
 * Created by lukasz on 01.12.14.
 */
public class GamePresenter implements IGameView.GameViewListener {
    GameView gameView;
    public GamePresenter(GameView gameView) {
        this.gameView = gameView;
        gameView.addListener(this);
    }
    public void buttonClick(String operation) {
        if(operation.equalsIgnoreCase("leave game")) {
            Notification.show("You left game");
            gameView.getUI().getNavigator().navigateTo(MainView.NAME);
        }
        else if(operation.equalsIgnoreCase("reroll")) {
            Notification.show("Dices rerolled");
        }
    }
}

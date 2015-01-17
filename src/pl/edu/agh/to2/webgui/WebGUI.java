package pl.edu.agh.to2.webgui;

import com.google.gwt.dev.jjs.SourceInfoCorrelation;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.server.VaadinRequest;
import pl.edu.agh.to2.webgui.presenter.*;
import pl.edu.agh.to2.webgui.view.*;
import to2.dice.game.GameState;
import to2.dice.messaging.LocalConnectionProxy;
import to2.dice.server.Server;
import to2.dice.server.ServerMessageListener;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-11-28.
 */

@Theme("mytheme")
@Push
@PreserveOnRefresh
public class WebGUI extends UI {

    @Override
    public void init(VaadinRequest request) {
        Page.getCurrent().setTitle("Dice");
        Server server = ContextListener.server;
        MessageListener listener = new MessageListener(this);
        getSession().setAttribute("user", null);
        getSession().setAttribute("state", null);
        LocalConnectionProxy lcp = null;
        try {
            lcp = new LocalConnectionProxy(server, listener);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        new Navigator(this, this);

        MainView mainView = new MainView();
        new MainPresenter(mainView, lcp);
        getNavigator().addView(MainView.NAME, mainView);

        LoginView loginView = new LoginView();
        new LoginPresenter(loginView, lcp);
        getNavigator().addView(LoginView.NAME, loginView);

        CreateGameView createGameView = new CreateGameView();
        new CreateGamePresenter(createGameView, lcp);
        getNavigator().addView(CreateGameView.NAME, createGameView);

        GameView gameView = new GameView();
        listener.setGamePresenter(new GamePresenter(gameView, lcp));
        getNavigator().addView(GameView.NAME, gameView);

        LobbyView lobbyView = new LobbyView();
        listener.setLobbyPresenter(new LobbyPresenter(lobbyView, lcp));
        getNavigator().addView(LobbyView.NAME, lobbyView);

        ScoreView scoreView = new ScoreView();
        listener.setScorePresenter(new ScorePresenter(scoreView));
        getNavigator().addView(ScoreView.NAME, scoreView);
        
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = viewChangeEvent.getNewView() instanceof LoginView;

                if(!isLoggedIn && !isLoginView) {
                    getSession().setAttribute("state", LoginView.NAME);
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    getNavigator().navigateTo((String)getSession().getAttribute("state"));
                    return false;
                } else if (!isLoggedIn) {
                    return true;
                }
                if (viewChangeEvent.getViewName().equals(getSession().getAttribute("state"))) {
                    return true;
                } else {
                    getNavigator().navigateTo((String) getSession().getAttribute("state"));
                    return false;
                }
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {

            }
        });
    }
}

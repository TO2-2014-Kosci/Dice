package pl.edu.agh.to2.webgui;

import com.google.gwt.dev.jjs.SourceInfoCorrelation;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
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
@Push
@PreserveOnRefresh
public class WebGUI extends UI {

    @Override
    public void init(VaadinRequest request) {
        Server server = ContextListener.server;
        MessageListener listener = new MessageListener(this);
        LocalConnectionProxy lcp = null;
        try {
            lcp = new LocalConnectionProxy(server, listener);
        } catch (ConnectException e) {
            e.printStackTrace();
        }
        getSession().setAttribute("lcp", lcp);
        getSession().setAttribute("listener", listener);

        new Navigator(this, this);
//        LoginView loginView = new LoginView();
//        LoginPresenter loginPresenter = new LoginPresenter(loginView, lcp);
        
//        LobbyView lobbyView = new LobbyView();
//        LobbyPresenter lobbyPresenter = new LobbyPresenter(lobbyView);
        
//        GameView gameView = new GameView();
//        GamePresenter gamePresenter = new GamePresenter(gameView);
        MainView mv = new MainView();
        new MainPresenter(mv, lcp);
        getNavigator().addView(MainView.NAME, mv);

        getNavigator().addView(LoginView.NAME, LoginView.class);

        CreateGameView cgv = new CreateGameView();
        new CreateGamePresenter(cgv, lcp);
        getNavigator().addView(CreateGameView.NAME, cgv);

        GameView gv = new GameView();
        listener.setGamePresenter(new GamePresenter(gv, lcp));
        getNavigator().addView(GameView.NAME, gv);

        LobbyView lv = new LobbyView();
        listener.setLobbyPresenter(new LobbyPresenter(lv, lcp));
        getNavigator().addView(LobbyView.NAME, lv);

        ScoreView sv = new ScoreView();
        listener.setScorePresenter(new ScorePresenter(sv));
        getNavigator().addView(ScoreView.NAME, sv);
        
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = viewChangeEvent.getNewView() instanceof LoginView;

                if(!isLoggedIn && !isLoginView) {
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;
                } else if (isLoggedIn && isLoginView) {
                    return false;
                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {

            }
        });
    }
}

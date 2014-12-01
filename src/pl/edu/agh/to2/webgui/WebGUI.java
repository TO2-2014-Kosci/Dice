package pl.edu.agh.to2.webgui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.server.VaadinRequest;
import pl.edu.agh.to2.webgui.presenter.LobbyPresenter;
import pl.edu.agh.to2.webgui.view.LobbyView;
import pl.edu.agh.to2.webgui.view.LoginView;

/**
 * Created by Maciej on 2014-11-28.
 */
public class WebGUI extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(new Label("Hello world!"));
        LobbyView lobbyView = new LobbyView();
       new LobbyPresenter(lobbyView);
       new Navigator(this, this);

       getNavigator().addView(lobbyView.NAME, lobbyView);

        /*
        getNavigator().addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {

                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = viewChangeEvent.getNewView() instanceof LoginView;

                if(!isLoggedIn && !isLoginView) {

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
        */
    }
}

package pl.edu.agh.to2.webgui;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.server.VaadinRequest;
import pl.edu.agh.to2.webgui.presenter.MainPresenter;
import pl.edu.agh.to2.webgui.view.LoginView;
import pl.edu.agh.to2.webgui.view.MainView;
import pl.edu.agh.to2.webgui.presenter.LoginPresenter;

/**
 * Created by Maciej on 2014-11-28.
 */
public class WebGUI extends UI {
    @Override
    public void init(VaadinRequest request) {
//        VerticalLayout layout = new VerticalLayout();
//        setContent(layout);
//        layout.addComponent(new Label("Hello world!"));

        new Navigator(this, this);
        LoginView loginView = new LoginView();
        LoginPresenter loginPresenter = new LoginPresenter(loginView);

//        MainView mainView = new MainView();
//        MainPresenter mainPresenter = new MainPresenter(mainView);

//        getNavigator().addView(MainView.NAME, mainView);
        getNavigator().addView(MainView.NAME, MainView.class);
        getNavigator().addView(LoginView.NAME, loginView);

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

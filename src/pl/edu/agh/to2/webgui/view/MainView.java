package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by Maciej on 2014-12-01.
 */
public class MainView extends VerticalLayout
        implements ILoginView, View, Button.ClickListener  {
    public static final String NAME = "";

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {

    }

    @Override
    public void addListener(LoginViewListener listener) {

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}

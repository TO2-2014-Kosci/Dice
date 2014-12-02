package pl.edu.agh.to2.webgui.view;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-11-28.
 */
public class LoginView extends VerticalLayout
    implements ILoginView, View, Button.ClickListener {

    public static final String NAME = "login";
    private final TextField username = new TextField("username");

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());
        return loginPanel;
    }

    private Component buildLabels() {
        CssLayout labels = new CssLayout();

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        labels.addComponent(welcome);

        return labels;
    }

    private Component buildFields() {
        HorizontalLayout fields =  new HorizontalLayout();
        fields.setSpacing(true);

        username.setIcon(FontAwesome.USER);
        username.setStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.focus();

        final Button signin = new Button("Sign In", this);
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        fields.addComponents(username, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        return fields;
    }

    List<LoginViewListener> listeners = new ArrayList<LoginViewListener>();

    @Override
    public void addListener(LoginViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (LoginViewListener listener : listeners) {
            listener.buttonClick(username.getValue());
        }
    }
}

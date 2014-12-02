package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import pl.edu.agh.to2.webgui.presenter.CreateGamePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGameView extends CustomComponent
        implements ICreateGameView, View, Button.ClickListener, MenuBar.Command {
    public static final String NAME = "create";
    public static final String CANCEL_TEXT = "List of games";
    public static final String LOGOUT_TEXT = "Logout";
    public static final String CREATE_TEXT = "Create game";

    private MenuBar menu = new MenuBar();

    public CreateGameView() {
        CreateGamePresenter presenter = new CreateGamePresenter(this);
        setSizeFull();
        GridLayout gridLayout = new GridLayout(1, 4);
        gridLayout.setWidth("100%");


        gridLayout.addComponent(buildMenu(), 0, 0);
        gridLayout.addComponent(buildGameForm(), 0, 1);
        setCompositionRoot(gridLayout);
    }

    private Component buildMenu() {
        menu.setWidth("100%");
        MenuBar.MenuItem cancel = menu.addItem(CANCEL_TEXT, FontAwesome.LIST, this);

        return menu;
    }

    private Component buildGameForm() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);

        final ComboBox gameType = new ComboBox("Game type");
        gameType.addItems("N*", "N+", "Poker");

        TextField playersNumber = new TextField("Number of players");
        TextField botsNumber = new TextField("Number of bots");
        final ComboBox level = new ComboBox("Game level");
        level.addItems("Easy", "Hard");

        final Button createGame = new Button(CREATE_TEXT, this);
        final Button cancel = new Button(CANCEL_TEXT, this);

        fields.addComponents(gameType, playersNumber, botsNumber, level, createGame, cancel);
        return fields;
    }

    List<CreateGameViewListener> listeners = new ArrayList<CreateGameViewListener>();

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for (CreateGameViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
    }

    @Override
    public void menuSelected(MenuBar.MenuItem menuItem) {
        for (CreateGameViewListener listener : listeners) {
            listener.menuSelected(menuItem.getText());
        }
    }

    @Override
    public void addListener(CreateGameViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        MenuBar.MenuItem currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), FontAwesome.USER, null);
        MenuBar.MenuItem logout = currentUser.addItem(LOGOUT_TEXT, FontAwesome.SIGN_OUT, this);

    }


}

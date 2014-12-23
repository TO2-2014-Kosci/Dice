package pl.edu.agh.to2.webgui.view;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import pl.edu.agh.to2.webgui.presenter.CreateGamePresenter;
import to2.dice.game.BotLevel;
import to2.dice.game.GameType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TextField gameName;
    private ComboBox gameType;
    private TextField diceNumber;
    private TextField playersNumber;
    private TextField timeForMove;
    private TextField maxInactiveTurns;
    private TextField roundsToWin;
    private TextField easyBots;
    private TextField hardBots;


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

        gameName = new TextField("Game Name");
        gameType = new ComboBox("Game type");
        gameType.addItems(GameType.values());
        diceNumber = new TextField("Number of dices");
        playersNumber = new TextField("Number of human players");
        timeForMove = new TextField("Time for move [s]");
        maxInactiveTurns = new TextField("Max inactive turns");
        roundsToWin = new TextField("Rounds to win");
        easyBots = new TextField("Number of easy bots");
        hardBots = new TextField("Number of hard bots");
        setValidation();
        final Button createGame = new Button(CREATE_TEXT, this);
        final Button cancel = new Button(CANCEL_TEXT, this);

        fields.addComponents(gameName, gameType, playersNumber, timeForMove, maxInactiveTurns, roundsToWin, easyBots, hardBots, createGame, cancel);
        return fields;
    }

    private void setValidation() {
        RegexpValidator num = new RegexpValidator("\\d+", "This field should contain only numbers");
        gameName.setRequired(true);
        gameType.setRequired(true);
//        diceNumber.addValidator(num);
//        diceNumber.setRequired(true);
        playersNumber.addValidator(num);
        playersNumber.setRequired(true);
        timeForMove.addValidator(num);
        timeForMove.setRequired(true);
        maxInactiveTurns.addValidator(num);
        maxInactiveTurns.setRequired(true);
        roundsToWin.addValidator(num);
        roundsToWin.setRequired(true);
        easyBots.addValidator(num);
        easyBots.setRequired(true);
        hardBots.addValidator(num);
        hardBots.setRequired(true);
    }

    @Override
    public void showNotification(String message) {
        Notification notification = new Notification(message, Notification.TYPE_ERROR_MESSAGE);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
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

    public String getGameName() {
        return gameName.getValue();
    }

    public GameType getGameType() {
        return (GameType) gameType.getValue();
    }

    public int getDiceNumber() {
//        return Integer.parseInt(diceNumber.getValue());
        return 5; //TODO ilosc kosci
    }

    public int getPlayersNumber() {
        return Integer.parseInt(playersNumber.getValue());
    }

    public int getTimeForMove() {
        return Integer.parseInt(timeForMove.getValue());
    }

    public int getMaxInactiveTurns() {
        return Integer.parseInt(maxInactiveTurns.getValue());
    }

    public int getRoundsToWin() {
        return Integer.parseInt(roundsToWin.getValue());
    }

    public Map<BotLevel,Integer> getBots() {
        Map<BotLevel, Integer> bots = new HashMap<BotLevel, Integer>();
        bots.put(BotLevel.EASY, Integer.parseInt(easyBots.getValue()));
        bots.put(BotLevel.HARD, Integer.parseInt(hardBots.getValue()));
        return bots;
    }
}

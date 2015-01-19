package pl.edu.agh.to2.webgui.view;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.edu.agh.to2.webgui.presenter.CreateGamePresenter;
import to2.dice.game.BotLevel;
import to2.dice.game.GameType;

import java.util.*;

/**
 * Created by Maciej on 2014-12-02.
 */
public class CreateGameView extends CustomComponent
        implements ICreateGameView, View, Button.ClickListener, MenuBar.Command {
    public static final String NAME = "create";
    public static final String CANCEL_TEXT = "List of games";
    public static final String LOGOUT_TEXT = "Logout";
    public static final String CREATE_TEXT = "Create game";
    public static final String RANDOM_TEXT = "Random game";

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
    private MenuBar.MenuItem currentUser;

    public CreateGameView() {
        setSizeFull();
//        setHeightUndefined();
        setStyleName("create-background");

        VerticalLayout panel = new VerticalLayout();
        panel.setHeightUndefined();
        panel.addComponent(buildMenu());
        menu.setHeightUndefined();
        panel.setComponentAlignment(menu, Alignment.TOP_CENTER);

        Component gameForm = buildGameForm();
        panel.addComponent(gameForm);
        panel.setComponentAlignment(gameForm, Alignment.MIDDLE_CENTER);

        setCompositionRoot(panel);
    }

    private Component buildMenu() {
        menu.setWidth("100%");
        menu.addItem(CANCEL_TEXT, FontAwesome.LIST, this);
        menu.addItem(RANDOM_TEXT, FontAwesome.RANDOM, this);

        return menu;
    }

    private Component buildGameForm() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);

        Label header = new Label("Create new game");
        header.setStyleName("huge bold");
        fields.addComponent(header);
        fields.setComponentAlignment(header, Alignment.TOP_CENTER);

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

//        HorizontalLayout buttons = new HorizontalLayout();
//        buttons.setSpacing(true);
        final Button createGame = new Button(CREATE_TEXT, this);
        createGame.setStyleName(ValoTheme.BUTTON_PRIMARY);
        final Button cancel = new Button(CANCEL_TEXT, this);
//        buttons.addComponents(createGame, cancel);

        fields.addComponents(gameName, gameType, playersNumber, timeForMove, maxInactiveTurns, roundsToWin, easyBots, hardBots, createGame, cancel);
        fields.setMargin(true);
        return fields;
    }

    private void setValidation() {
        RegexpValidator num = new RegexpValidator("\\d+", true, "This field should contain only numbers");
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
    public void showNotification(String message, String style) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setStyleName(style);
        notification.setDelayMsec(1000);
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
        if (currentUser == null) {
            currentUser = menu.addItem(String.valueOf(getSession().getAttribute("user")), FontAwesome.USER, null);
            MenuBar.MenuItem logout = currentUser.addItem(LOGOUT_TEXT, FontAwesome.SIGN_OUT, this);
        }
        else {
            currentUser.setText((String)getSession().getAttribute("user"));
        }
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

    public void setRandom() {
        Random random = new Random();
        gameName.setValue("Game" + System.currentTimeMillis());
        gameType.setValue(GameType.values()[random.nextInt(3)]);
        playersNumber.setValue(((Integer) random.nextInt(5)).toString());
        timeForMove.setValue(((Integer)(random.nextInt(1000) + 10)).toString());
        maxInactiveTurns.setValue(((Integer)(random.nextInt(7)+1)).toString());
        roundsToWin.setValue(((Integer)(random.nextInt(9) + 1)).toString());
        easyBots.setValue(((Integer)(random.nextInt(9) + 1)).toString());
        hardBots.setValue(((Integer)(random.nextInt(9) + 1)).toString());
    }
}

package pl.edu.agh.to2.webgui.view;

import com.google.gwt.layout.client.*;
import com.google.gwt.layout.client.Layout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import pl.edu.agh.to2.webgui.presenter.GamePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 01.12.14.
 */
public class GameView extends VerticalLayout
        implements IGameView, View,Button.ClickListener {

    public static final String NAME = "game";
    public static final String LEAVE_TEXT = "Leave game";
    public static final String REROLL_TEXT = "Reroll";

    List<GameViewListener> listeners = new ArrayList<GameViewListener>();
    Table players = new Table();
    Panel generalPanel = new Panel();
    Panel dicesPanel = new Panel("Your dices");
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private Button reroll;
    private Button leave;
    private Label header;
    private Label info = new Label();
    private Label roundInfo = new Label();

    public GameView() {
//        new GamePresenter(this);
        prepareView();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for(GameViewListener gameViewListener : listeners){
            gameViewListener.buttonClick(clickEvent.getButton().getCaption());
        }
    }

    @Override
    public void addListener(GameViewListener listener) {
        listeners.add(listener);
    }

    private void prepareView() {

        createDicesPanel();
        populateTable();
        header = new Label("Poker game");
        header.addStyleName("h1 align-center");
        addAndSetComponent(this, header, Alignment.TOP_CENTER);
        generalPanel.setContent(createGeneralLayout());
        addAndSetComponent(this, generalPanel, Alignment.MIDDLE_CENTER);

    }

    private void addAndSetComponent(AbstractOrderedLayout layout, Component component, Alignment alignment) {
        layout.addComponent(component);
        layout.setComponentAlignment(component, alignment);
    }

    private void createDicesPanel() {

        HorizontalLayout dicesPanelLayout = new HorizontalLayout();

//      prepare dicesPanel layout

        checkBoxes.add(new CheckBox("10"));
        checkBoxes.add(new CheckBox("20"));
        checkBoxes.add(new CheckBox("30"));
        checkBoxes.add(new CheckBox("40"));
        checkBoxes.add(new CheckBox("50"));

        for (CheckBox cb : checkBoxes) {
            addAndSetComponent(dicesPanelLayout, cb, Alignment.TOP_CENTER);
        }

        dicesPanelLayout.setMargin(true);
        dicesPanelLayout.setSpacing(true);

//      set layout

        dicesPanel.setContent(dicesPanelLayout);
        dicesPanel.setWidth("285");
        dicesPanel.setVisible(false);
    }

    private VerticalLayout createGeneralLayout() {

        VerticalLayout generalPanelLayout = new VerticalLayout();

        roundInfo.addStyleName("h2 align-center");
        addAndSetComponent(generalPanelLayout, roundInfo, Alignment.TOP_CENTER);

        info.addStyleName("h2 align-center");
        addAndSetComponent(generalPanelLayout, info, Alignment.TOP_CENTER);

        addAndSetComponent(generalPanelLayout, players, Alignment.TOP_CENTER);
        addAndSetComponent(generalPanelLayout, dicesPanel, Alignment.MIDDLE_CENTER);

        reroll = new Button(REROLL_TEXT, this);
        reroll.addStyleName(ValoTheme.BUTTON_PRIMARY);
        reroll.setEnabled(false);
        reroll.setVisible(false);
        addAndSetComponent(generalPanelLayout, reroll, Alignment.BOTTOM_CENTER);

        leave = new Button(LEAVE_TEXT, this);
        leave.addStyleName(ValoTheme.BUTTON_SMALL);
        addAndSetComponent(generalPanelLayout, leave, Alignment.BOTTOM_LEFT);

        generalPanelLayout.setMargin(true);
        generalPanelLayout.setSpacing(true);

        return generalPanelLayout;
    }

    private void populateTable(){
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.addContainerProperty("Dices", String.class, null);
        players.setColumnWidth("Dices", 100);

    }

    @Override
    public void showNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
    }

    public boolean[] getDices() {
        boolean[] dices = new boolean[5]; //TODO ustawic jakis ludzki rozmiar
        for(int i = 0; i < 5; i++) {
            dices[i] = checkBoxes.get(i).getValue();
        }
        return dices;
    }

    public void updatePlayersList(List<Object[]> updatedPlayersList) {
        players.removeAllItems();
        for (Object[] updatedPlayer : updatedPlayersList) {
            players.addItem(updatedPlayer, null);
        }
        if (players.size() > 10) {
            players.setPageLength(10);
        } else {
            players.setPageLength(players.size());
        }

    }

    public void setDices(int[] updatedDices) {
        for (int i = 0; i < 5; i++) {
            checkBoxes.get(i).setCaption(Integer.toString(updatedDices[i]));
        }
    }

    public void setHeader(String message) {
        header.setValue(message);
    }
    public void setInfo (String message) {
        info.setValue(message);
    }

    public void setRoundInfo(String message) {
        roundInfo.setValue(message);
    }

    public void enablePlayerUI(boolean enable) {
        reroll.setVisible(enable);
        dicesPanel.setVisible(enable);
    }
    public void enableReroll(boolean enabled) {
        reroll.setEnabled(enabled);
    }
}

package pl.edu.agh.to2.webgui.view;

import com.google.gwt.layout.client.*;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.Timer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.time.StopWatch;
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
    public static final String STAND_UP_TEXT = "Stand up";

    List<GameViewListener> listeners = new ArrayList<GameViewListener>();
    Table players = new Table();
    Panel dicesPanel = new Panel("Your dices");
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private Button standUp;
    private Button reroll;
    private Label header;
    private Label info = new Label();
    private Label roundInfo = new Label();
    private Label countDown = new Label("Time left: ");
    private ProgressBar progressBar = new ProgressBar(0.0f);

    public GameView() {
        setSizeFull();

        createDicesPanel();
        populateTable();

        addAndSetComponent(this, createGeneralLayout(), Alignment.MIDDLE_CENTER);
        addStyleName("game-background");

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
        generalPanelLayout.setSizeFull();

        addAndSetComponent(generalPanelLayout, createHeaders(), Alignment.TOP_CENTER);

        players.setSizeFull();
        addAndSetComponent(generalPanelLayout, players, Alignment.TOP_CENTER);
        addAndSetComponent(generalPanelLayout, dicesPanel, Alignment.MIDDLE_CENTER);

        reroll = new Button(REROLL_TEXT, this);
        reroll.addStyleName(ValoTheme.BUTTON_PRIMARY);
        reroll.setEnabled(false);
        reroll.setVisible(false);
        addAndSetComponent(generalPanelLayout, reroll, Alignment.BOTTOM_CENTER);

        HorizontalLayout buttons = new HorizontalLayout();
        standUp = new Button(STAND_UP_TEXT, this);
        standUp.addStyleName(ValoTheme.BUTTON_SMALL);
        standUp.setVisible(false);
        Button leave = new Button(LEAVE_TEXT, this);
        leave.setStyleName(ValoTheme.BUTTON_SMALL);
        buttons.addComponent(standUp);
        buttons.addComponent(leave);
        buttons.setSpacing(true);

        addAndSetComponent(generalPanelLayout, buttons, Alignment.BOTTOM_CENTER);

        generalPanelLayout.setMargin(true);
        generalPanelLayout.setSpacing(true);
        generalPanelLayout.setSizeUndefined();

        return generalPanelLayout;
    }

    private VerticalLayout createHeaders() {
        VerticalLayout headersVerticalLayout = new VerticalLayout();
        headersVerticalLayout.setSizeFull();
        headersVerticalLayout.setMargin(true);
        headersVerticalLayout.setStyleName("well");

        header = new Label("Poker game");
        header.addStyleName("h1 bold align-center");
        addAndSetComponent(headersVerticalLayout, header, Alignment.TOP_CENTER);

        roundInfo.addStyleName("huge align-center");
        addAndSetComponent(headersVerticalLayout, roundInfo, Alignment.TOP_CENTER);

        info.addStyleName("huge align-center padding ");
        addAndSetComponent(headersVerticalLayout, info, Alignment.TOP_CENTER);

//      TODO ProgressBar debug - delete in final version
//        countDown.addStyleName("align-center");
//        addAndSetComponent(generalPanelLayout, countDown, Alignment.TOP_CENTER);

        addAndSetComponent(headersVerticalLayout, progressBar, Alignment.TOP_CENTER);

        return headersVerticalLayout;
    }

    private void populateTable(){
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.addContainerProperty("Dices", String.class, null);
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


    @Override
    public void showNotification(String message, String style, Position position) {
        Notification notification = new Notification(message);
        notification.setPosition(position);
        notification.setStyleName(style);
        notification.setDelayMsec(500);
        notification.show(Page.getCurrent());
    }

    public boolean[] getDices() {
        boolean[] dices = new boolean[5]; //TODO ustawic jakis ludzki rozmiar
        for(int i = 0; i < 5; i++) {
            dices[i] = checkBoxes.get(i).getValue();
            checkBoxes.get(i).setValue(false);
        }
        return dices;
    }

    public void updatePlayersList(List<Object[]> updatedPlayersList) {
        players.removeAllItems();
        for (Object[] updatedPlayer : updatedPlayersList) {
            players.addItem(updatedPlayer, null);
        }
        if (players.size() > 8) {
            players.setPageLength(8);
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
        standUp.setVisible(enable);
    }
    public void enableReroll(boolean enabled) {
        reroll.setEnabled(enabled);
    }

    public void updateCountDown(String message) {
        countDown.setValue(message);

    }

    public void updateProgressBar(float progress) {
        progressBar.setValue(progress);
    }

    public void resetProgressBar() {
        progressBar.setValue(0.0f);
    }


}

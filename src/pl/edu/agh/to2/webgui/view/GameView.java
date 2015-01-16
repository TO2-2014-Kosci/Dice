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
    Panel centralPanel = new Panel();
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private Button reroll;
    private Button leave;
    private Label header;
    private Label info = new Label();

    public GameView() {
//        new GamePresenter(this);
        header = new Label("Poker game");
        header.addStyleName("h1 align-center");

        addComponent(header);
        setComponentAlignment(header, Alignment.TOP_CENTER);
        preparePanel();
        addComponent(generalPanel);
        setComponentAlignment(generalPanel, Alignment.MIDDLE_CENTER);
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

    private void preparePanel(){
        populateTable();
        VerticalLayout generalPanelLayout = new VerticalLayout();
        HorizontalLayout dicesPanelLayout = new HorizontalLayout();
        VerticalLayout centralPanelLayout = new VerticalLayout();
        checkBoxes.add(new CheckBox("10"));
        checkBoxes.add(new CheckBox("20"));
        checkBoxes.add(new CheckBox("30"));
        checkBoxes.add(new CheckBox("40"));
        checkBoxes.add(new CheckBox("50"));
        for(CheckBox cb : checkBoxes) {
            dicesPanelLayout.addComponent(cb);
            dicesPanelLayout.setComponentAlignment(cb, Alignment.TOP_CENTER);
        }
        info.addStyleName("h2 align-center");
        //generalPanelLayout.setWidth("100%");
        generalPanelLayout.addComponent(info);
        generalPanelLayout.setComponentAlignment(info, Alignment.TOP_CENTER);
        generalPanelLayout.addComponent(players);
        generalPanelLayout.addComponent(dicesPanel);
        dicesPanel.setWidth("285");
        generalPanelLayout.setComponentAlignment(players, Alignment.TOP_CENTER);
        generalPanelLayout.setComponentAlignment(dicesPanel, Alignment.MIDDLE_CENTER);
        reroll = new Button(REROLL_TEXT, this);
        reroll.addStyleName(ValoTheme.BUTTON_PRIMARY);
        reroll.setEnabled(false);
        generalPanelLayout.addComponent(reroll);
        generalPanelLayout.setComponentAlignment(reroll, Alignment.BOTTOM_CENTER);
        generalPanelLayout.setMargin(true);
        generalPanelLayout.setSpacing(true);
        dicesPanel.setContent(dicesPanelLayout);
        dicesPanelLayout.setMargin(true);
        dicesPanelLayout.setSpacing(true);
        leave = new Button(LEAVE_TEXT, this);
        leave.addStyleName(ValoTheme.BUTTON_SMALL);
        generalPanelLayout.addComponent(leave);
        generalPanelLayout.setComponentAlignment(leave, Alignment.BOTTOM_LEFT);

        generalPanel.setContent(generalPanelLayout);
    }
    private void populateTable(){
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.addContainerProperty("Dices", String.class, null);
        players.setColumnWidth("Dices", 100);
        players.setPageLength(players.size());
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

    public void enableReroll(boolean enabled) {
        reroll.setEnabled(enabled);
    }
}

package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 01.12.14.
 */
public class GameView extends CustomComponent
        implements IGameView, View,Button.ClickListener {

    public static final String NAME = "game";
    public static final String LEAVE_TEXT = "Leave game";
    public static final String REROLL_TEXT = "Reroll";

    List<GameViewListener> listeners = new ArrayList<GameViewListener>();
    Table players = new Table();
    Panel generalPanel = new Panel();
    Panel dicesPanel = new Panel("Your dices");
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();

    public GameView() {
        setSizeFull();
        preparePanel();
        setCompositionRoot(generalPanel);
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
        Label time = new Label("Time left : 40 seconds");
        time.addStyleName("h2");
        populateTable();
        GridLayout generalPanelLayout = new GridLayout(9,9);
        HorizontalLayout dicesPanelLayout = new HorizontalLayout();
        checkBoxes.add(new CheckBox("10"));
        checkBoxes.add(new CheckBox("20"));
        checkBoxes.add(new CheckBox("30"));
        checkBoxes.add(new CheckBox("40"));
        checkBoxes.add(new CheckBox("50"));
        for(CheckBox cb : checkBoxes) {
            dicesPanelLayout.addComponent(cb);
        }
        generalPanelLayout.setWidth("100%");
        generalPanelLayout.addComponent(time, 4, 0);
        generalPanelLayout.addComponent(players,0,1);
        generalPanelLayout.addComponent(
            new Button(LEAVE_TEXT,this),0,4
        );
        generalPanelLayout.addComponent(new Button(REROLL_TEXT, this),4,3);
        dicesPanel.setContent(dicesPanelLayout);
        generalPanelLayout.addComponent(dicesPanel,4,2);
        generalPanel.setContent(generalPanelLayout);
    }
    private void populateTable(){
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.addContainerProperty("Dices", String.class, null);
        players.setPageLength(players.size());
        players.addItem(new Object[]{"X",1, "3,3,3,4,5"}, 1);
        players.addItem(new Object[]{"Y",2, "1,1,5,3,2"},2);
        players.addItem(new Object[]{"Z", 0,"1,1,5,3,2"}, 3);
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
        for (int i = 0; i < updatedDices.length; i++) {
            checkBoxes.get(i).setCaption(Integer.toString(updatedDices[i]));
        }
    }
}

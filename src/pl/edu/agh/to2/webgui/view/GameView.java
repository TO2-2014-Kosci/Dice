package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukasz on 01.12.14.
 */
public class GameView extends CustomComponent
        implements IGameView, View,Button.ClickListener {

    public static final String NAME = "game";
    List<GameViewListener> listeners = new ArrayList<GameViewListener>();
    Table players = new Table();
    Panel generalPanel = new Panel();
    Panel dicesPanel = new Panel("Your dices");

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
       dicesPanelLayout.addComponent(new CheckBox("1"));
       dicesPanelLayout.addComponent(new CheckBox("2"));
       dicesPanelLayout.addComponent(new CheckBox("2"));
       dicesPanelLayout.addComponent(new CheckBox("5"));
       dicesPanelLayout.addComponent(new CheckBox("3"));
       generalPanelLayout.setWidth("100%");
       generalPanelLayout.addComponent(time, 4, 0);
       generalPanelLayout.addComponent(players,0,1);
       generalPanelLayout.addComponent(
               new Button("Leave Game",this),0,4
       );
       generalPanelLayout.addComponent(new Button("Reroll",this),4,3);
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
}

package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import pl.edu.agh.to2.webgui.presenter.ScorePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciej on 2014-12-23.
 */
public class ScoreView extends VerticalLayout
    implements  IScoreView, View, Button.ClickListener {

    public static final String NAME = "score";
    public static final String EXIT_TEXT = "Exit";

    List<ScoreViewListener> listeners = new ArrayList<ScoreViewListener>();
    Table players = new Table();

    public ScoreView() {
        new ScorePresenter(this);
        setSizeFull();

        Component layout = buildScoreTable();
        addComponent(layout);
        setComponentAlignment(layout, Alignment.MIDDLE_CENTER);

    }

    private Component buildScoreTable() {
        GridLayout layout = new GridLayout(1, 2);
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.setPageLength(players.size());
        players.setColumnWidth("Player", 100);
        players.setColumnWidth("Score", 50);
        players.addItem(new Object[]{"Y",2},1);
        layout.addComponent(players, 0, 0);

        final Button exit = new Button(EXIT_TEXT, this);
        layout.addComponent(exit, 0, 1);

        return layout;
    }

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        for(ScoreViewListener listener : listeners) {
            listener.buttonClick(clickEvent.getButton().getCaption());
        }
    }

    @Override
    public void addListener(ScoreViewListener listener) {
        listeners.add(listener);
    }

    @Override
    public void showNotification(String message) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.show(Page.getCurrent());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public void updatePlayersList(List<Object[]> updatedPlayersList) {
        players.removeAllItems();
        for (Object[] updatedPlayer : updatedPlayersList) {
            players.addItem(updatedPlayer, null);
        }
        players.sort(new Object[] {"Score"}, new boolean[]{false});
    }
}

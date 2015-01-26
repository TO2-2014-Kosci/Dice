package pl.edu.agh.to2.webgui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
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
        setSizeFull();

        setSpacing(true);
        setStyleName("score-background");

        Component scoreTable = buildScoreTable();
        addComponent(scoreTable);
        setComponentAlignment(scoreTable, Alignment.MIDDLE_CENTER);
    }

    private Component buildScoreTable() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeUndefined();
        layout.addStyleName("well");
        layout.setSpacing(true);
        layout.setMargin(true);

        //build header
        Label header = new Label("Results");
        header.setStyleName("huge bold");
        header.setSizeUndefined();
        layout.addComponent(header);
        layout.setComponentAlignment(header, Alignment.TOP_CENTER);

        //build table
        players.setSelectable(false);
        players.setSortEnabled(false);
        players.setImmediate(true);
        players.addContainerProperty("Player", String.class, null);
        players.addContainerProperty("Score", Integer.class, null);
        players.setColumnWidth("Score", 80);
        players.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        layout.addComponent(players);
        layout.setComponentAlignment(players, Alignment.MIDDLE_CENTER);

        //build button
        final Button exit = new Button(EXIT_TEXT, this);
        exit.setStyleName(ValoTheme.BUTTON_PRIMARY);
        exit.setSizeUndefined();
        layout.addComponent(exit);
        layout.setComponentAlignment(exit, Alignment.BOTTOM_CENTER);

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
    public void showNotification(String message, String style) {
        Notification notification = new Notification(message);
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setStyleName(style);
        notification.setDelayMsec(1000);
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
        if(players.size() > 10) players.setPageLength(10);
        else players.setPageLength(players.size());
    }
}

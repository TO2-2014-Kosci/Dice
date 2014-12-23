package pl.edu.agh.to2.webgui.view;

/**
 * Created by Maciej on 2014-12-23.
 */
public interface IScoreView {
    interface ScoreViewListener {
        void buttonClick(String operation);
    }

    public void addListener(ScoreViewListener listener);
    public void showNotification(String message);
}

package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.network.messages.game.PlayZoneUpdate;
import ee.taltech.gandalf.screens.GameScreen;
import ee.taltech.gandalf.screens.ScreenController;

public class PlayZoneListener extends Listener {
    private ScreenController screenController;
    /**
     * Construct PlayZoneListener.
     *
     * @param screenController game screen controller
     */
    public PlayZoneListener(ScreenController screenController) {
        this.screenController = screenController;
    }

    public void received(Connection connection, Object incomingData) {
        GameScreen gameScreen = screenController.getGameScreen();
        switch (incomingData) {
            case PlayZoneUpdate playZoneUpdate: // spellPosition message
                gameScreen.startedGame.getPlayZone().shrinkPlayZone(playZoneUpdate.radius);
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

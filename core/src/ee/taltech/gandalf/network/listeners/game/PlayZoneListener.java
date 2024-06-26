package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.components.PlayZone;
import ee.taltech.gandalf.network.messages.game.PlayZoneCoordinates;
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

    /**
     * Received messages from server.
     *
     * @param connection server connection
     * @param incomingData message from server
     */
    @Override
    public void received(Connection connection, Object incomingData) {
        GameScreen gameScreen = screenController.getGameScreen();
        switch (incomingData) {
            case PlayZoneCoordinates playZoneCoordinates:
                System.out.println("Coordinates received");
                gameScreen.startedGame.initializePlayZone(playZoneCoordinates.firstPlayZoneX,
                        playZoneCoordinates.firstPlayZoneY, playZoneCoordinates.secondPlayZoneX,
                        playZoneCoordinates.secondPlayZoneY, playZoneCoordinates.thirdPlayZoneX,
                        playZoneCoordinates.thirdPlayZoneY);
                break;
            case PlayZoneUpdate playZoneUpdate: // playZoneUpdate message
                PlayZone playZone = gameScreen.startedGame.getPlayZone();

                gameScreen.setCurrentTime(playZoneUpdate.timer);
                if (playZone != null && playZone.getStage() != playZoneUpdate.stage) {
                    String message = playZone.updateZone(playZoneUpdate.stage);
                    gameScreen.displayMessageOnScreen(message);
                }
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

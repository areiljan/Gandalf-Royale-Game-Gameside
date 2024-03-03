package ee.taltech.network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.Position;
import ee.taltech.screen.ScreenController;
import ee.taltech.screen.screens.GameScreen;

public class PlayerPositionListener extends Listener {

    ScreenController screenController;

    /**
     * Construct PlayerPositionListener.
     *
     * @param screenController game screen controller
     */
    public PlayerPositionListener(ScreenController screenController) {
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
        GameScreen gameScreen;
        gameScreen = screenController.getGameScreen();
        switch (incomingData){
            case Position position: // Position message
                if (position.userID == connection.getID()) {
                    gameScreen.startedGame.checkOverwritePlayerPosition(position);
                } else {
                    gameScreen.startedGame.movePlayer(position);
                }
                break;
            default: // If something else comes through
                break;
        }
    }
}



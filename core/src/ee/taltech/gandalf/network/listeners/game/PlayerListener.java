package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.network.messages.game.ActionTaken;
import ee.taltech.gandalf.network.messages.game.KilledPlayer;
import ee.taltech.gandalf.network.messages.game.Position;
import ee.taltech.gandalf.screens.GameScreen;
import ee.taltech.gandalf.screens.ScreenController;

public class PlayerListener extends Listener {

    ScreenController screenController;

    /**
     * Construct PlayerListener.
     *
     * @param screenController game screen controller
     */
    public PlayerListener(ScreenController screenController) {
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
        switch (incomingData) {
            case Position position: // Position message
                if (position.userID == connection.getID()) {
                    gameScreen.startedGame.checkOverwritePlayerPosition(position);
                } else {
                    gameScreen.startedGame.movePlayer(position);
                }
                break;
            case ActionTaken actionTaken:
                PlayerCharacter player = gameScreen.startedGame.getPlayer(actionTaken.userID);
                if (player.playerID == actionTaken.userID) {
                    player.getPlayerAnimator().updateAction(actionTaken);
                }
                break;
            case KilledPlayer killedPlayer:
                gameScreen.startedGame.killPlayer(killedPlayer.id);
                break;
            default: // If something else comes through
                break;
        }
    }
}



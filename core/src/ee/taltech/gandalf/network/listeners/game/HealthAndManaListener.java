package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.network.messages.game.KilledPlayer;
import ee.taltech.gandalf.network.messages.game.UpdateHealth;
import ee.taltech.gandalf.network.messages.game.UpdateMana;
import ee.taltech.gandalf.screens.ScreenController;
import ee.taltech.gandalf.screens.GameScreen;

public class HealthAndManaListener extends Listener {

    ScreenController screenController;

    /**
     * Construct HealthAndManaListener.
     *
     * @param screenController for accessing game screen
     */
    public HealthAndManaListener(ScreenController screenController) {
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
            case UpdateHealth message: // UpdateHealth message
                gameScreen.startedGame.updatePlayersHealth(message.playerId, message.health);
                break;
            case UpdateMana message: // UpdateMana message
                gameScreen.startedGame.updatePlayersMana(message.playerId, message.mana);
                break;
            case KilledPlayer message: // KilledPlayer message
                gameScreen.startedGame.killPlayer(message.id);

                break;
            default:
                break;
        }
    }
}

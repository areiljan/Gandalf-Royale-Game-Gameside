package ee.taltech.network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.UpdateHealth;
import ee.taltech.network.messages.UpdateMana;
import ee.taltech.screen.ScreenController;
import ee.taltech.screen.screens.GameScreen;

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
                gameScreen.updatePlayersHealth(message.playerId, message.health);
                break;
            case UpdateMana message: // UpdateMana message
                gameScreen.updatePlayersMana(message.playerId, message.mana);
                break;
            default: // Something else
                break;
        }
    }
}

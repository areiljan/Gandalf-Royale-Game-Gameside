package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.components.StartedGame;
import ee.taltech.gandalf.entities.Mob;
import ee.taltech.gandalf.network.messages.game.MobPosition;
import ee.taltech.gandalf.network.messages.game.UpdateMobHealth;
import ee.taltech.gandalf.screens.GameScreen;
import ee.taltech.gandalf.screens.ScreenController;

public class MobListener extends Listener {

    ScreenController screenController;

    /**
     * Construct MobListener.
     *
     * @param screenController for accessing game screen
     */
    public MobListener(ScreenController screenController) {
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
        StartedGame game = gameScreen.startedGame;
        Mob mob;
        switch (incomingData) {
            case MobPosition message: // Mob position message
                if (game.getMobs().containsKey(message.mobId)) { // If this mob is already in the game
                    mob = game.getMobs().get(message.mobId);
                    mob.setPosition(message.xPosition, message.yPosition); // Update position
                } else { // If this mob is not in game
                    mob = new Mob(message.xPosition, message.yPosition, message.mobId); // Create new mob
                    game.addMob(mob); // Add new mob to the game
                }
                break;
            case UpdateMobHealth message: // Mob health update message
                mob = game.getMobs().get(message.mobId); // Get mob by ID

                if (message.health != 0) { // If new heath is not 0
                    mob.setHealth(message.health); // Update health
                } else { // New heath is 0
                    game.removeMob(message.mobId); // Remove mob from the game
                }
                break;
            default: // Something else
                break;
        }
    }
}

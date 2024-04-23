package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.entities.Item;
import ee.taltech.gandalf.network.messages.game.CoinPickedUp;
import ee.taltech.gandalf.network.messages.game.HealingPotionUsed;
import ee.taltech.gandalf.network.messages.game.ItemDropped;
import ee.taltech.gandalf.network.messages.game.ItemPickedUp;
import ee.taltech.gandalf.screens.GameScreen;
import ee.taltech.gandalf.screens.ScreenController;

public class ItemListener extends Listener {

    ScreenController screenController;

    /**
     * Construct ItemListener.
     *
     * @param screenController for accessing game screen
     */
    public ItemListener(ScreenController screenController) {
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
        Item item;
        switch (incomingData) {
            case ItemDropped message: // ItemDropped message
                    // If player id is not null aka player dropped the item and this is clients player
                if (message.playerId != null && message.playerId == connection.getID()) {
                    item = gameScreen.startedGame.getPlayer(message.playerId).removeItem(message.itemId);

                    // Update items position to players current position
                    item.setXPosition(message.xPosition);
                    item.setYPosition(message.yPosition);
                } else { // If player id is null aka game dropped the item
                    item = new Item(message.itemId, message.type, message.xPosition, message.yPosition);
                }

                gameScreen.startedGame.addItem(item); // Add item to world
                break;
            case ItemPickedUp message: // ItemPickedUp message
                // If player id is not null aka player picked item up and this is clients player
                if (message.playerId != null && message.playerId == connection.getID()) {
                    item = gameScreen.startedGame.removeItem(message.itemId);
                    gameScreen.startedGame.getPlayer(message.playerId).pickUpItem(item);
                } else { // If player id is null aka game removed item from the ground
                    gameScreen.startedGame.removeItem(message.itemId);
                }
                break;
            case CoinPickedUp message: // CoinPickedUp message
                gameScreen.startedGame.removeItem(message.coinId);
                gameScreen.startedGame.getPlayer(message.playerId).addCoin();
                break;
            case HealingPotionUsed message:
                gameScreen.startedGame.getPlayer(message.playerId).removeItem(message.itemId);
                break;
            default: // Something else
                break;
        }
    }
}

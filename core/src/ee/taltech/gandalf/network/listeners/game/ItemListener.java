package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.entities.Item;
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
                if (message.playerId != null) { // If player id is not null aka player dropped the item
                    if (message.playerId == connection.getID()) {
                        item = gameScreen.startedGame.getAlivePlayers().get(message.playerId).dropItem(message.itemId);
                    } else {
                        item = new Item(message.itemId, message.type);
                    }
                    System.out.println("ITEM_LISTENER ITEM_DROPPED_MESSAGE itemID: " + message.itemId + " playerID: " + message.playerId + " item: " + item);
                    // Update items position to players current position
                    item.setXPosition(message.xPosition);
                    item.setYPosition(message.yPosition);
                } else { // If player id is null aka game dropped the item
                    item = new Item(message.itemId, message.type, message.xPosition, message.yPosition);
                }

                gameScreen.startedGame.addItem(item); // Add item to world
                break;
            case ItemPickedUp message: // ItemPickedUp message
                if (message.playerId != null) { // If player id is not null aka player picked item up
                    item = gameScreen.startedGame.removeItem(message.itemId);
                    if (message.playerId == connection.getID()) {
                        gameScreen.startedGame.getAlivePlayers().get(message.playerId).pickUpItem(item);
                    }
                    System.out.println("ITEM_LISTENER ITEM_PICKED_UP_MESSAGE itemID: " + message.itemId + " playerID: " + message.playerId + " player's selectedslot " + gameScreen.startedGame.getAlivePlayers().get(message.playerId).getSelectedSlot());
                } else { // If player id is null aka game removed item from the ground
                    gameScreen.startedGame.removeItem(message.itemId);
                }
                break;
            default: // Something else
                break;
        }
    }
}

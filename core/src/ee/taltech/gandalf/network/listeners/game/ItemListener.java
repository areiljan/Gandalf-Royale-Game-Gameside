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
                System.out.println("LISTENER got ItemDropped message, playerId: " + message.playerId);
                if (message.playerId != null) {
                    item = gameScreen.startedGame.getAlivePlayers().get(message.playerId).dropItem(message.itemId);
                } else {
                    item = new Item(message.itemId, message.type, message.xPosition, message.yPosition);
                }

                gameScreen.startedGame.addItem(item);
                break;
            case ItemPickedUp message: // ItemPickedUp message
                if (message.playerId != null) {
                    item = gameScreen.startedGame.removeItem(message.itemId);
                    gameScreen.startedGame.getAlivePlayers().get(message.playerId).pickUpItem(item);
                } else {
                    gameScreen.startedGame.removeItem(message.itemId);
                }
                break;
            default: // Something else
                break;
        }
    }
}

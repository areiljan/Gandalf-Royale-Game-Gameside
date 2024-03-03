package ee.taltech.network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.*;
import ee.taltech.objects.Fireball;
import ee.taltech.screen.ScreenController;
import ee.taltech.screen.screens.GameScreen;
import ee.taltech.screen.screens.LobbyScreen;
import ee.taltech.utilities.Lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireballListener extends Listener {
    ScreenController screenController;

    /**
     * Construct LobbyListener.
     *
     * @param screenController game screen controller
     */
    public FireballListener(ScreenController screenController) {
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
        switch (incomingData){
            case FireballPosition fireball: // fireballPosition message
                gameScreen.startedGame.updateFireballPositions(fireball.senderPlayerID,
                        fireball.xPosition, fireball.yPosition, fireball.id);
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

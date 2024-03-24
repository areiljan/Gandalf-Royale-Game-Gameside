package ee.taltech.gandalf.network.listeners.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.network.messages.game.SpellDispel;
import ee.taltech.gandalf.network.messages.game.SpellPosition;
import ee.taltech.gandalf.screens.ScreenController;
import ee.taltech.gandalf.screens.GameScreen;

public class SpellListener extends Listener {
    ScreenController screenController;

    /**
     * Construct SpellListener.
     *
     * @param screenController game screen controller
     */
    public SpellListener(ScreenController screenController) {
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
            case SpellPosition spellPosition: // spellPosition message
                gameScreen.startedGame.updateSpellPositions(spellPosition.senderPlayerID,
                        spellPosition.xPosition, spellPosition.yPosition, spellPosition.id, spellPosition.type);
                break;
            case SpellDispel spellDispel:
                System.out.println("Hello, SpellDispel.");
                gameScreen.startedGame.removeSpell(spellDispel.id);
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

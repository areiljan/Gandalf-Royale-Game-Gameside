package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.ItemTypes;

public class SpellPosition {
    public int senderPlayerID;
    public double xPosition;
    public double yPosition;
    public int id;
    public ItemTypes type;

    /**
     * Empty constructor for Kryonet.
     */
    public SpellPosition() {
        // Empty constructor for server to fill.
    }
}

package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.ItemTypes;

public class ItemDropped {

    public Integer playerId;
    public Integer itemId;
    public ItemTypes type;
    public float xPosition;
    public float yPosition;

    /**
     * Empty constructor for Kryonet.
     */
    public ItemDropped() {
        // Empty constructor for server to fill.
    }
}

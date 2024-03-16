package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.SpellTypes;

public class ItemPickedUp {

    public Integer playerId;
    public Integer itemId;
    public SpellTypes type;

    /**
     * Empty constructor for Kryonet.
     */
    public ItemPickedUp() {
        // Empty constructor for server to fill.
    }
}

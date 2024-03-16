package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.SpellTypes;

public class ItemPickedUp {
    public Integer playerId;
    public Integer itemId;

    /**
     * Empty constructor for Kryonet.
     */
    public ItemPickedUp(Integer playerId, Integer itemId, SpellTypes type) {
        // Empty constructor for server to fill.
    }
}

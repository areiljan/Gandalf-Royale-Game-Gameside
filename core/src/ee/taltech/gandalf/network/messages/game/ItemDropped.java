package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.SpellTypes;

public class ItemDropped {
    public Integer playerId;
    public Integer itemId;
    public SpellTypes type;
    public Float xPosition;
    public Float yPosition;

    /**
     * Empty constructor for Kryonet.
     */
    public ItemDropped(Integer playerId, Integer itemId, SpellTypes type, Float xPosition, Float yPosition){
        // Empty constructor for server to fill.
    }
}

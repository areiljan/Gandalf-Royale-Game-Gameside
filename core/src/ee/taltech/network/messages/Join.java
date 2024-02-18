package ee.taltech.network.messages;

public class Join {
    public Integer gameId; // Game ID that the player wants to join
    public Integer playerId; // Players ID who wants to join

    /**
     * Empty constructor for Kryonet.
     */
    public Join() {
    }

    /**
     * Construct join message.
     *
     * @param gameId given game ID where player joins
     * @param playerId given player ID who wants to join
     */
    public Join(Integer gameId, Integer playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}

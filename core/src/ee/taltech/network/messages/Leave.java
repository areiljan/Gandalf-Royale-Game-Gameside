package ee.taltech.network.messages;

public class Leave {
    public Integer gameId; // Game ID that the player wants to leave
    public Integer playerId; // Players ID who wants to leave

    /**
     * Empty Constructor for Kryonet.
     */
    public Leave() {
    }

    /**
     * Construct leave message.
     *
     * @param gameId given game ID where player leaves
     * @param playerId given player ID who wants to leaves
     */
    public Leave(Integer gameId, Integer playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}

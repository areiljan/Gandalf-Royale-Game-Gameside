package ee.taltech.gandalf.network.messages.lobby;

public class LobbyDismantle {
    public Integer gameId; // Game ID that will be dismantled

    /**
     * Empty Constructor for Kryonet.
     */
    public LobbyDismantle() {
    }

    /**
     * Construct lobby dismantle message.
     *
     * @param gameId given game ID that will be dismantled
     */
    public LobbyDismantle(Integer gameId) {
        this.gameId = gameId;
    }
}

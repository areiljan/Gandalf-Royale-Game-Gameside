package ee.taltech.network.messages;

public class LobbyCreation {
    public String gameName; // Game name that is displayed in lobby screen
    public Integer gameId; // Game ID
    public Integer hostId; // Lobby creator aka Lobby host ID

    /**
     * Empty Constructor for Kryonet.
     */
    public LobbyCreation() {
    }

    /**
     * Construct lobby creation message.
     *
     * @param gameName given game name by game creator
     * @param hostId game creators ID
     */
    public LobbyCreation(String gameName, Integer hostId) {
        this.gameName = gameName;
        this.hostId = hostId;
    }
}

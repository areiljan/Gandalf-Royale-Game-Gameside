package ee.taltech.gandalf.network.messages.game;

public class GameLeave {
    public Integer playerID;

    /**
     * Empty constructor for kryonet.
     */
    public GameLeave() {
    }

    /**
     * Construct GameLeave message.
     *
     * @param playerID player's ID who leave
     */
    public GameLeave(Integer playerID) {
        this.playerID = playerID;
    }
}

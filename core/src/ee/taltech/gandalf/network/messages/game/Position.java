package ee.taltech.gandalf.network.messages.game;

public class Position {
    public float xPosition;
    public float yPosition;
    public int userID;

    /**
     * Empty Constructor for Kryonet.
     */
    public Position() {
        // Don't remove. This needs to be implemented for kryo register.
    }
}

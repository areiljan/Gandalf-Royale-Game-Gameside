package ee.taltech.network.messages;

public class Position {
    public int xPosition;
    public int yPosition;
    public int userID;

    /**
     * Empty Constructor for Kryonet.
     */
    public Position() {
        // Don't remove. This needs to be implemented for kryo register.
    }
}
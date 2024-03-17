package ee.taltech.gandalf.network.messages.game;
public class ActionTaken {
    public int userID;
    public boolean action;
    public int mouseX;
    public int mouseY;

    /**
     * Construct action message.
     * Used for fluent animation on enemy characters.
     */
    public ActionTaken () {
        // Empty constructor for server to fill.
    }
}
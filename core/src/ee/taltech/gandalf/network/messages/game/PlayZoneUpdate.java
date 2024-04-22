package ee.taltech.gandalf.network.messages.game;

public class PlayZoneUpdate {
    public int timer;
    public int stage;

    /**
     * Update PlayZone Radius.
     */
    public PlayZoneUpdate() {
        // empty constructor for KryoNet.
    }
}
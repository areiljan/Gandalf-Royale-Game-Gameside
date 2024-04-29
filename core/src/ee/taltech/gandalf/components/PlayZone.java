package ee.taltech.gandalf.components;

public class PlayZone {
    private int stage;
    private final int firstPlayZoneX;
    private final int firstPlayZoneY;
    private final int secondPlayZoneX;
    private final int secondPlayZoneY;
    private final int thirdPlayZoneX;
    private final int thirdPlayZoneY;

    /**
     * PlayZone constructor.
     * These coordinates come from the server at the start of each game.
     * @param firstPlayZoneX - center point.
     * @param firstPlayZoneY - center point.
     * @param secondPlayZoneX - center point.
     * @param secondPlayZoneY - center point.
     * @param thirdPlayZoneX - center point.
     * @param thirdPlayZoneY - center point.
     */
    public PlayZone(int firstPlayZoneX, int firstPlayZoneY,
                    int secondPlayZoneX, int secondPlayZoneY,
                    int thirdPlayZoneX, int thirdPlayZoneY) {
        this.stage = 0;
        this.firstPlayZoneX = firstPlayZoneX;
        this.firstPlayZoneY = firstPlayZoneY;
        this.secondPlayZoneX = secondPlayZoneX;
        this.secondPlayZoneY = secondPlayZoneY;
        this.thirdPlayZoneX = thirdPlayZoneX;
        this.thirdPlayZoneY = thirdPlayZoneY;
    }

    /**
     * First playZone x coordinate getter.
     * @return - x coordinate as int.
     */
    public int firstPlayZoneX() {
        return firstPlayZoneX;
    }

    /**
     * First playZone y coordinate getter.
     * @return - y coordinate as int.
     */
    public int firstPlayZoneY() {
        return firstPlayZoneY;
    }

    /**
     * Second playZone x coordinate getter.
     * @return - x coordinate as int.
     */
    public int secondPlayZoneX() {
        return secondPlayZoneX;
    }

    /**
     * Second playZone y coordinate getter.
     * @return - y coordinate as int.
     */
    public int secondPlayZoneY() {
        return secondPlayZoneY;
    }

    /**
     * Third playZone x coordinate getter.
     * @return - x coordinate as int.
     */
    public int thirdPlayZoneX() {
        return thirdPlayZoneX;
    }

    /**
     * Third playZone y coordinate getter.
     * @return - y coordinate as int.
     */
    public int thirdPlayZoneY() {
        return thirdPlayZoneY;
    }

    /**
     * Update the stage.
     * @param stage - stage number from server.
     * @return string of message to show on the screen
     */
    public String updateZone(int stage) {
        this.stage = stage;
        String message = "";

        if (stage % 2 != 0) {
            if (stage == 1) {
                message += "-----------------------PHASE ONE-----------------------\n";
            } else if (stage == 3) {
                message += "-----------------------PHASE TWO-----------------------\n";
            }else if (stage == 5) {
                message += "----------------------PHASE THREE----------------------\n";
            }
            message += "                  coordinates x: " + firstPlayZoneX + " y: " + firstPlayZoneY + "                  \n";
            message += "                      You have 60 seconds\n";
            message += "-----------------------------------------------------------";
        }
        else {
            if (stage == 2) {
                message = "FIRST ZONE SPAWNED";
            } else if (stage == 4) {
                message = "SECOND ZONE SPAWNED";
            } else if (stage == 6) {
                message = "THIRD ZONE SPAWNED";
            }
        }

        return message;
    }

    /**
     * stage getter.
     * @return - int of stage.
     */
    public int getStage() {
        return stage;
    }
}

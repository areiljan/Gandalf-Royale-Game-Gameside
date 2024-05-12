package ee.taltech.gandalf.components;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

public class PlayZone {
    private int stage;
    private final int firstPlayZoneX;
    private final int firstPlayZoneY;
    private final int secondPlayZoneX;
    private final int secondPlayZoneY;
    private final int thirdPlayZoneX;
    private final int thirdPlayZoneY;
    private int currentPlayZoneX;
    private int currentPlayZoneY;

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
        this.currentPlayZoneX = 0;
        this.currentPlayZoneY = 0;
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
            message += "60 seconds until next zone";
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
     * Rotation from the coordinates entered to current zone center.
     */
    public int rotationToCurrentZone(float x, float y) {
        double angle = Math.toDegrees(Math.atan2(currentPlayZoneY - y, currentPlayZoneX - x)) - 45;
        // the plus 45 degrees is just to adjust the rotation of the ZoneArrow.

        // Ensure angle is positive
        if (angle < 0) {
            angle += 360;
        }
        return (int) angle;
    }

    /**
     * Are the specified coordinates in the zone right now.
     * @param x - the x coordinate.
     * @param y - the y coordinate.
     * @return - true if in the zone.
     * Mirrored from serverside.
     */
    public boolean areCoordinatesInNewZone(int x, int y) {
        if (stage < 1) {
            return true;
        } else if (stage <= 2) {
            int distanceFromMidPoint = (int) sqrt(Math.pow(x - firstPlayZoneX, 2) + (Math.pow(firstPlayZoneY - y, 2)));
            return (distanceFromMidPoint < Constants.FIRST_ZONE_RADIUS);
        } else if (stage <= 4) {
            int distanceFromMidPoint = (int) sqrt(Math.pow(x - secondPlayZoneX, 2) + (Math.pow(secondPlayZoneY - y, 2)));
            return (distanceFromMidPoint < Constants.SECOND_ZONE_RADIUS);
        } else {
            int distanceFromMidPoint = (int) sqrt(Math.pow(x - thirdPlayZoneX, 2) + (Math.pow(thirdPlayZoneY - y, 2)));
            return (distanceFromMidPoint < Constants.THIRD_ZONE_RADIUS);
        }
    }

    /**
     * stage getter.
     * @return - int of stage.
     */
    public int getStage() {
        return stage;
    }
}

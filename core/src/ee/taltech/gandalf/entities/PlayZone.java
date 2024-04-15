package ee.taltech.gandalf.entities;

public class PlayZone {
    private int timer;
    private int stage;
    private int firstPlayZoneX;
    private int firstPlayZoneY;
    private int secondPlayZoneX;
    private int secondPlayZoneY;
    private int thirdPlayZoneX;
    private int thirdPlayZoneY;


    public PlayZone(int firstPlayZoneX, int firstPlayZoneY,
                    int secondPlayZoneX, int secondPlayZoneY,
                    int thirdPlayZoneX, int thirdPlayZoneY) {
        this.timer = 0;
        this.stage = 1;
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

    public void updateZone(int startTime) {
        timer = startTime;
        if (timer < 20) {
            stage = 1;
        } else if (timer < 100) {
            stage = 2;
        } else if (timer < 200) {
            stage = 3;
            // implement first zone
            // create body
        } else if (timer < 300) {
            stage = 4;
            // show second zone
        } else if (timer < 400) {
            stage = 5;
            // implement second zone
            // create body
        } else if (timer < 500) {
            stage = 6;
            // show third zone
        } else if (timer < 600) {
            stage = 7;
            // implement third zone
            // create body
            // final countdown
        } else if (timer < 800) {
            stage = 8;
            // the entire map turns red
            // create body
        }
    }

    public int getStage() {
        return stage;
    }
}

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

    public void updateZone(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }
}

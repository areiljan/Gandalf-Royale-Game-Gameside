package ee.taltech.gandalf.entities;


public class Mob {
    private float xPosition;
    private float yPosition;
    private final Integer id;
    private Integer health;

    /**
     * Construct Mob.
     *
     * @param x mob's x coordinate
     * @param y mob's y coordinate
     * @param id mob's ID
     */
    public Mob(float x, float y, Integer id) {
        this.xPosition = x;
        this.yPosition = y;
        this.id = id;
    }

    /**
     * Get mob's x coordinate.
     *
     * @return xPosition
     */
    public float getXPosition() {
        return xPosition;
    }

    /**
     * Get mob's y coordinate.
     *
     * @return yPosition
     */
    public float getYPosition() {
        return yPosition;
    }

    /**
     * Get mob's ID.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get mob's health.
     *
     * @return health
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * Set mob's health.
     *
     * @param health new health
     */
    public void setHealth(Integer health) {
        this.health = health;
    }

    /**
     * Set mob's position.
     *
     * @param xPosition new x position
     * @param yPosition new y position
     */
    public void setPosition(float xPosition, float yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}

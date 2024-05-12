package ee.taltech.gandalf.entities;


public class Mob implements Entity {
    private float xPosition;
    private float yPosition;
    private float LastXPosition;
    private final Integer id;
    private Integer health;
    private final MobAnimator mobAnimator;
    private boolean lookRight;

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
        this.lookRight = true;
        this.mobAnimator = new MobAnimator(this, id);
    }

    /**
     * mobAnimator getter.
     * @return - mobAnimator.
     */
    public MobAnimator getMobAnimator() {
        return mobAnimator;
    }

    /**
     * Get mob's x coordinate.
     *
     * @return xPosition
     */
    @Override
    public float getXPosition() {
        return xPosition;
    }

    /**
     * Get mob's y coordinate.
     *
     * @return yPosition
     */
    @Override
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
     * @param xNewPosition new x position
     * @param yNewPosition new y position
     */
    public void setPosition(float xNewPosition, float yNewPosition) {
        this.LastXPosition = this.xPosition;
        this.xPosition = xNewPosition;
        this.yPosition = yNewPosition;

        lookRight = LastXPosition <= xPosition;
    }

    /**
     * Facing direction used in MobAnimator.
     * @return - boolean of lookRight.
     */
    public boolean lookRight() {
        return lookRight;
    }
}

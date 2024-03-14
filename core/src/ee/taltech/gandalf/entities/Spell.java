package ee.taltech.gandalf.entities;

import ee.taltech.gandalf.components.SpellTypes;

public class Spell {

    private final Integer senderId;
    private double xPosition;
    private double yPosition;
    private final Integer id;

    private final SpellTypes type;

    /**
     * Construct Spell.
     *
     * @param senderId spell caster
     * @param xPosition x position
     * @param yPosition y position
     * @param id spell IDb
     */
    public Spell(Integer senderId, double xPosition, double yPosition, Integer id, SpellTypes type) {
        this.senderId = senderId;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.id = id;
        this.type = type;
    }

    /**
     * Get sender ID.
     *
     * @return senderId
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * Get x position.
     *
     * @return x position
     */
    public double getXPosition() {
        return xPosition;
    }

    /**
     * Get y position.
     *
     * @return y position
     */
    public double getYPosition() {
        return yPosition;
    }

    /**
     * Get ID.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get spell type.
     *
     * @return type
     */
    public SpellTypes getType() {
        return type;
    }

    /**
     * Set x position.
     *
     * @param xPosition new x position
     */
    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set y position.
     *
     * @param yPosition new y position
     */
    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }
}

package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import ee.taltech.gandalf.components.SpellTypes;

public class Item {

    private boolean isDropped;
    private final SpellTypes type;
    private final Integer id;
    private Integer xPosition;
    private Integer yPosition;

    private Texture texture;

    /**
     * Construct Item that is in the inventory and does not need coordinates.
     *
     * @param id item's ID
     * @param type item's type
     * @param isDropped boolean that shows if item is dropped aka on the ground
     */
    public Item(Integer id, SpellTypes type, boolean isDropped) {
        this.isDropped = isDropped;
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
    }

    /**
     * Construct Item that is on the ground and needs coordinates.
     *
     * @param id item's ID
     * @param type item's type
     * @param isDropped boolean that shows if item is dropped aka on the ground
     * @param xPosition item's x coordinate
     * @param yPosition item's y coordinate
     */
    public Item(Integer id, SpellTypes type, boolean isDropped, Integer xPosition, Integer yPosition) {
        this.isDropped = isDropped;
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     * Get if the item is dropped or not.
     *
     * @return isDropped
     */
    public boolean isDropped() {
        return isDropped;
    }

    /**
     * Get item's type.
     *
     * @return type
     */
    public SpellTypes getType() {
        return type;
    }

    /**
     * Get item's ID.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get items texture.
     *
     * @return texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get item's x coordinate.
     *
     * @return xPosition
     */
    public Integer getXPosition() {
        return xPosition;
    }

    /**
     * Get item's y coordinate.
     *
     * @return yPosition
     */
    public Integer getYPosition() {
        return yPosition;
    }

    /**
     * Set if the item is dropped or not.
     *
     * @param dropped new value for isDropped
     */
    public void setDropped(boolean dropped) {
        isDropped = dropped;
    }

    /**
     * Set item's x coordinate.
     *
     * @param xPosition new x coordinate
     */
    public void setXPosition(Integer xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set item's y coordinate.
     *
     * @param yPosition new y coordinate
     */
    public void setYPosition(Integer yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Set items texture based on what type of item it is.
     *
     * @return texture
     */
    private Texture setTextureBasedOnType() {
        if (type == SpellTypes.FIREBALL) {
            return new Texture("fireball_book.png");
        }
        return null;
    }
}

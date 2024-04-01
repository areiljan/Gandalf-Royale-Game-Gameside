package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.components.SpellTypes;
import ee.taltech.gandalf.screens.GameScreen;

public class Item {

    private final SpellTypes type;
    private final Integer id;
    private float xPosition;
    private float yPosition;

    private Texture texture;
    private Body body;

    /**
     * Construct Item that is in the inventory and does not need coordinates.
     *
     * @param id item's ID
     * @param type item's type
     */
    public Item(Integer id, SpellTypes type) {
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
        this.body = null;
    }

    /**
     * Construct Item that is on the ground and needs coordinates.
     *
     * @param id item's ID
     * @param type item's type
     * @param xPosition item's x coordinate
     * @param yPosition item's y coordinate
     */
    public Item(Integer id, SpellTypes type, float xPosition, float yPosition) {
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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
    public float getXPosition() {
        return xPosition;
    }

    /**
     * Get item's y coordinate.
     *
     * @return yPosition
     */
    public float getYPosition() {
        return yPosition;
    }

    /**
     * Set item's x coordinate.
     *
     * @param xPosition new x coordinate
     */
    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set item's y coordinate.
     *
     * @param yPosition new y coordinate
     */
    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Set items texture based on what type of item it is.
     *
     * @return texture
     */
    private Texture setTextureBasedOnType() {
        Texture itemsTexture = null;
        if (type == SpellTypes.FIREBALL) {
            // Get texture from game screen because then OpenGL does not give error
            itemsTexture = GameScreen.getTexture(GameScreen.TextureType.FIREBALL_BOOK);
        }
        return itemsTexture;
    }
}

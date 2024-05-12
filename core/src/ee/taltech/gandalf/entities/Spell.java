package ee.taltech.gandalf.entities;

import ee.taltech.gandalf.components.ItemTypes;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ee.taltech.gandalf.components.TextureType;
import ee.taltech.gandalf.screens.GameScreen;

import java.util.Optional;

public class Spell implements Entity {

    private final Integer senderId;
    private double previousXPosition;
    private double previousYPosition;
    private double xPosition;
    private double yPosition;
    private final Integer id;

    private final ItemTypes type;
    private Animation<TextureRegion> animation;

    /**
     * Construct Spell.
     *
     * @param senderId spell caster
     * @param xPosition x position
     * @param yPosition y position
     * @param id spell IDb
     */
    public Spell(Integer senderId, double xPosition, double yPosition, Integer id, ItemTypes type) {
        this.senderId = senderId;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.previousXPosition = 0;
        this.previousYPosition = 0;
        this.id = id;
        this.type = type;
        createAnimations();
    }

    /**
     * Getter for the fireballAnimations.
     *
     * @return fireball animation
     */
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    /**
     * Return the movement direction.
     * Useful for some animations.
     *
     * @return - rotation.
     */
    public Optional<Float> rotation() {
        if (previousXPosition != 0 && previousYPosition != 0) {
            double xDifference = previousXPosition - xPosition;
            double yDifference = previousYPosition - yPosition;
            float angle = (float) (Math.atan2(yDifference, xDifference) * 180 / MathUtils.PI);
            return Optional.of(angle);
        }
        previousXPosition = xPosition;
        previousYPosition = yPosition;
        return Optional.empty();
    }

    /**
     * Create animations for the fireball.
     */
    private void createAnimations() {
        if (type == ItemTypes.FIREBALL) {
            Texture fireballTexture = GameScreen.getTexture(TextureType.FIREBALL);
            // Define frames in the spritesheet
            TextureRegion[][] frames = TextureRegion.split(fireballTexture, 72, 32);
            // Convert 2D array to 1D array
            TextureRegion[] fireballFrames = new TextureRegion[10];

            for (int i = 0; i < 10; i++) {
                fireballFrames[i] = frames[0][i];
                fireballFrames[i].flip(true, false);
            }
            animation = new Animation<>(0.1F, fireballFrames);
        } else if (type == ItemTypes.PLASMA) {
            Texture plasmaTexture = GameScreen.getTexture(TextureType.PLASMA);
            // redefine frames in the spritesheet
            TextureRegion[][] frames = TextureRegion.split(plasmaTexture, 64, 48);
            // Convert 2D array to 1D array
            TextureRegion[] plasmaFrames = new TextureRegion[8];
            for (int i = 0; i < 8; i++) {
                plasmaFrames[i] = frames[0][i];
                plasmaFrames[i].flip(true, false);
            }
            animation = new Animation<>(0.1F, plasmaFrames);
        } else if (type == ItemTypes.METEOR) {
            Texture plasmaTexture = GameScreen.getTexture(TextureType.METEOR);
            // redefine frames in the spritesheet
            TextureRegion[][] frames = TextureRegion.split(plasmaTexture, 96, 64);
            // Convert 2D array to 1D array
            TextureRegion[] plasmaFrames = new TextureRegion[8];
            for (int i = 0; i < 8; i++) {
                plasmaFrames[i] = frames[0][i];
                plasmaFrames[i].flip(true, false);
            }
            animation = new Animation<>(0.1F, plasmaFrames);
        } else if (type == ItemTypes.KUNAI) {
            Texture plasmaTexture = GameScreen.getTexture(TextureType.KUNAI);
            // redefine frames in the spritesheet
            TextureRegion[][] frames = TextureRegion.split(plasmaTexture, 32, 16);
            // Convert 2D array to 1D array
            TextureRegion[] plasmaFrames = new TextureRegion[8];
            for (int i = 0; i < 8; i++) {
                plasmaFrames[i] = frames[0][i];
                plasmaFrames[i].flip(true, false);
            }
            animation = new Animation<>(0.1F, plasmaFrames);
        }
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
    @Override
    public float getXPosition() {
        return (float) xPosition;
    }


    /**
     * Get y position.
     *
     * @return y position
     */
    @Override
    public float getYPosition() {
        return (float) yPosition;
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
    public ItemTypes getType() {
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

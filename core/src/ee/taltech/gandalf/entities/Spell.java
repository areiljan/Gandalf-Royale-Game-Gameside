package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ee.taltech.gandalf.components.SpellTypes;
import ee.taltech.gandalf.screens.GameScreen;

import java.util.Optional;

public class Spell {

    private final Integer senderId;
    private double previousXPosition;
    private double previousYPosition;
    private double xPosition;
    private double yPosition;
    private final Integer id;

    private final SpellTypes type;
    private Animation<TextureRegion> fireballAnimation;

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
        this.previousXPosition = 0;
        this.previousYPosition = 0;
        this.id = id;
        this.type = type;
        CreateAnimations();
    }

    public Animation<TextureRegion> getFireballAnimation() {
        return fireballAnimation;
    }

    /**
     * Return the movement direction.
     * Useful for some animations.
     *
     * @return - rotation.
     */
    public Optional<Float> rotation() {
        if (type == SpellTypes.FIREBALL && previousXPosition != 0 && previousYPosition != 0) {
            double xDifference = previousXPosition - xPosition;
            double yDifference = previousYPosition - yPosition;
            float angle = (float) (Math.atan2(yDifference, xDifference) * 180 / MathUtils.PI);
            return Optional.of(angle);
        }
        previousXPosition = xPosition;
        previousYPosition = yPosition;
        return Optional.empty();
    }

    private void CreateAnimations() {
        Texture fireballTexture = GameScreen.getTexture(GameScreen.TextureType.FIREBALL);
        // Define frames in the spritesheet
        TextureRegion[][] frames = TextureRegion.split(fireballTexture, 32, 17);

        // Convert 2D array to 1D array
        TextureRegion[] fireballFrames = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            fireballFrames[i] = frames[0][i];
            fireballFrames[i].flip(true, false);
        }

        fireballAnimation = new Animation<>(0.2F, fireballFrames);
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

package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.gandalf.components.TextureType;
import ee.taltech.gandalf.screens.GameScreen;

public class MobAnimator {
    private final Texture pumpkinMovementTexture;
    private final Texture pumpkinAttackTexture;
    private final Mob mob;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> movementAnimation;
    private Animation<TextureRegion> flippedAttackAnimation;
    private Animation<TextureRegion> flippedMovementAnimation;

    /**
     * Construct PlayerCharacterAnimator.
     *
     * @param mob mob that is being animated.
     * @param mobId mobs id.
     */
    public MobAnimator(Mob mob, Integer mobId) {
        this.mob = mob;
        this.pumpkinMovementTexture = GameScreen.getTexture(TextureType.PUMPKINWALK);
        this.pumpkinAttackTexture = GameScreen.getTexture(TextureType.PUMPKINATTACK);
        createAnimations();
    }

    /**
     * ActionAnimation getter.
     *
     * @return actionAnimation.
     */
    public Animation<TextureRegion> attackAnimation() {
        if (mob.lookRight()) {
            return flippedAttackAnimation;
        } else {
            return attackAnimation;
        }
    }

    /**
     * MovementAnimation getter.
     *
     * @return movementAnimation.
     */
    public Animation<TextureRegion> movementAnimation() {
        if (mob.lookRight()) {
            return flippedMovementAnimation;
        } else {
            return movementAnimation;
        }
    }

    /**
     * Create animations for the selected mob.
     * Initialized upon mob creation.
     */
    private void createAnimations() {
        // Define frames in the sprite sheet
        TextureRegion[][] movementFrames2D = TextureRegion.split(pumpkinMovementTexture, 28, 30);
        TextureRegion[][] attackFrames2D = TextureRegion.split(pumpkinAttackTexture, 28, 30);

        // Convert 2D array to 1D array
        TextureRegion[] movementFrames = new TextureRegion[7];
        TextureRegion[] attackFrames = new TextureRegion[4];
        TextureRegion[] flippedMovementFrames = new TextureRegion[movementFrames.length];
        TextureRegion[] flippedAttackFrames = new TextureRegion[attackFrames.length];
        int index = 0;
        for (int j = 0; j < 7; j++) {
            movementFrames[index] = movementFrames2D[0][j];
            flippedMovementFrames[index] = new TextureRegion(movementFrames[index]);
            flippedMovementFrames[index].flip(true, false);
            index++;
        }
        index = 0;
        for (int j = 0; j < 4; j++) {
            attackFrames[index] = attackFrames2D[0][j];
            flippedAttackFrames[index] = new TextureRegion(attackFrames[index]);
            flippedAttackFrames[index].flip(true, false);
            index++;
        }
        movementAnimation = new Animation<>(0.1F, movementFrames);
        flippedMovementAnimation = new Animation<>(0.1F, flippedMovementFrames);
        attackAnimation = new Animation<>(0.1F, attackFrames);
        flippedAttackAnimation = new Animation<>(0.1F, flippedAttackFrames);
    }
}

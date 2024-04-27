package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.gandalf.components.TextureType;
import ee.taltech.gandalf.network.messages.game.ActionTaken;
import ee.taltech.gandalf.screens.GameScreen;

public class MobAnimator {
    private final Texture pumpkinMovementTexture;
    private final Texture pumpkinAttackTexture;
    private final Mob mob;
    private boolean previousAction;
    public int actionTimes;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> movementAnimation;
    private Animation<TextureRegion> flippedAttackAnimation;
    private Animation<TextureRegion> flippedMovementAnimation;
    Texture spriteSheet;

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
        this.actionTimes = 0;
        this.previousAction = false;
        createAnimations();
    }

    /**
     * ActionAnimation getter.
     *
     * @return actionAnimation.
     */
    public Animation<TextureRegion> attackAnimation() {
        if (mob.lookRight()) {
            return attackAnimation;
        } else {
            return flippedAttackAnimation;
        }
    }

    /**
     * MovementAnimation getter.
     *
     * @return movementAnimation.
     */
    public Animation<TextureRegion> movementAnimation() {
        if (mob.lookRight()) {
            return movementAnimation;
        } else {
            return flippedMovementAnimation;
        }
    }

    /**
     * Create animations for the selected character.
     * Initialized upon character creation.
     */
    private void createAnimations() {
        // Define frames in the sprite sheet
        TextureRegion[][] movementFrames2d = TextureRegion.split(pumpkinMovementTexture, 25, 29);
        TextureRegion[][] attackFrames2d = TextureRegion.split(pumpkinAttackTexture, 25, 29);

        // Convert 2D array to 1D array
        TextureRegion[] movementFrames = new TextureRegion[7];
        TextureRegion[] flippedMovementFrames = new TextureRegion[7];

        TextureRegion[] attackFrames = new TextureRegion[4];
        TextureRegion[] flippedAttackFrames = new TextureRegion[4];

        System.arraycopy(movementFrames2d, 0, movementFrames, 0, 7);
        System.arraycopy(attackFrames2d, 0, attackFrames, 0, 4);


        for (int i = 0; i < movementFrames.length; i++) {
            // Flip frames for movement animation
            flippedMovementFrames[i] = new TextureRegion(movementFrames[i]);
            flippedMovementFrames[i].flip(true, false);
        }
        for (int i = 0; i < attackFrames.length; i++) {
            // Flip frames for action animation
            flippedAttackFrames[i] = new TextureRegion(attackFrames[i]);
            flippedAttackFrames[i].flip(true, false);
        }

        // Make an animation out of each array of frames.
        movementAnimation = new Animation<>(0.1F, movementFrames);
        flippedMovementAnimation = new Animation<>(0.1f, flippedMovementFrames);
        attackAnimation = new Animation<>(0.1F, attackFrames);
        flippedAttackAnimation = new Animation<>(0.1F, flippedAttackFrames);
    }
}

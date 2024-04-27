import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.gandalf.components.TextureType;
import ee.taltech.gandalf.entities.Mob;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.network.messages.game.ActionTaken;
import ee.taltech.gandalf.screens.GameScreen;

public class MobAnimator {
    private final Texture pumpkinWalkTexture;
    private final Texture pumkinAttackTexture;
    private final Mob mob;
    private boolean previousAction;
    private AnimationStates state;

    public int actionTimes;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> actionAnimation;
    private Animation<TextureRegion> movementAnimation;
    private Animation<TextureRegion> flippedIdleAnimation;
    private Animation<TextureRegion> flippedMovementAnimation;
    private Animation<TextureRegion> flippedDeathAnimation;
    private Animation<TextureRegion> flippedActionAnimation;

    public int mouseXPosition;
    public int mouseYPosition;

    Texture spriteSheet;

    /**
     * Construct PlayerCharacterAnimator.
     *
     * @param mob mob that is being animated.
     * @param mobId mobs id.
     */
    public MobAnimator(Mob mob, Integer mobId) {
        this.mob = mob;
        this.pumpkinWalkTexture = GameScreen.getTexture(TextureType.PUMPKINWALK);
        this.pumkinAttackTexture = GameScreen.getTexture(TextureType.PUMPKINATTACK);
        this.actionTimes = 0;
        this.state = AnimationStates.MOVEMENT;
        this.previousAction = false;
        createAnimations();
    }

    /**
     * Different states of animation.
     */
    public enum AnimationStates {
        MOVEMENT,
        ATTACK
    }

    /**
     * Get animations current state.
     *
     * @return state
     */
    public AnimationStates getState() {
        return state;
    }

    /**
     * Change animator state based on character action.
     */
    public void setState(AnimationStates animationState) {
        state = animationState;
    }

    /**
     * Change animator state based on character action.
     */
    public void setState() {

        } else {
            previousX = mob.xPosition;
            previousY = mob.yPosition;
            state = AnimationStates.MOVEMENT;
        }
    }

    /**
     * Getter for the characterTexture
     *
     * @return texture
     */
    public Texture characterTexture() {
        return characterTexture;
    }

    /**
     * ActionAnimation getter.
     *
     * @return actionAnimation.
     */
    public Animation<TextureRegion> attackAnimation() {
        if (mob.) {
            return actionAnimation;
        } else {
            return flippedActionAnimation;
        }
    }

    /**
     * MovementAnimation getter.
     *
     * @return movementAnimation.
     */
    public Animation<TextureRegion> movementAnimation() {
        if (lookRight) {
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
        TextureRegion[][] frames = TextureRegion.split(characterTexture, 64, 64);

        // Convert 2D array to 1D array
        TextureRegion[] animationFrames = new TextureRegion[36];
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                animationFrames[index++] = frames[i][j];
            }
        }
        TextureRegion[] idleFrames = new TextureRegion[6];
        TextureRegion[] movementFrames = new TextureRegion[6];
        TextureRegion[] deathFrames = new TextureRegion[6];
        TextureRegion[] actionFrames = new TextureRegion[6];
        TextureRegion[] flippedIdleFrames = new TextureRegion[idleFrames.length];
        TextureRegion[] flippedMovementFrames = new TextureRegion[movementFrames.length];
        TextureRegion[] flippedDeathFrames = new TextureRegion[deathFrames.length];
        TextureRegion[] flippedActionFrames = new TextureRegion[actionFrames.length];
        // Make the big animationFrames array into different animations.
        System.arraycopy(animationFrames, 0, idleFrames, 0, 6);
        System.arraycopy(animationFrames, 6, movementFrames, 0, 6);
        System.arraycopy(animationFrames, 12, deathFrames, 0, 6);
        System.arraycopy(animationFrames, 18, actionFrames, 0, 6);


        // Flip the frames for each animation
        for (int i = 0; i < idleFrames.length; i++) {
            // Flip frames for idle animation
            flippedIdleFrames[i] = new TextureRegion(idleFrames[i]);
            flippedIdleFrames[i].flip(true, false);
        }
        for (int i = 0; i < movementFrames.length; i++) {
            // Flip frames for movement animation
            flippedMovementFrames[i] = new TextureRegion(movementFrames[i]);
            flippedMovementFrames[i].flip(true, false);
        }
        for (int i = 0; i < deathFrames.length; i++) {
            // Flip frames for death animation
            flippedDeathFrames[i] = new TextureRegion(deathFrames[i]);
            flippedDeathFrames[i].flip(true, false);
        }
        for (int i = 0; i < actionFrames.length; i++) {
            // Flip frames for action animation
            flippedActionFrames[i] = new TextureRegion(actionFrames[i]);
            flippedActionFrames[i].flip(true, false);
        }


        // Make an animation out of each array of frames.
        idleAnimation = new Animation<>(0.1F, idleFrames);
        movementAnimation = new Animation<>(0.1F, movementFrames);
        deathAnimation = new Animation<>(0.3F, deathFrames);
        flippedDeathAnimation = new Animation<>(0.3f, flippedDeathFrames);
        actionAnimation = new Animation<>(0.1F, actionFrames);
        flippedActionAnimation = new Animation<>(0.1f, flippedActionFrames);
        flippedIdleAnimation = new Animation<>(0.1f, flippedIdleFrames);
        flippedMovementAnimation = new Animation<>(0.1f, flippedMovementFrames);
    }

    /**
     * Used for animation.
     * Is the Character looking right.
     * @return - true if it is.
     */
    public boolean lookRight() {
        return lookRight;
    }

    /**
     * Update player's action.
     */
    public void updateAction(ActionTaken actionTaken) {
        if (actionTaken.action) {
            if (!previousAction) {
                setState(ee.taltech.gandalf.entities.PlayerCharacterAnimator.AnimationStates.ACTION);
            }
            previousAction = true;
        } else {
            previousAction = false;
        }

        // updateAction is sent every frame.
        this.mouseXPosition = actionTaken.mouseX;
        this.mouseYPosition = actionTaken.mouseY;
        if (mouseXPosition > 0) {
            lookRight = true;
        } else {
            lookRight = false;
        }
    }
}

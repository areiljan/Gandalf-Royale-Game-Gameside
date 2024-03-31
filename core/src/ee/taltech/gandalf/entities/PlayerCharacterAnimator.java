package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.gandalf.network.messages.game.ActionTaken;
import ee.taltech.gandalf.screens.GameScreen;

public class PlayerCharacterAnimator {
    private final Texture characterTexture;
    private final PlayerCharacter playerCharacter;
    private int previousY;
    private int previousX;
    private boolean lookRight;
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
    public boolean action() {
        return action;
    }
    private boolean action;
    Texture spriteSheet;

    public PlayerCharacterAnimator(PlayerCharacter playerCharacter, Integer playerID) {
        this.playerCharacter = playerCharacter;
        this.action = false;
        this.lookRight = true;
        this.previousX = 0;
        this.previousY = 0;
        this.characterTexture = GameScreen.getWizardTexture(playerID);
        createAnimations();
    }

    /**
     * Getter for the characterTexture
     * @return texture
     */
    public Texture characterTexture() {
        return characterTexture;
    }

    /**
     * idleAnimation getter.
     * @return idleAnimation.
     */
    public Animation<TextureRegion> idleAnimation() {
        if (lookRight) {
            return idleAnimation;
        } else {
            return flippedIdleAnimation;
        }
    }

    /**
     * deathAnimation getter.
     * @return deathAnimation.
     * @return
     */
    public Animation<TextureRegion> deathAnimation() {
        if (lookRight) {
            return deathAnimation;
        } else {
            return flippedDeathAnimation;
        }
    }

    /**
     * actionAnimation getter.
     * @return actionAnimation.
     */
    public Animation<TextureRegion> actionAnimation() {
        if (lookRight) {
            return actionAnimation;
        } else {
            return flippedActionAnimation;
        }
    }

    /**
     * movementAnimation getter.
     * @return - movementAnimation.
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
        // Define frames in the spritesheet
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
        idleAnimation = new Animation<>(0.2F, idleFrames);
        movementAnimation = new Animation<>(0.2F, movementFrames);
        deathAnimation = new Animation<>(0.2F, deathFrames);
        actionAnimation = new Animation<>(0.2F, actionFrames);
        flippedIdleAnimation = new Animation<>(0.2f, flippedIdleFrames);
        flippedMovementAnimation = new Animation<>(0.2f, flippedMovementFrames);
        flippedDeathAnimation = new Animation<>(0.2f, flippedDeathFrames);
        flippedActionAnimation = new Animation<>(0.2f, flippedActionFrames);
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
     * Are the coordinates changing.
     *
     * @return - true if they are.
     */
    public boolean isMoving() {
        if (previousX != playerCharacter.xPosition || previousY != playerCharacter.yPosition) {
            previousX = playerCharacter.xPosition;
            previousY = playerCharacter.yPosition;
            return true;
        } else {
            previousX = playerCharacter.xPosition;
            previousY = playerCharacter.yPosition;
            return false;
        }
    }

    /**
     * Know which direction a player is moving.
     */
    public void updatePlayerDirection() {
        if (mouseXPosition > 0) {
            lookRight = true;
        } else {
            lookRight = false;
        }
    }

    /**
     * Update player's action.
     */
    public void updateAction(ActionTaken actionTaken) {
        // updateAction is sent every frame.
        this.action = actionTaken.action;
        this.mouseXPosition = actionTaken.mouseX;
        this.mouseYPosition = actionTaken.mouseY;
    }

    /**
     * Set the player animation based on the movement and actions.
     */
    public void setAnimation() {
        // Define frames in the spritesheet
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, 64, 64);

        // Convert 2D array to 1D array
        TextureRegion[] animationFrames = new TextureRegion[36];
        int index = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                animationFrames[index++] = frames[i][j];
            }
        }

    }
}

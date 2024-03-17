package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.network.messages.game.ActionTaken;
import ee.taltech.gandalf.network.messages.game.KeyPress;
import ee.taltech.gandalf.network.messages.game.MouseClicks;
import ee.taltech.gandalf.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ee.taltech.gandalf.screens.GameScreen.TextureType.WIZARD;

public class PlayerCharacter {
    public static final Integer WIDTH = 55;
    public static final Integer HEIGHT = 70;
    private final Texture characterTexture;
    private int previousY;
    private int previousX;
    private boolean lookRight;
    private Animation idleAnimation;
    private Animation deathAnimation;
    private Animation actionAnimation;
    private Animation movementAnimation;
    private Animation flippedIdleAnimation;
    private Animation flippedMovementAnimation;
    private Animation flippedDeathAnimation;
    private Animation flippedActionAnimation;
    private TextureRegion[] actionFrames;
    private TextureRegion[] deathFrames;
    private TextureRegion[] movementFrames;
    private TextureRegion[] idleFrames;

    public boolean action() {
        return action;
    }

    private boolean action;
    private Body body;
    public int xPosition;
    public int yPosition;
    public int playerID;

    private boolean moveLeft;
    boolean moveRight;
    boolean moveDown;
    boolean moveUp;
    public int mouseXPosition;
    public int mouseYPosition;
    public boolean mouseLeftClick;
    public Integer health;
    public double mana;
    private List<Item> inventory;
    private Integer selectedSlot;
    Texture spriteSheet;


    /**
     * Getter for the characterTexture
     * @return texture
     */
    public Texture characterTexture() {
        return characterTexture;
    }

    /**
     * Construct PlayerCharacter.
     *
     * @param playerID player's ID
     */
    public PlayerCharacter(Integer playerID) {
        // Here should be the random spawn points for a PlayerCharacter
        this.xPosition = 0;
        this.yPosition = 0;
        this.playerID = playerID;
        this.action = false;
        this.lookRight = true;
        this.previousX = 0;
        this.previousY = 0;
        health = 100;
        mana = 100;

        this.characterTexture = GameScreen.getWizardTexture(playerID);
        createAnimations();

        inventory = new ArrayList<>();
        // Add empty items to inventory slots
        inventory.add(0, null);
        inventory.add(1, null);
        inventory.add(2, null);

        selectedSlot = 0; // By default, player's first inventory slot is selected
    }

    public Animation idleAnimation() {
        if (lookRight) {
            return idleAnimation;
        } else {
            return flippedIdleAnimation;
        }
    }

    public Animation deathAnimation() {
        if (lookRight) {
            return deathAnimation;
        } else {
            return flippedDeathAnimation;
        }
    }

    public Animation actionAnimation() {
        if (lookRight) {
            return actionAnimation;
        } else {
            return flippedActionAnimation;
        }
    }

    public Animation movementAnimation() {
        if (lookRight) {
            return movementAnimation;
        } else {
            return flippedMovementAnimation;
        }
    }

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
        idleFrames = new TextureRegion[6];
        movementFrames = new TextureRegion[6];
        deathFrames = new TextureRegion[6];
        actionFrames = new TextureRegion[6];
        TextureRegion[] flippedIdleFrames = new TextureRegion[idleFrames.length];
        TextureRegion[] flippedMovementFrames = new TextureRegion[movementFrames.length];
        TextureRegion[] flippedDeathFrames = new TextureRegion[deathFrames.length];
        TextureRegion[] flippedActionFrames = new TextureRegion[actionFrames.length];
        System.arraycopy(animationFrames, 0, idleFrames, 0, 6);
        System.arraycopy(animationFrames, 6, movementFrames, 0, 6);
        System.arraycopy(animationFrames, 12, deathFrames, 0, 6);
        System.arraycopy(animationFrames, 18, actionFrames, 0, 6);

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
        if (previousX != xPosition || previousY != yPosition) {
            previousX = xPosition;
            previousY = yPosition;
            return true;
        } else {
            previousX = xPosition;
            previousY = yPosition;
            return false;
        }
    }

    /**
     * Know which direction a player is moving.
     */
    public void updatePlayerDirection() {
        if (mouseXPosition > 100) {
            lookRight = true;
        } else {
            lookRight = false;
        }
    }

    /**
     * Create player's hit box.
     *
     * @param world world, where hit boxes are in
     */
    public void createHitBox(World world) {
        // Create a dynamic or static body for the player
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(xPosition, yPosition);
        Body hitBoxBody = world.createBody(bodyDef);

        // Create a fixture defining the hit box shape
        PolygonShape hitBoxShape = new PolygonShape();
        hitBoxShape.setAsBox(PlayerCharacter.WIDTH, PlayerCharacter.HEIGHT);

        // Attach the fixture to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitBoxShape;
        fixtureDef.density = 1.0f;
        hitBoxBody.createFixture(fixtureDef);

        // Clean up
        hitBoxShape.dispose();

        hitBoxBody.setUserData(this);
        this.body = hitBoxBody;
    }

    /**
     * Set player's health.
     *
     * @param newHealth new health that is set to player
     */
    public void setHealth(Integer newHealth) {
        health = newHealth;
    }

    /**
     * Set player's mana.
     *
     * @param newMana new mana that is set to player.
     */
    public void setMana(double newMana) {
        mana = newMana;
    }

    /**
     * Get player's body.
     *
     * @return body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Get player's inventory.
     *
     * @return inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Get player's selected slot.
     *
      * @return selectedSlot
     */
    public Integer getSelectedSlot() {
        return selectedSlot;
    }

    /**
     * Set player's selected slot.
     *
     * @param selectedSlot new selected slot
     */
    public void setSelectedSlot(Integer selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    /**
     * Pick up item from the ground.
     *
     * @param item picked up item
     */
    public void pickUpItem(Item item) {
        if (inventory.get(selectedSlot) == null) { // Pick up item when selected slot is empty
            inventory.remove((int) selectedSlot); // Remove null from the spot
            inventory.add(selectedSlot, item); // Add item to the spot
        }
    }

    /**
     * Drop item.
     *
     * @param droppedItemsId ID of the item that is dropped
     * @return droppedItem
     */
    public Item dropItem(Integer droppedItemsId) {
        for (Item item : inventory) {
            if (!Objects.equals(item, null) && Objects.equals(item.getId(), droppedItemsId)) { // Find correct item
                int itemsIndex = inventory.indexOf(item); // Get items index
                inventory.remove(item); // Remove item from inventory
                inventory.add(itemsIndex, null); // Put null in the empty spot
                return item;
            }
        }
        return null; // Should never get to this point
    }

    /**
     * Update player's position.
     */
    public void updatePosition() {
        // updatePosition is activated every TPS.

        // One key press distance that a character travels.
        int distance = 8;
        // Diagonal movement correction formula.
        int diagonal = (int) (distance / Math.sqrt(2));
        if (moveLeft && moveUp) {
            this.xPosition -= diagonal;
            this.yPosition += diagonal;
        } else if (moveLeft && moveDown) {
            this.xPosition -= diagonal;
            this.yPosition -= diagonal;
        } else if (moveRight && moveUp) {
            this.xPosition += diagonal;
            this.yPosition += diagonal;
        } else if (moveRight && moveDown) {
            this.xPosition += diagonal;
            this.yPosition -= diagonal;
        } else {
            oneDirectionMovement(distance);
        }
        // Set the position of the Box2D body to match the player's coordinates
        body.setTransform( (float) xPosition + 91, (float) yPosition + 70, body.getAngle());
    }

    /**
     * Update player's action.
     */
    public void updateAction(ActionTaken actionTaken) {
        // updateAction is sent every frame.
        this.action = actionTaken.action;
        System.out.println("PLAYERCHARACTER UPDATE_ACTION id:" + actionTaken.userID + " x: " + actionTaken.mouseX + " y: " + actionTaken.mouseY);
        this.mouseXPosition = actionTaken.mouseX;
        this.mouseYPosition = actionTaken.mouseY;
    }

    /**
     * Moving only in one direction.
     *
     * @param distance how much player is moving
     */
    private void oneDirectionMovement(int distance) {
        if (moveLeft) {
            this.xPosition -= distance;
        }
        if (moveRight) {
            this.xPosition += distance;
        }
        if (moveUp) {
            this.yPosition += distance;
        }
        if (moveDown) {
            this.yPosition -= distance;
        }
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

    /**
     * Set player's movement based on keypress.
     *
     * @param keyPress incoming keypress
     */
    public void setMovement(KeyPress keyPress) {
        // Set a action where player should be headed.
        if (keyPress.action == KeyPress.Action.LEFT) {
            this.moveLeft = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.RIGHT) {
            this.moveRight = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.UP) {
            this.moveUp = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.DOWN) {
            this.moveDown = keyPress.pressed;
        }
    }

    /**
     * Set player characters position.
     *
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     */
    public void setPosition(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}

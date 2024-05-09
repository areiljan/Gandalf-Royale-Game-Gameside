package ee.taltech.gandalf.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.components.Constants;
import ee.taltech.gandalf.network.messages.game.KeyPress;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerCharacter implements Entity {
    public static final Integer WIDTH = 12;
    public static final Integer HEIGHT = 24;

    private final PlayerCharacterAnimator playerCharacterAnimator;
    private Body body;

    private float xPosition;
    private float yPosition;
    private float lastXPosition;
    private float lastYPosition;
    public int playerID;

    private Vector2 movement;

    private Integer health;
    private double mana;
    private Integer coins;

    private List<Item> inventory;
    private Integer selectedSlot;


    /**
     * Construct PlayerCharacter.
     *
     * @param playerID player's ID
     */
    public PlayerCharacter(Integer playerID) {
        // Here should be the random spawn points for a PlayerCharacter
        this.xPosition = 4800 / Constants.PPM;
        this.yPosition = 4800 / Constants.PPM;
        this.playerID = playerID;

        health = 100;
        mana = 100;
        coins = 0;

        this.playerCharacterAnimator = new PlayerCharacterAnimator(this, playerID);

        this.movement = Vector2.Zero;

        inventory = new ArrayList<>();
        // Add empty items to inventory slots
        inventory.add(0, null);
        inventory.add(1, null);
        inventory.add(2, null);

        selectedSlot = 0; // By default, player's first inventory slot is selected
    }
    public PlayerCharacterAnimator getPlayerAnimator() {
        return playerCharacterAnimator;
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
        bodyDef.fixedRotation = true;
        Body playerBody = world.createBody(bodyDef);

        // Create a fixture defining the hit box shape
        PolygonShape hitBoxShape = new PolygonShape();
        hitBoxShape.setAsBox(PlayerCharacter.WIDTH / Constants.PPM, PlayerCharacter.HEIGHT / Constants.PPM);

        CircleShape collisionCircle = new CircleShape();
        float circleRadius = 10 / Constants.PPM;
        collisionCircle.setRadius(circleRadius);
        collisionCircle.setPosition(new Vector2(0f, -(PlayerCharacter.HEIGHT / Constants.PPM) + circleRadius));

        FixtureDef fixtureDefCollisionCircle = new FixtureDef();
        fixtureDefCollisionCircle.shape = collisionCircle;
        fixtureDefCollisionCircle.density = 0.0f;

        FixtureDef fixtureDefHitbox = new FixtureDef();
        fixtureDefHitbox.shape = hitBoxShape;
        fixtureDefHitbox.density = 0.0f;
        fixtureDefHitbox.isSensor = true;

        // Attach the fixture to the body
        playerBody.createFixture(fixtureDefHitbox);
        playerBody.createFixture(fixtureDefCollisionCircle);

        // Clean up
        hitBoxShape.dispose();
        collisionCircle.dispose();

        playerBody.setUserData(this);
        this.body = playerBody;
    }

    /**
     * Get player's health.
     *
     * @return health
     */
    public Integer getHealth() {
        return health;
    }

    /**
     * Get player's mana.
     *
     * @return mana
     */
    public double getMana() {
        return mana;
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
     * Remove item.
     *
     * @param itemsId ID of the item that is removed
     * @return removed item
     */
    public Item removeItem(Integer itemsId) {
        for (Item item : inventory) {
            if (!Objects.equals(item, null) && Objects.equals(item.getId(), itemsId)) { // Find correct item
                int itemsIndex = inventory.indexOf(item); // Get items index
                inventory.remove(item); // Remove item from inventory
                inventory.add(itemsIndex, null); // Put null in the empty spot
                return item;
            }
        }
        return null; // Should never get to this point
    }

    /**
     * Add coin to player.
     */
    public void addCoin() {
        coins++;
    }

    /**
     * Get player's coins.
     *
     * @return coins
     */
    public Integer getCoins() {
        return coins;
    }


    /**
     * Set player's movement based on keypress.
     *
     * @param keyPress incoming keypress
     */
    public void setMovement(KeyPress keyPress) {
        // Set an action where player should be headed.
        if (keyPress.pressed) {
            if (keyPress.action == KeyPress.Action.LEFT) {
                movement.x = -1;
            } else if (keyPress.action == KeyPress.Action.RIGHT) {
                movement.x = 1;
            } else if (keyPress.action == KeyPress.Action.UP) {
                movement.y = 1;
            } else if (keyPress.action == KeyPress.Action.DOWN) {
                movement.y = -1;
            }
        } else {
            // Only cancel the movement if one key is pressed down.
            if (keyPress.action == KeyPress.Action.LEFT && movement.x < 0) {
                movement.x = 0;
            } else if (keyPress.action == KeyPress.Action.RIGHT && movement.x > 0) {
                movement.x = 0;
            } else if (keyPress.action == KeyPress.Action.UP && movement.y > 0) {
                movement.y = 0;
            } else if (keyPress.action == KeyPress.Action.DOWN && movement.y < 0) {
                movement.y = 0;
            }
        }
    }

    /**
     * Applies force to the movement direction, even after collision.
     */
    public void updateVector() {
        Vector2 scaledMovement = movement.cpy().scl(Constants.movementSpeed);
        float maxSpeed = Constants.movementSpeed * (float) Math.sqrt(2);
        scaledMovement.clamp(maxSpeed, maxSpeed);
        body.setLinearVelocity(scaledMovement);
    }

    /**
     * Set player characters position.
     *
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     */
    public void setPosition(float xPosition, float yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     * Update the position of the body.
     */
    public void updatePosition() {
        if (lastYPosition != yPosition && lastXPosition != xPosition) {
            this.body.setTransform(xPosition, yPosition, body.getAngle());
            this.lastXPosition = xPosition;
            this.lastYPosition = yPosition;
        }
    }

    /**
     * @return Player x position
     */
    @Override
    public float getXPosition() {
        if (body == null) {
            return xPosition;
        }
        return body.getPosition().x;
    }

    @Override
    public float getHeight() {
        return PlayerCharacter.HEIGHT;
    }

    /**
     * @return Player y position
     */
    @Override
    public float getYPosition() {
        if (body == null) {
            return yPosition;
        }
        return body.getPosition().y;
    }


}

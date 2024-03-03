package ee.taltech.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ee.taltech.network.messages.KeyPress;
import ee.taltech.network.messages.MouseClicks;
import ee.taltech.screen.screens.GameScreen;

public class PlayerCharacter {
    public int getxPosition() {
        return xPosition;
    }

    public int xPosition;

    public int getyPosition() {
        return yPosition;
    }

    public int yPosition;
    public int playerID;
    public boolean moveLeft;
    boolean moveRight;
    boolean moveDown;
    boolean moveUp;
    public double mouseXPosition;
    public double mouseYPosition;
    public boolean mouseLeftClick;
    public boolean faceLeft = false;
    private GameScreen gameScreen;
    public Integer health;
    public Integer mana;

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
        health = 100;
        mana = 100;
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
    public void setMana(Integer newMana) {
        health = newMana;
    }

    /**
     * Update player's position.
     */
    public void updatePosition() {
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
    }

    public void setMouseHover(MouseClicks mouseClicks) {
        this.mouseXPosition = mouseClicks.mouseXPosition;
        this.mouseYPosition = mouseClicks.mouseYPosition;
        this.mouseLeftClick = mouseClicks.leftMouse;
    }



    /**
     * Set player's movement based on keypress.
     *
     * @param keyPress incoming keypress
     */
    public void setMovement(KeyPress keyPress) {
        // Set a direction where player should be headed.
        if (keyPress.direction == KeyPress.Direction.LEFT) {
            this.moveLeft = keyPress.pressed;
        }
        if (keyPress.direction == KeyPress.Direction.RIGHT) {
            this.moveRight = keyPress.pressed;
        }
        if (keyPress.direction == KeyPress.Direction.UP) {
            this.moveUp = keyPress.pressed;
        }
        if (keyPress.direction == KeyPress.Direction.DOWN) {
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
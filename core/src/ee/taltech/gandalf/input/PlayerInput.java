package ee.taltech.gandalf.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.SpellTypes;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.network.messages.game.KeyPress;
import ee.taltech.gandalf.network.messages.game.MouseClicks;

import java.util.Objects;

public class PlayerInput implements InputProcessor {
    private GandalfRoyale game;
    private PlayerCharacter playerCharacter;
    private KeyPress key;
    private MouseClicks mouse;
    private boolean leftMouseDown;
    private boolean faceLeft;

    /**
     * Construct PlayerInput.
     *
     * @param game GandalfRoyale game instance
     * @param playerCharacter character who's input is read
     */
    public PlayerInput(GandalfRoyale game, PlayerCharacter playerCharacter) {
        this.game = game;
        this.playerCharacter = playerCharacter;
    }

    /**
     * Detect what key is pressed down.
     *
     * @param keycode keycode
     * @return false
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                key = new KeyPress(KeyPress.Action.UP, true);
                break;
            case Input.Keys.A:
                key = new KeyPress(KeyPress.Action.LEFT, true);
                break;
            case Input.Keys.S:
                key = new KeyPress(KeyPress.Action.DOWN, true);
                break;
            case Input.Keys.D:
                key = new KeyPress(KeyPress.Action.RIGHT, true);
                break;
            case Input.Keys.F:
                key = new KeyPress(KeyPress.Action.INTERACT, true);
                break;
            case Input.Keys.NUM_1:
                playerCharacter.setSelectedSlot(1);
                break;
            case Input.Keys.NUM_2:
                playerCharacter.setSelectedSlot(2);
                break;
            case Input.Keys.NUM_3:
                playerCharacter.setSelectedSlot(3);
                break;
            default:
                break;
        }
        if (!Objects.equals(key, null)) {
            // Send LEFT to server
            game.nc.sendUDP(key);
            // Send LEFT to client
            playerCharacter.setMovement(key);
            key = null;
        }
        return false;
    }

    /**
     * Detect what key is realised.
     *
     * @param keycode keycode
     * @return false
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                key = new KeyPress(KeyPress.Action.UP, false);
                break;
            case Input.Keys.S:
                key = new KeyPress(KeyPress.Action.DOWN, false);
                break;
            case Input.Keys.A:
                key = new KeyPress(KeyPress.Action.LEFT, false);
                break;
            case Input.Keys.D:
                key = new KeyPress(KeyPress.Action.RIGHT, false);
                break;
            case Input.Keys.F:
                key = new KeyPress(KeyPress.Action.INTERACT, false);
                break;
            default:
                break;
        }
        if (!Objects.equals(key, null)) {
            // Send LEFT to server
            game.nc.sendUDP(key);
            // Send LEFT to client
            playerCharacter.setMovement(key);
            key = null;
        }
        return false;
    }

    /**
     * IGNORED.
     *
     * @param keyTyped ignored
     * @return false
     */
    @Override
    public boolean keyTyped(char keyTyped) {
        return false;
    }

    /**
     * Mouse pressed.
     *
     * @param screenX x coordinate
     * @param screenY y coordinate
     * @param pointer ignored
     * @param button what button is pressed
     * @return false
     */
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        if (button == Buttons.LEFT) {
            // Get the character's position
            Vector2 characterPositionOnScreen = new Vector2((float) windowWidth / 2, (float) windowHeight / 2);

            // Create a vector representing the mouse position
            Vector2 mousePosition = new Vector2(screenX, screenY);

            // Subtract the character's position from the mouse position to get the relative position
            Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);

            leftMouseDown = true;
            mouse = new MouseClicks(SpellTypes.FIREBALL, true,
                    relativeMousePosition.x, relativeMousePosition.y);
            leftMouseDown = false;
            game.nc.sendUDP(mouse);
        }
        return false;
    }

    /**
     * IGNORED.
     *
     * @param screenX ignored
     * @param screenY ignored
     * @param pointer ignored
     * @param button ignored
     * @return false
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Update mouse coordinates.
     *
     * @param xMousePosition x coordinate
     * @param yMousePosition y coordinate
     * @return false
     */
    @Override
    public boolean mouseMoved(int xMousePosition, int yMousePosition) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        // Get the character's position
        Vector2 characterPositionOnScreen = new Vector2((float) windowWidth / 2, (float) windowHeight / 2);

        // Create a vector representing the mouse position
        Vector2 mousePosition = new Vector2(xMousePosition, yMousePosition);

        // Subtract the character's position from the mouse position to get the relative position
        Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);

        mouse = new MouseClicks(SpellTypes.NOTHING, leftMouseDown,
                relativeMousePosition.x, relativeMousePosition.y);
        game.nc.sendUDP(mouse);
        return false;
    }

    /**
     * IGNORED.
     *
     * @param i ignored
     * @param i1 ignored
     * @param i2 ignored
     * @param i3 ignored
     * @return false
     */
    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    /**
     * IGNORED.
     *
     * @param i ignored
     * @param i1 ignored
     * @param i2 ignored
     * @return false
     */
    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    /**
     * Change selected inventory slot with scrolling.
     *
     * @param amountX ignored
     * @param amountY positive if scrolled up, negative if scrolled down
     * @return true
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY < 0) { // Scrolling up
            if (playerCharacter.getSelectedSlot() != 3) {
                playerCharacter.setSelectedSlot(playerCharacter.getSelectedSlot() + 1);
            }
        } else if (amountY > 0) { // Scrolling down
            if (playerCharacter.getSelectedSlot() != 1) {
                playerCharacter.setSelectedSlot(playerCharacter.getSelectedSlot() -1);
            }
        }
        return true;
    }
}
package ee.taltech.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.messages.KeyPress;
import ee.taltech.network.messages.MouseClicks;
import ee.taltech.screen.screens.GameScreen;
import ee.taltech.utilities.Lobby;

import java.util.Objects;

public class PlayerInput implements InputProcessor {
    private final GameScreen gameScreen;
    private GandalfRoyale game;
    private PlayerCharacter playerCharacter;
    private KeyPress key;
    private MouseClicks mouse;
    private boolean leftMouseDown;
    private boolean faceLeft;
    private OrthographicCamera camera = new OrthographicCamera();


    public PlayerInput(GandalfRoyale game, PlayerCharacter playerCharacter, GameScreen gameScreen) {
        this.game = game;
        this.playerCharacter = playerCharacter;
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                key = new KeyPress(KeyPress.Direction.UP, true);
                break;
            case Input.Keys.A:
                key = new KeyPress(KeyPress.Direction.LEFT, true);
                break;
            case Input.Keys.S:
                key = new KeyPress(KeyPress.Direction.DOWN, true);
                break;
            case Input.Keys.D:
                key = new KeyPress(KeyPress.Direction.RIGHT, true);
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

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                key = new KeyPress(KeyPress.Direction.UP, false);
                break;
            case Input.Keys.S:
                key = new KeyPress(KeyPress.Direction.DOWN, false);
                break;
            case Input.Keys.A:
                key = new KeyPress(KeyPress.Direction.LEFT, false);
                break;
            case Input.Keys.D:
                key = new KeyPress(KeyPress.Direction.RIGHT, false);
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

    @Override
    public boolean keyTyped(char keyTyped) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        if (button == Buttons.LEFT) {
            // Get the character's position
            Vector2 characterPositionOnScreen = new Vector2(windowWidth / 2, windowHeight / 2);

            // Create a vector representing the mouse position
            Vector2 mousePosition = new Vector2(screenX, screenY);

            // Subtract the character's position from the mouse position to get the relative position
            Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);

            leftMouseDown = true;
            mouse = new MouseClicks(MouseClicks.Spell.FIREBALL, leftMouseDown, relativeMousePosition.x, relativeMousePosition.y);
            leftMouseDown = false;
            game.nc.sendUDP(mouse);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    // Mouse positions
    @Override
    public boolean mouseMoved(int xMousePosition, int yMousePosition) {
        int windowHeight = Gdx.graphics.getHeight();
        int windowWidth = Gdx.graphics.getWidth();
        // Get the character's position
        Vector2 characterPositionOnScreen = new Vector2(windowWidth / 2, windowHeight / 2);

        // Create a vector representing the mouse position
        Vector2 mousePosition = new Vector2(xMousePosition, yMousePosition);

        // Subtract the character's position from the mouse position to get the relative position
        Vector2 relativeMousePosition = new Vector2(mousePosition).sub(characterPositionOnScreen);

        mouse = new MouseClicks(MouseClicks.Spell.FIREBALL, leftMouseDown, relativeMousePosition.x, relativeMousePosition.y);
        game.nc.sendUDP(mouse);
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

}
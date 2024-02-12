package ee.taltech.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.messages.KeyPress;

public class PlayerInput implements InputProcessor {

    private GandalfRoyale game;
    private PlayerCharacter playerCharacter;

    private KeyPress key;

    public PlayerInput(GandalfRoyale game, PlayerCharacter playerCharacter) {
        this.game = game;
        this.playerCharacter = playerCharacter;
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
        // Send LEFT to server
        game.nc.sendUDP(key);
        // Send LEFT to client
        playerCharacter.setMovement(key);
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
        // Send LEFT to server
        game.nc.sendUDP(key);
        // Send LEFT to client
        playerCharacter.setMovement(key);
        return false;
    }

    @Override
    public boolean keyTyped(char keyTyped) {
        return false;
    }

    @Override
    public boolean touchDown(int xPressPosition, int yPressPosition, int i2, int mouseButton) {
        // mouseButton gives an int of given button press on the mouse
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
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
    public boolean mouseMoved(int xMousePosition, int yMousePosition) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

}
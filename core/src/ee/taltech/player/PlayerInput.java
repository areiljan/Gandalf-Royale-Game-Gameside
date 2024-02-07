package ee.taltech.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.messages.KeyPress;

public class PlayerInput implements InputProcessor {

    private GandalfRoyale game;

    public PlayerInput(GandalfRoyale game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                System.out.println("LEFT");
                // Send LEFT to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.LEFT, true));
                break;
            case Input.Keys.D:
                System.out.println("RIGHT");
                // Send RIGHT to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.RIGHT, true));
                break;
            case Input.Keys.S:
                System.out.println("DOWN");
                // Send DOWN to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.DOWN, true));
                break;
            case Input.Keys.W:
                System.out.println("UP");
                // Send UP to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.UP, true));
                break;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                System.out.println("Unpressed LEFT");
                // Send LEFT to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.LEFT, false));
                break;
            case Input.Keys.D:
                System.out.println("Unpressed RIGHT");
                // Send RIGHT to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.RIGHT, false));
                break;
            case Input.Keys.S:
                System.out.println("Unpressed DOWN");
                // Send DOWN to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.DOWN, false));
                break;
            case Input.Keys.W:
                System.out.println("Unpressed UP");
                // Send UP to server
                game.nc.sendUDP(new KeyPress(KeyPress.Direction.UP, false));
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char keyTyped) {
        return false;
    }

    @Override
    public boolean touchDown(int xPressPosition, int yPressPosition, int i2, int mouseButton) {
        // mouseButton gives an int of given button press on the mouse
        System.out.println(xPressPosition + ":" + yPressPosition + ":" + i2 + ":" + mouseButton);
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
        // y-axis Mouse Position is inverted, because top-left corner is (0,0).
        // game.nc.sendUDP(xMousePosition + "," + -yMousePosition);
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
package ee.taltech.gandalf.network.messages.game;

public class KeyPress {
    public enum Action {
        UP, DOWN, LEFT, RIGHT, INTERACT
    }
    public Action action;
    public boolean pressed;

    /**
     * Empty constructor for Kryonet.
     */
    public KeyPress() {
    }

    /**
     * Construct key press message.
     *
     * @param action where player wants to move
     * @param pressed if key was pressed or realised
     */
    public KeyPress(Action action, boolean pressed) {
        this.action = action;
        this.pressed = pressed;
    }
}

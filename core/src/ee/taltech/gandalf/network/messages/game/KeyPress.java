package ee.taltech.gandalf.network.messages.game;

public class KeyPress {
    public enum Action {
        UP, DOWN, LEFT, RIGHT, INTERACT, DROP
    }
    public Action action;
    public boolean pressed;
    public Integer extraField;

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

    public KeyPress(Action action, boolean pressed, Integer extraField) {
        this.action = action;
        this.pressed = pressed;
        this.extraField = extraField;
    }
}

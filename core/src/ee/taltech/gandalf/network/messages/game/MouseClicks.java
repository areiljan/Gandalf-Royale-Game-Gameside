package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.SpellTypes;

public class MouseClicks {
    public boolean leftMouse;
    public double mouseXPosition;
    public double mouseYPosition;
    public SpellTypes type;

    public MouseClicks(SpellTypes type, boolean leftMouse, double mouseXPosition, double mouseYPosition) {
        this.leftMouse = leftMouse;
        this.mouseXPosition = mouseXPosition;
        this.mouseYPosition = mouseYPosition;
        this.type = type;
    }
}

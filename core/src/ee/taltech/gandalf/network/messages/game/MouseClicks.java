package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.ItemTypes;

public class MouseClicks {
    public boolean leftMouse;
    public double mouseXPosition;
    public double mouseYPosition;
    public ItemTypes type;

    public MouseClicks(ItemTypes type, boolean leftMouse, double mouseXPosition, double mouseYPosition) {
        this.leftMouse = leftMouse;
        this.mouseXPosition = mouseXPosition;
        this.mouseYPosition = mouseYPosition;
        this.type = type;
    }
}

package ee.taltech.gandalf.network.messages.game;

import ee.taltech.gandalf.components.ItemTypes;

public class MouseClicks {
    public boolean leftMouse;
    public double mouseXPosition;
    public double mouseYPosition;
    public ItemTypes type;
    public Integer extraField;

    /**
     * Empty constructor for Kryonet.
     */
    public MouseClicks() {
    }

    /**
     * Construct MouseClick.
     *
     * @param type type of item that is clicked
     * @param leftMouse if left mouse true on not
     * @param mouseXPosition mouse x coordinate
     * @param mouseYPosition mouse y coordinate
     */
    public MouseClicks(ItemTypes type, boolean leftMouse, double mouseXPosition, double mouseYPosition) {
        this.leftMouse = leftMouse;
        this.mouseXPosition = mouseXPosition;
        this.mouseYPosition = mouseYPosition;
        this.type = type;
    }

    /**
     * Construct MouseClick.
     *
     * @param type type of item that is clicked
     * @param leftMouse if left mouse true on not
     * @param mouseXPosition mouse x coordinate
     * @param mouseYPosition mouse y coordinate
     * @param extraField extra field if item's id need's to be passed as well
     */
    public MouseClicks(ItemTypes type, boolean leftMouse, double mouseXPosition, double mouseYPosition, Integer extraField) {
        this.leftMouse = leftMouse;
        this.mouseXPosition = mouseXPosition;
        this.mouseYPosition = mouseYPosition;
        this.type = type;
        this.extraField = extraField;
    }
}

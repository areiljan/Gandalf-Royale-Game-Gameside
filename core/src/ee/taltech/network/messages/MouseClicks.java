package ee.taltech.network.messages;

public class MouseClicks {
    public enum Spell {
        NOTHING, FIREBALL
    }
    public boolean leftMouse;
    public double mouseXPosition;
    public double mouseYPosition;
    public Spell spell;

    public MouseClicks(Spell spell, boolean leftMouse, double mouseXPosition, double mouseYPosition) {
        this.leftMouse = leftMouse;
        this.mouseXPosition = mouseXPosition;
        this.mouseYPosition = mouseYPosition;
        this.spell = spell;
    }
}

package ee.taltech.player;

import ee.taltech.gandalf.GandalfRoyale;

public class PlayerCharacter {
    int x, y;
    boolean leftMove, rightMove, upMove, downMove;
    int serverAssignedID;
    GandalfRoyale game;

    public PlayerCharacter(GandalfRoyale game) {
        this.game = game;
    }

    public void updateMotion() {
    }

}
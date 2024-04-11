package ee.taltech.gandalf.entities;

public class PlayZone {
    private int radius;


    public PlayZone() {
        this.radius = 5000;
    }

    public int getRadius() {
        return radius;
    }

    public void shrinkPlayZone(int radius) {
        System.out.println("New radius is: " + radius);
        this.radius = radius;
    }
}

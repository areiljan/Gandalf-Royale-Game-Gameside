package ee.taltech.gandalf.entities;

public class PlayZone {
    private int timer;
    private int stage;
    public PlayZone() {
        this.timer = 0;
        this.stage = 1;
    }

    public void updateZone(int startTime) {
        timer = startTime;
        if (timer < 30) {
            stage = 8;
        }
//        } else if (timer > 30 && timer < 80) {
//            stage = 2;
//        } else if (timer < 200) {
//            stage = 3;
//            // implement first zone
//            // create body
//        } else if (timer < 250) {
//            stage = 4;
//            // show second zone
//        } else if (timer < 350) {
//            stage = 5;
//            // implement second zone
//            // create body
//        } else if (timer < 450) {
//            stage = 6;
//            // show third zone
//        } else if (timer < 550) {
//            stage = 7;
//            // implement third zone
//            // create body
//            // final countdown
//        } else if (timer < 800) {
//            stage = 8;
//            // the entire map turns red
//            // create body
//        }
    }

    public int getStage() {
        return stage;
    }
}

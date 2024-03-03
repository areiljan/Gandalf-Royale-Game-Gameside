package ee.taltech.objects;

import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.NetworkClient;
import ee.taltech.network.messages.FireballPosition;
import ee.taltech.screen.screens.GameScreen;
import ee.taltech.utilities.Lobby;

import java.util.ArrayList;
import java.util.List;


public class Fireball {
    NetworkClient nc;
    public List<FireballPosition> fireballPositions = new ArrayList<>();
    public Fireball() {
    }

    public void updateFireballPositions(FireballPosition fireballToUpdate) {
        System.out.println("X: " + fireballToUpdate.fireballXPosition + " Y: " + fireballToUpdate.fireballYPosition);

        if (fireballToUpdate != null) {
            if (fireballPositions.isEmpty()) {
                // If the fireballPositions list is empty, simply add the received fireball position
                fireballPositions.add(fireballToUpdate);
            } else {
                // If the fireballPositions list is not empty, check if the received fireball position matches any existing fireball
                boolean found = false;
                for (FireballPosition fireball : fireballPositions) {
                    if (fireball.fireballID == fireballToUpdate.fireballID) {
                        // If a matching fireball is found, update its position
                        int index = fireballPositions.indexOf(fireball);
                        fireballPositions.set(index, fireballToUpdate);
                        found = true;
                        break; // Exit the loop since the fireball is found and updated
                    }
                }
                // If the received fireball position doesn't match any existing fireball, add it to the list
                if (!found) {
                    fireballPositions.add(fireballToUpdate);
                }
            }
        }
    }
}

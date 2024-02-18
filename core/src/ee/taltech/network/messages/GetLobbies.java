package ee.taltech.network.messages;

import java.util.List;

public class GetLobbies {
    public String name; // Game name
    public Integer gameId; // Game ID
    public List<Integer> players; // Players in game

    /**
     * Empty constructor for Kryonet.
     */
    public GetLobbies() {
        // Empty constructor for server to fill.
    }
}

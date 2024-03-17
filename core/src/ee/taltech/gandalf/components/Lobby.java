package ee.taltech.gandalf.components;

import java.util.List;

public class Lobby {
    private String name;
    private Integer id;
    private List<Integer> players;

    /**
     * Construct lobby.
     *
     * @param name given name for lobby by host
     * @param id given ID for lobby by sever
     * @param players players in lobby
     */
    public Lobby(String name, Integer id, List<Integer> players) {
        this.name = name;
        this.id = id;
        this.players = players;
    }

    /**
     * Add player to lobby.
     *
     * @param playerId that will join lobby
     */
    public void addPlayer(Integer playerId) {
        players.add(playerId);
    }

    /**
     * Remove player form lobby.
     *
     * @param playerId that will leave lobby
     */
    public void removePlayer(Integer playerId) {
        players.remove(playerId);
    }

    /**
     * Get lobby's player count.
     *
     * @return player count
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Get lobby's ID.
     *
     * @return lobby's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get lobby's name.
     *
     * @return lobby's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get lobby's host ID.
     *
     * @return host ID
     */
    public Integer getHostId() {
        return players.getFirst();
    }

    /**
     * Get player list.
     *
     * @return player list
     */
    public List<Integer> getPlayers() {
        return players;
    }
}

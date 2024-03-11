package ee.taltech.utilities;

import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.messages.Position;
import ee.taltech.objects.Fireball;
import ee.taltech.player.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StartedGame {

    // Threshold for the server to override the position difference
    private static final Integer OVERWRITE_THRESHOLD = 5;
    private final World world;
    private final GandalfRoyale game;
    private final Map<Integer, PlayerCharacter> alivePlayers;
    private final Map<Integer, PlayerCharacter> deadPlayers;
    private final Integer clientId;
    private final PlayerCharacter clientCharacter;
    private final Map<Integer, Fireball> fireballs;

    /**
     * Construct StartGame.
     *
     * @param game GandalfRoyale game instance
     * @param lobby player ID-s list
     */
    public StartedGame(GandalfRoyale game, Lobby lobby, World world) {
        this.world = world;
        this.game = game;

        alivePlayers = createAlivePlayersMap(lobby);
        deadPlayers = new HashMap<>();
        clientId = game.nc.clientId;
        clientCharacter = alivePlayers.get(game.nc.clientId);
        clientCharacter.createHitBox(world);
        fireballs = new HashMap<>();
    }

    /**
     * Get alive players.
     *
     * @return alivePlayers
     */
    public Map<Integer, PlayerCharacter> getAlivePlayers() {
        return alivePlayers;
    }

    /**
     * Get dead players.
     *
     * @return deadPlayers
     */
    public Map<Integer, PlayerCharacter> getDeadPlayers() {
        return deadPlayers;
    }

    /**
     * Get client character.
     *
     * @return clientCharacter
     */
    public PlayerCharacter getClientCharacter() {
        return clientCharacter;
    }

    public Map<Integer, Fireball> getFireballs() {
        return fireballs;
    }

    /**
     * Create alive players map.
     *
     * @param lobby list of player ID-s
     * @return alivePlayers map
     */
    private Map<Integer, PlayerCharacter> createAlivePlayersMap(Lobby lobby) {
        Map<Integer, PlayerCharacter> map = new HashMap<>();
        for (Integer playerId : lobby.getPlayers()) {
            PlayerCharacter player = new PlayerCharacter(playerId);
            map.put(playerId, player);
        }
        return map;
    }

    /**
     * Remove player from alive players.
     *
     * @param id ID of player that is removed
     */
    public void removePlayerFromAlivePlayers(Integer id) {
        alivePlayers.remove(id);
    }


    /**
     * Update player's health.
     *
     * @param id player's ID
     * @param health player's new health
     */
    public void updatePlayersHealth(Integer id, Integer health) {
        alivePlayers.get(id).setHealth(health);
        if (health == 0) {
            killPlayer(id);
        }
    }

    /**
     * Update player's mana.
     *
     * @param id player's ID
     * @param mana player's new mana
     */
    public void updatePlayersMana(Integer id, double mana) {
        alivePlayers.get(id).setMana(mana);
    }

    /**
     * Check and Overwrite players position if difference is over the threshold.
     */
    public void checkOverwritePlayerPosition(Position position) {
        // Check if there is an incoming Position message and if it matches the clientID
        if (position != null && position.userID == clientId
                // Check for the difference in client prediction and actual server position
                && (Math.abs(clientCharacter.xPosition - position.xPosition) > OVERWRITE_THRESHOLD
                || Math.abs(clientCharacter.yPosition - position.yPosition) > OVERWRITE_THRESHOLD)) {
            // Locates the client according to the server position (override the prediction made on client).
            clientCharacter.setPosition(position.xPosition, position.yPosition);
        }
    }

    /**
     * Method to move enemy players.
     *
     * @param position Message from server, that contains playerID, X, Y coordinates.
     */
    public void movePlayer(Position position) {
        PlayerCharacter enemyPlayer = alivePlayers.get(position.userID);
        enemyPlayer.setPosition(position.xPosition, position.yPosition);
    }

    /**
     * Update fireball positions.
     *
     * @param senderId fireball sender
     * @param xPosition x position
     * @param yPosition y position
     * @param id fireball ID
     */
    public void updateFireballPositions(Integer senderId, double xPosition, double yPosition, Integer id) {
        if (!fireballs.containsKey(id)) {
            fireballs.put(id, new Fireball(senderId, xPosition, yPosition, id, world));
        } else {
           fireballs.get(id).setXPosition(xPosition);
           fireballs.get(id).setYPosition(yPosition);
           fireballs.get(id).setHitBoxPosition(xPosition, yPosition);
        }
    }

    /**
     * Update started game.
     *
     * @param delta time
     */
    public void update(float delta) {
        clientCharacter.updatePosition(); // Update the player position prediction for smoother response.
    }

    /**
     * Kill player.
     *
     * @param id player who is killed
     */
    private void killPlayer(Integer id) {
        PlayerCharacter player = alivePlayers.get(id);
        deadPlayers.put(id, player);

        // Don't read players input if they are dead
        if (Objects.equals(id, clientId)) {
            game.screenController.getGameScreen().disableClientPlayerCharacter();
        }
    }
}

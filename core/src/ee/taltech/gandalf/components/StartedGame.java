package ee.taltech.gandalf.components;

import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.entities.Item;
import ee.taltech.gandalf.entities.Mob;
import ee.taltech.gandalf.entities.PlayerCharacterAnimator;
import ee.taltech.gandalf.entities.Spell;
import ee.taltech.gandalf.entities.*;
import ee.taltech.gandalf.network.messages.game.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StartedGame {
    // Threshold for the server to override the position difference
    private final GandalfRoyale game;
    private final World world;
    private final Map<Integer, PlayerCharacter> gamePlayers;
    private final List<Integer> deadPlayers;
    private final Integer clientId;
    private final PlayerCharacter clientCharacter;
    private final Map<Integer, Spell> spells;
    private final Map<Integer, Item> items;
    private final Map<Integer, Mob> mobs;
    private PlayZone playZone;

    /**
     * Construct StartGame.
     *
     * @param game GandalfRoyale game instance
     * @param lobby player ID-s list
     */
    public StartedGame(GandalfRoyale game, Lobby lobby, World world) {
        this.game = game;
        this.world = world;
        gamePlayers = createAlivePlayersMap(lobby);
        deadPlayers = new ArrayList<>();
        clientId = game.nc.clientId;
        clientCharacter = gamePlayers.get(game.nc.clientId);
        clientCharacter.createHitBox(this.world);

        // To sync putting items to Map and getting them. Creates a synchronization with failsafe.
        spells = new ConcurrentHashMap<>();
        items = new ConcurrentHashMap<>();
        mobs = new ConcurrentHashMap<>();
        this.playZone = null;
    }

    /**
     * Initialize the PlayZone.
     */
    public void initializePlayZone(int firstPlayZoneX, int firstPlayZoneY,
                                   int secondPlayZoneX, int secondPlayZoneY,
                                   int thirdPlayZoneX, int thirdPlayZoneY) {
        this.playZone = new PlayZone(firstPlayZoneX, firstPlayZoneY, secondPlayZoneX, secondPlayZoneY, thirdPlayZoneX, thirdPlayZoneY);
    }

    /**
     * Get the PlayZone.
     */
    public PlayZone getPlayZone() {
        return playZone;
    }

    /**
     * Get alive players.
     *
     * @return alivePlayers
     */
    public Map<Integer, PlayerCharacter> getGamePlayers() {
        return gamePlayers;
    }

    /**
     * Get a player from game by their ID.
     *
     * @param playerId player's ID who is wanted
     * @return player's character
     */
    public PlayerCharacter getPlayer(Integer playerId) {
        return gamePlayers.get(playerId);
    }

    /**
     * Get dead players.
     *
     * @return deadPlayers
     */
    public List<Integer> getDeadPlayers() {
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

    /**
     * Get cast spells in the world.
     *
     * @return spells
     */
    public Map<Integer, Spell> getSpells() {
        synchronized (spells) {
            return new HashMap<>(spells);
        }
    }

    /**
     * Get items that are dropped in the world.
     *
     * @return items
     */
    public Map<Integer, Item> getItems() {
        synchronized (mobs) {
            return new HashMap<>(items);
        }
    }

    /**
     * Get mobs that are in the world.
     *
     * @return mobs
     */
    public Map<Integer, Mob> getMobs() {
        synchronized (mobs) {
            return new HashMap<>(mobs);
        }
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
     * Update player's health.
     *
     * @param id player's ID
     * @param health player's new health
     */
    public void updatePlayersHealth(Integer id, Integer health) {
        gamePlayers.get(id).setHealth(health);
        if (health == 0 && !deadPlayers.contains(id)) {
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
        gamePlayers.get(id).setMana(mana);
    }

    /**
     * Check and Overwrite players position if difference is over the threshold.
     */
    public void checkOverwritePlayerPosition(Position position) {
        // Check if there is an incoming Position message and if it matches the clientID
        if (position != null && position.userID == clientId
                // Check for the difference in client prediction and actual server position
                && (Math.abs(clientCharacter.getXPosition() - position.xPosition) > Constants.MOVEMENT_THRESHOLD
                || Math.abs(clientCharacter.getYPosition() - position.yPosition) > Constants.MOVEMENT_THRESHOLD)) {
            // Locates the client according to the server position (override the prediction made on client).
            float newX = clientCharacter.getXPosition() + (position.xPosition - clientCharacter.getXPosition()) * Constants.INTERP_FACTOR;
            float newY = clientCharacter.getYPosition() + (position.yPosition - clientCharacter.getYPosition()) * Constants.INTERP_FACTOR;
            clientCharacter.setPosition(newX, newY);
        }
    }

    /**
     * Method to move enemy players.
     *
     * @param position Message from server, that contains playerID, X, Y coordinates.
     */
    public void movePlayer(Position position) {
        PlayerCharacter enemyPlayer = gamePlayers.get(position.userID);
        enemyPlayer.setPosition(position.xPosition, position.yPosition);
    }

    /**
     * Update spell positions.
     *
     * @param senderId spell sender
     * @param xPosition x position
     * @param yPosition y position
     * @param id spell ID
     */
    public void updateSpellPositions(Integer senderId, double xPosition, double yPosition,
                                     Integer id, ItemTypes type) {
        synchronized (spells) {
            if (!spells.containsKey(id)) {
                spells.put(id, new Spell(senderId, xPosition, yPosition, id, type));
            } else {
                spells.get(id).setXPosition(xPosition);
                spells.get(id).setYPosition(yPosition);
            }
        }
    }

    /**
     * Get rid of the spell.
     */
    public void removeSpell(Integer id) {
        synchronized (spells) {
            spells.remove(id);
        }
    }

    /**
     * Kill player.
     *
     * @param id player who is killed
     */
    public void killPlayer(Integer id) {
        PlayerCharacter player = gamePlayers.get(id);
        player.getPlayerAnimator().setState(PlayerCharacterAnimator.AnimationStates.DEATH);

        deadPlayers.add(id);
        player.setHealth(0);
        player.setMana(0);
    }

    /**
     * Add item to world aka add dropped item on the ground.
     *
     * @param item item that is added
     */
    public void addItem(Item item) {
        synchronized (items) {
            items.put(item.getId(), item);
        }
    }

    /**
     * Remove item from the world aka pick up item form the ground.
     *
     * @param itemId item's id that is removed
     * @return removed item
     */
    public Item removeItem(Integer itemId) {
        synchronized (items) {
            Item removedItem = items.get(itemId);
            items.remove(itemId);
            return removedItem;
        }
    }

    /**
     * Add new mob to the game.
     *
     * @param mob mob that is added
     */
    public void addMob(Mob mob) {
        synchronized (mobs) {
            mobs.put(mob.getId(), mob);
        }
    }

    /**
     * Remove mob from the game.
     *
     * @param mobId mob's id who is removed
     */
    public void removeMob(Integer mobId) {
        synchronized (mobs) {
            mobs.remove(mobId);
        }
    }

    /**
     * Update started game.
     *
     * @param delta time
     */
    public void update(float delta) {
        clientCharacter.updateVector();
        clientCharacter.updatePosition();
    }
}

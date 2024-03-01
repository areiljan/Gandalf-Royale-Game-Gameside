package ee.taltech.screen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.NetworkClient;
import ee.taltech.network.messages.Position;
import ee.taltech.player.PlayerCharacter;
import ee.taltech.player.PlayerInput;
import ee.taltech.utilities.Lobby;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter {

    GandalfRoyale game;
    NetworkClient nc;
    Texture img;
    Texture opponentImg;
    Lobby lobby;
    private Map<Integer, PlayerCharacter> alivePlayers;

    // Threshold for the server to override the position difference
    private static final Integer OVERWRITE_THRESHOLD = 5;
    public PlayerCharacter playerCharacter;

    // Used ONLY for static background image (TEMPORARY)
    private static final Texture BACKGROUND_TEXTURE = new Texture("game.png"); // Background image
    private static final Sprite BACKGROUND_SPRITE = new Sprite(BACKGROUND_TEXTURE); // Background sprite made from image
    private final ShapeRenderer shapeRenderer;

    /**
     * Construct GameScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public GameScreen(GandalfRoyale game, Lobby lobby) {
        this.game = game;
        this.nc = game.nc;
        this.lobby = lobby;
        alivePlayers = new HashMap<>();
        for (Integer playerId : lobby.getPlayers()) {
            alivePlayers.put(playerId, new PlayerCharacter(playerId));
        }
        playerCharacter = alivePlayers.get(this.nc.clientId); // Create the client character object.
        game.nc.addGameListeners();
        shapeRenderer = new ShapeRenderer();
    }

    /**
     * Check and Overwrite players position if difference is over the threshold.
     */
    public void checkOverwritePlayerPosition(Position position) {
        // Check if there is an incoming Position message and if it matches the clientID
        if (position != null && position.userID == this.nc.clientId
                // Check for the difference in client prediction and actual server position
                && (Math.abs(playerCharacter.xPosition - position.xPosition) > OVERWRITE_THRESHOLD
                || Math.abs(playerCharacter.yPosition - position.yPosition) > OVERWRITE_THRESHOLD)) {
            // Locates the client according to the server position (override the prediction made on client).
            playerCharacter.setPosition(position.xPosition, position.yPosition);
        }
    }

    /**
     * Draw player character to screen.
     */
    private void drawPlayer() {
        // Set the image to 3 times smaller picture and flip it, if player is moving left.
        game.batch.begin();
        game.batch.draw(img, playerCharacter.xPosition, playerCharacter.yPosition,
                (float) img.getWidth() / 3, (float) img.getHeight() / 3, 0, 0,
                img.getWidth(), img.getHeight(), playerCharacter.moveLeft, false);
        game.batch.end();

        // Draw health and mana bar
        drawBars(playerCharacter);
    }

    /**
     * Draw the enemy wizard player.
     */
    private void drawEnemy() {
        for (PlayerCharacter enemyPlayer : alivePlayers.values()) {
            if (enemyPlayer.playerID != nc.clientId) {
                // Draw enemy player
                game.batch.begin();
                game.batch.draw(img, enemyPlayer.xPosition, enemyPlayer.yPosition,
                        (float) img.getWidth() / 3, (float) img.getHeight() / 3, 0, 0,
                        img.getWidth(), img.getHeight(), enemyPlayer.moveLeft, false);
                game.batch.end();

                // Draw health and mana bar
                drawBars(enemyPlayer);
            }
        }
    }

    /**
     * Draw health and mana bar for player.
     *
     * @param player any PlayerCharacter
     */
    private void drawBars(PlayerCharacter player) {
        // Render shapes
        shapeRenderer.setProjectionMatrix(game.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw missing health bar
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3 + 10,
                100 * 2, 5);

        // Draw health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3 + 10,
                player.health * 2, 5);

        // Draw missing mana bar
        shapeRenderer.setColor(Color.NAVY);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3,
                100 * 2, 5);

        // Draw mana bar
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3,
                player.mana * 2, 5);

        // Stop rendering shapes
        shapeRenderer.end();
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
     * Update player's health.
     *
     * @param id player's ID
     * @param health player's new health
     */
    public void updatePlayersHealth(Integer id, Integer health) {
        alivePlayers.get(id).setHealth(health);
    }

    /**
     * Update player's mana.
     *
     * @param id player's ID
     * @param mana player's new mana
     */
    public void updatePlayersMana(Integer id, Integer mana) {
        alivePlayers.get(id).setMana(mana);
    }

    /**
     * Show screen on initialisation.
     */
    @Override
    public void show() {
        img = new Texture("wizard.png");
        opponentImg = new Texture("opponent.png");
        Gdx.input.setInputProcessor(new PlayerInput(game, playerCharacter)); // Start listening to custom inputs.
    }

    /**
     * Render the screen aka update every frame.
     *
     * @param delta time
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        playerCharacter.updatePosition(); // Update the player position prediction for smoother response.

        // Update camera position to follow player
        game.camera.position.x = playerCharacter.xPosition;
        game.camera.position.y = playerCharacter.yPosition;

        // Update camera matrices
        game.camera.update();

        // Set camera projection matrix
        game.batch.setProjectionMatrix(game.camera.combined);

        // Render game objects
        game.batch.begin();
        BACKGROUND_SPRITE.draw(game.batch); // Used ONLY for static background image (TEMPORARY)
        game.batch.end();

        drawPlayer(); // Draw client character
        drawEnemy(); // Draw enemy wizards.
    }

    /**
     * Hide screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        img.dispose();
        opponentImg.dispose();
    }
}

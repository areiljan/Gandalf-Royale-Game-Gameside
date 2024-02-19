package ee.taltech.screen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.NetworkClient;
import ee.taltech.network.messages.Position;
import ee.taltech.player.PlayerCharacter;
import ee.taltech.player.PlayerInput;
import ee.taltech.utilities.Lobby;

public class GameScreen extends ScreenAdapter {

    GandalfRoyale game;
    NetworkClient nc;
    Texture img;
    Texture opponentImg;
    Lobby lobby;

    // Threshold for the server to override the position difference
    private static final Integer OVERWRITE_THRESHOLD = 5;

    public PlayerCharacter playerCharacter;

    /**
     * Construct GameScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public GameScreen(GandalfRoyale game, Lobby lobby) {
        this.game = game;
        this.nc = game.nc;
        this.lobby = lobby;
        playerCharacter = new PlayerCharacter(this.nc.clientId); // Create the client character object.
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
        game.batch.draw(img, playerCharacter.xPosition, playerCharacter.yPosition,
                (float) img.getWidth() / 3, (float) img.getHeight() / 3, 0, 0,
                img.getWidth(), img.getHeight(), playerCharacter.moveLeft, false);
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
        game.batch.begin();
        drawPlayer(); // Draw client character
        game.batch.end();
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

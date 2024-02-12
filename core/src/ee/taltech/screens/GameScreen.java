package ee.taltech.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.NetworkClient;
import ee.taltech.player.PlayerCharacter;
import ee.taltech.player.PlayerInput;

import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter {

    GandalfRoyale game;
    NetworkClient nc;
    Texture img;
    Texture opponentImg;

    public PlayerCharacter playerCharacter;

    public GameScreen(GandalfRoyale game) {
        this.game = game;
        this.nc = game.nc;
        nc.addListener(); // Add NetworkClient listener, that listens for the messages from the server.
        playerCharacter = new PlayerCharacter(this.nc.clientId); // Create the client character object.
    }

    @Override
    public void show() {
        img = new Texture("wizard.png");
        opponentImg = new Texture("opponent.png");
        Gdx.input.setInputProcessor(new PlayerInput(game, playerCharacter)); // Start listening to custom inputs.
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        playerCharacter.updatePosition(); // Update the player position prediction for smoother response.
        checkOverwritePlayerPosition(); // Check if server needs to overwrite client position prediction.
        game.batch.begin();
        drawPlayer(); // Draw client character
        game.batch.end();
    }

    public void checkOverwritePlayerPosition() {
        // Set the threshold for the server to override the position difference
        int threshold = 5;
        // Check if there is an incoming Position message and if it matches the clientID
        if (this.nc.receivedPosition != null && this.nc.receivedPosition.userID == this.nc.clientId &&
                // Check for the difference in client prediction and actual server position
                (Math.abs(playerCharacter.xPosition - this.nc.receivedPosition.xPosition) > threshold
                        || Math.abs(playerCharacter.yPosition - this.nc.receivedPosition.yPosition) > threshold)) {
            // Locates the client according to the server position (override the prediction made on client).
            playerCharacter.setPosition(this.nc.receivedPosition.xPosition, this.nc.receivedPosition.yPosition);
        }
    }

    private void drawPlayer() {
        // Set the image to 3 times smaller picture and flip it, if player is moving left.
        game.batch.draw(img, playerCharacter.xPosition, playerCharacter.yPosition,
                (float) img.getWidth() / 3, (float) img.getHeight() / 3, 0, 0, img.getWidth(), img.getHeight(),
                playerCharacter.moveLeft, false);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        img.dispose();
        opponentImg.dispose();
    }
}

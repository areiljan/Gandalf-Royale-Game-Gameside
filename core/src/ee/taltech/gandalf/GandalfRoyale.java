package ee.taltech.gandalf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.network.NetworkClient;
import ee.taltech.screens.MenuScreen;

public class GandalfRoyale extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public NetworkClient nc;
    public MenuScreen menuScreen;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        menuScreen = new MenuScreen(this); // Creating MenuScreen

        try {
            nc = new NetworkClient();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        setScreen(menuScreen); // Setting screen to MenuScreen
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

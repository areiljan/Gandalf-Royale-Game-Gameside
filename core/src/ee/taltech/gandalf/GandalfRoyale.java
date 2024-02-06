package ee.taltech.gandalf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.network.NetworkClient;
import ee.taltech.screens.MenuScreen;
import jdk.internal.net.http.common.ConnectionExpiredException;

public class GandalfRoyale extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public NetworkClient nc;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        try {
            nc = new NetworkClient();
        } catch (ConnectionExpiredException e) {
            throw new RuntimeException(e);
        }
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

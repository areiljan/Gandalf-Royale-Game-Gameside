package ee.taltech.gandalf;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import ee.taltech.gandalf.network.NetworkClient;
import ee.taltech.gandalf.screens.ScreenController;

public class GandalfRoyale extends Game {
    public static BitmapFont font;
    public SpriteBatch batch;
    public NetworkClient nc;

    public ScreenController screenController() {
        return screenController;
    }

    public ScreenController screenController;

    /**
     * Create game instance.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/WizardWorldSimplified.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);
        generator.dispose();


        screenController = new ScreenController(this);

        try {
            nc = new NetworkClient(screenController);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        screenController.setMenuScreen(); // Set MenuScreen as the first screen.
    }

    /**
     * Dispose game.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }


}

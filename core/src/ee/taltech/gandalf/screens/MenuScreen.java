package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.taltech.gandalf.GandalfRoyale;


public class MenuScreen extends ScreenAdapter {

    GandalfRoyale game;
    Stage stage;
    Table root;
    TextButton buttonPlay;
    TextButton buttonSettings;
    TextButton buttonExit;

    // Background picture constants
    private static final Texture BACKGROUND_TEXTURE = new Texture("menuBackground.png"); // Background image

    /**
     * Construct MenuScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public MenuScreen(GandalfRoyale game) {
        this.game = game;

        stage = new Stage(game.viewport, game.batch); // Creating a stage (place, where things can be put on)

        root = new Table(); // Creating root table that covers the whole stage
        root.top(); // Starts from the top of the screen
        root.setFillParent(true); // Fill the stage
        stage.addActor(root); // Add root to stage (only actor on stage)

        createUI(); // Create visual part (stated only in constructor)
        setupListeners(); // Set up listeners for buttons (stated only in constructor)
    }

    /**
     * Create visual part of the MenuScreen.
     */
    private void createUI() {
        // Creating a table (place, where things can be put in and positioned)
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Styling heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(GandalfRoyale.font, Color.FIREBRICK);

        // Styling buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.getDrawable("button.up")
//        textButtonStyle.down = skin.getDrawable("button.down")
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = game.font;
        textButtonStyle.fontColor = Color.BLACK;

        // Creating heading
        Label heading = new Label("GandalfRoyale", headingStyle);
        heading.setFontScale(3);

        // Creating Play button
        buttonPlay = new TextButton("Play", textButtonStyle);
        buttonPlay.pad(20);
        buttonPlay.getLabel().setFontScale(2);

        // Creating Settings button
        buttonSettings = new TextButton("Settings", textButtonStyle);
        buttonSettings.pad(20);
        buttonSettings.getLabel().setFontScale(2);

        // Creating Exit button
        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.pad(20);
        buttonExit.getLabel().setFontScale(2);

        // Creating credits (creator names) label
        Label credits = new Label("*Artur_Reiljan*Rasmus_Kilkson*Ramus_Raasuke*", headingStyle);

        // Adding elements to the Table
        table.add(heading);
        table.row();
        table.padBottom(50);
        table.add(buttonPlay);
        table.row();
        table.add(buttonSettings);
        table.row();
        table.add(buttonExit);
        table.row();
        table.add(credits);

        // Adding the Table to the root table that is on stage
        root.add(table);
    }

    /**
     * Set up listeners for buttons to work.
     */
    private void setupListeners() {

        // Play button functionality on click
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.screenController.setLobbyScreen(); // Change screen to LobbyScreen
            }
        });

        // Settings button functionality on click
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new SettingsScreen(game)); // Change screen to SettingsScreen
            }
        });

        // Exit button functionality on click
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Close the whole application
            }
        });
    }

    /**
     * Show screen on initialisation.
     */
    @Override
    public void show() {
        // Reading input
        Gdx.input.setInputProcessor(stage);
        game.batch.setProjectionMatrix(game.viewport.getCamera().projection);
    }

    /**
     * Render the screen aka update every frame.
     *
     * @param delta time
     */
    @Override
    public void render(float delta) {
        // Draw background picture
        game.batch.begin();
        game.batch.draw(BACKGROUND_TEXTURE, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Update stage every frame
        stage.act(delta);
        stage.draw();
    }

    /**
     * Resize the viewport.
     *
     * @param width new width
     * @param height new height
     */
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    /**
     * Hide screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Dispose screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

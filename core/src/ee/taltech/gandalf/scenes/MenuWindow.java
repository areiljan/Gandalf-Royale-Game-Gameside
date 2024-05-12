package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.network.messages.game.GameLeave;


public class MenuWindow {
    private final GandalfRoyale game;

    private final Stage stage;
    private final Table root;
    private TextButton buttonSettings;
    private TextButton buttonExit;

    public MenuWindow(GandalfRoyale game) {
        this.game = game;

        ScreenViewport viewport = new ScreenViewport();

        stage = new Stage(viewport); // Create stage where everything is

        root = new Table(); // Root table that helps to position items on screen
        root.center(); // Put root to center
        root.setFillParent(true); // Fill the whole stage

        stage.addActor(root); // Put root table to stage

        createUI();
        setupListeners();

        stage.setDebugAll(true);
    }

    /**
     * Get menu window's stage.
     *
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Create visual part of the MenuScreen.
     */
    private void createUI() {
        // Creating a table (place, where things can be put in and positioned)
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Styling buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = GandalfRoyale.font;
        textButtonStyle.fontColor = Color.BLACK;

        // Creating Settings button
        buttonSettings = new TextButton("Settings", textButtonStyle);
        buttonSettings.pad(20);
        buttonSettings.getLabel().setFontScale(2);

        // Creating Exit button
        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.pad(20);
        buttonExit.getLabel().setFontScale(2);


        // Adding elements to the Table
        table.add(buttonSettings);
        table.row();
        table.add(buttonExit);

        // Adding the Table to the root table that is on stage
        root.add(table);
    }

    /**
     * Set up listeners for buttons to work.
     */
    private void setupListeners() {
        // Settings button functionality on click
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        // Exit button functionality on click
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.nc.sendTCP(new GameLeave(game.nc.clientId)); // Send server a messages that player left
                game.screenController.setMenuScreen(); // Put player back to Menu screen
            }
        });
    }

    /**
     * Draw MenuWindow.
     */
    public void draw() {
        stage.act();
        stage.draw();
    }

    /**
     * Resize MenuWindow.
     *
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Dispose menu window.
     */
    public void dispose() {
        stage.dispose();
    }
}

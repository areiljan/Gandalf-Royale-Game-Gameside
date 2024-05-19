package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.screens.GameScreen;
import ee.taltech.gandalf.screens.MenuScreen;

import java.util.HashMap;
import java.util.Map;

public class SettingsWindow implements InputProcessor {
    private final GandalfRoyale game;

    private Map<String, Integer> keyBindings;

    private final Stage stage;
    private final Table root;

    private Label up;
    private Label down;
    private Label left;
    private Label right;
    private Label pickAndDrop;

    private TextButton backButton;

    private String editingAction;

    private final InputMultiplexer inputMultiplexer;

    /**
     * Construct SettingsWindow for menu screen.
     *
     * @param game game
     */
    public SettingsWindow(GandalfRoyale game) {
        this.game = game;

        // Create default keymap
        keyBindings = new HashMap<>();
        keyBindings.put("UP", Input.Keys.W);
        keyBindings.put("DOWN", Input.Keys.S);
        keyBindings.put("LEFT", Input.Keys.A);
        keyBindings.put("RIGHT", Input.Keys.D);
        keyBindings.put("PICK", Input.Keys.F);

        ScreenViewport viewport = new ScreenViewport();

        stage = new Stage(viewport); // Create stage where everything is

        root = new Table(); // Root table that helps to position items on screen
        root.center(); // Put root to center
        root.setFillParent(true); // Fill the whole stage

        stage.addActor(root); // Put root table to stage

        // Set up input processor
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);

        createUI();
        setupListeners();

        editingAction = null;
    }

    /**
     * Get input processor.
     *
     * @return inputMultiplexer
     */
    public InputProcessor getInput() {
        return inputMultiplexer;
    }

    /**
     * Get key bindings map.
     *
     * @return keybindings
     */
    public Map<String, Integer> getKeyBindings() {
        return keyBindings;
    }

    /**
     * Create visual part of the SettingsWindow.
     */
    private void createUI() {
        // Creating a table (place, where things can be put in and positioned)
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Styling labels
        Label.LabelStyle labelStyle = new Label.LabelStyle(GandalfRoyale.font, Color.BLACK);

        // Styling buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = GandalfRoyale.font;
        textButtonStyle.fontColor = Color.BLACK;

        // Create labels
        up = new Label("Move up:  " + Input.Keys.toString(keyBindings.get("UP")), labelStyle);
        down = new Label("Move down:  " + Input.Keys.toString(keyBindings.get("DOWN")), labelStyle);
        left = new Label("Move left:  " + Input.Keys.toString(keyBindings.get("LEFT")), labelStyle);
        right = new Label("Move right:  " + Input.Keys.toString(keyBindings.get("RIGHT")), labelStyle);
        pickAndDrop = new Label("Pick up / drop:  " + Input.Keys.toString(keyBindings.get("PICK")), labelStyle);

        // Create tips
        Label tips = new Label("ESC for cancel", labelStyle);

        // Create back button
        backButton = new TextButton("Back", textButtonStyle);

        // Make the font bigger
        up.setFontScale(2);
        down.setFontScale(2);
        left.setFontScale(2);
        right.setFontScale(2);
        pickAndDrop.setFontScale(2);
        tips.setFontScale(2);
        backButton.getLabel().setFontScale(2);

        // Slap all the elements on the table
        table.row();
        table.add(up);
        table.row();
        table.add(down);
        table.row();
        table.add(left);
        table.row();
        table.add(right);
        table.row();
        table.add(pickAndDrop);
        table.row();
        table.add(tips).pad(40);
        table.row();
        table.add(backButton);


        // Adding the Table to the root table that is on stage
        root.add(table);
    }

    /**
     * Set up listeners for buttons to work.
     */
   private void setupListeners() {
       // Up label click
       up.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
              editingAction = "UP";
              up.setFontScale(2.3f);
          }
       });

       // Down label click
       down.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               editingAction = "DOWN";
               down.setFontScale(2.3f);
           }
       });

       // Left label click
       left.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               editingAction = "LEFT";
               left.setFontScale(2.3f);
           }
       });

       // Right label click
       right.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               editingAction = "RIGHT";
               right.setFontScale(2.3f);
           }
       });

       // Pick and drop label click
       pickAndDrop.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               editingAction = "PICK";
               pickAndDrop.setFontScale(2.3f);
           }
       });

       // Back button click
       backButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               ScreenAdapter screen = game.screenController.getCurrentScreen();
               if (screen instanceof MenuScreen menuScreen) {
                   menuScreen.toggleSettingsWindow();
               } else if (screen instanceof GameScreen gameScreen) {
                   gameScreen.toggleSettingsWindow();
               }
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

    @Override
    public boolean keyDown(int keycode) {
        if (editingAction != null && keycode != Input.Keys.ESCAPE) {
            keyBindings.put(editingAction, keycode); // Update keymap
            switch (editingAction) {
                case "UP":
                    up.setText("Move up: " + Input.Keys.toString(keycode));
                    break;
                case "DOWN":
                    down.setText("Move down: " + Input.Keys.toString(keycode));
                    break;
                case "LEFT":
                    left.setText("Move left: " + Input.Keys.toString(keycode));
                    break;
                case "RIGHT":
                    right.setText("Move right: " + Input.Keys.toString(keycode));
                    break;
                case "PICK":
                    pickAndDrop.setText("Pick up / drop: " + Input.Keys.toString(keycode));
                    break;
                default:
                    break;
            }
        }
        // Reset values
        editingAction = null;
        up.setFontScale(2);
        down.setFontScale(2);
        left.setFontScale(2);
        right.setFontScale(2);
        pickAndDrop.setFontScale(2);

        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean keyUp(int i) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    /**
     * Ignored.
     */
    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}

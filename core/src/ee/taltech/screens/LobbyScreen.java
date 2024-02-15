package ee.taltech.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.gandalf.GandalfRoyale;

import java.util.logging.Logger;

public class LobbyScreen extends ScreenAdapter {

    GandalfRoyale game;
    Stage stage;
    Window gameNamingWindow;
    String gameName;
    Integer numberOfPlayers;
    TextButton buttonCreateGame;
    TextButton buttonBack;
    TextButton buttonJoin;
    TextButton buttonCancel;
    TextButton buttonCreate;
    TextField nameTextField;

    TextButton.TextButtonStyle textButtonStyle;
    Label.LabelStyle headingStyle;
    Label.LabelStyle gameStyle;

    Logger logger = Logger.getLogger(getClass().getName()); // need to be deleted later

    public LobbyScreen(GandalfRoyale game) {
        this.game = game;
        gameName = "Game_Name"; // Game name variable (changed by player)
        numberOfPlayers = 0; // Number of players variable (changed by sever)


        // Creating a Stage (place, where things can be put on)
        stage = new Stage(new ScreenViewport());
//        skin = new Skin(Gdx.files.internal("uiskin.json"));

        createUI(); // Creating user interface
        setupListeners(); // Setup listeners
    }

    private void createUI() {
        // Creating a Table (place, where things can be put in and positioned)
        Table table = new Table();
        table.setFillParent(true); // Fill the stage

        // Add items to stage
        stage.addActor(table);

        // Styling labels
        headingStyle = new Label.LabelStyle(game.font, Color.FIREBRICK);
        gameStyle = new Label.LabelStyle(game.font, Color.WHITE);

        // Styling button
        textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.getDrawable("button.up")
//        textButtonStyle.down = skin.getDrawable("button.down")
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = game.font;
        textButtonStyle.fontColor = Color.RED;

        // Create pop-up window where player can name their game
        gameNamingWindow = createGameNamingWindow();

        // Create top row
        buttonBack = new TextButton("Back", textButtonStyle);
        Label heading = new Label("LOBBY", headingStyle);
        buttonCreateGame = new TextButton("Create game", textButtonStyle);


        // Add top row to the table
        table.add(buttonBack).pad(10).left();
        table.add(heading).pad(10).expandX();
        table.add(buttonCreateGame).pad(10).right();

        // Create one game instance
        Label gameNameLabel = new Label(gameName, gameStyle);
        Label playersLabel = new Label( numberOfPlayers + " /10", gameStyle);
        buttonJoin = new TextButton("Join", textButtonStyle);

        // Add game info to the table
        table.row().padTop(20);
        table.add(gameNameLabel).colspan(2).center();
        table.add(playersLabel).center();
        table.add(buttonJoin).right();
    }

    private Window createGameNamingWindow() {
        // Styling window
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = game.font;
        windowStyle.titleFontColor = Color.RED;

        // Styling text field
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = game.font;
        textFieldStyle.fontColor = Color.BLUE;

        // Creating game naming window
        Window namingWindow = new Window("Starting Game", windowStyle);

        // Creating buttons
        buttonCancel = new TextButton("Cancel", textButtonStyle);
        buttonCreate = new TextButton("Create", textButtonStyle);

        // Creating text field
        nameTextField = new TextField("", textFieldStyle);
        nameTextField.setMessageText("Enter game name:");
        nameTextField.setMaxLength(12);
        nameTextField.setBlinkTime(1);

        // Adding elements to window
        namingWindow.getTitleLabel().setAlignment(Align.center);
        namingWindow.add(nameTextField).width(150f).height(50f);
        namingWindow.row();
        namingWindow.add(buttonCancel).padRight(10f);
        namingWindow.add(buttonCreate).padRight(50f);

        // Packing window up
        namingWindow.pack();

        // Setting windows position on the stage
        namingWindow.setPosition(stage.getWidth() / 2 - namingWindow.getWidth() / 2,
                stage.getHeight() / 2 - namingWindow.getHeight() / 2);

        // Returning working window
        return namingWindow;
    }

    private void setupListeners() {

        // Back button functionality on mouse click
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // If Back button is pressed go to MenuScreen
                game.setScreen(new MenuScreen(game));
            }
        });

        // Create game button functionality on mouse click
        buttonCreateGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // If Create Game button is pressed create a new game
                stage.addActor(gameNamingWindow);
            }
        });

        // Join button functionality on mouse click
        buttonJoin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // If number of players in game is less than 10 join and add 1 player
                if (numberOfPlayers < 10) {
                    game.setScreen(new GameScreen(game));
                    numberOfPlayers++;
                } else {
                    // Tell that game is full
                    logger.info("Game is FULL!");
                }
            }
        });

        // Cancel button in game naming window functionality on mouse click
        buttonCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nameTextField.setText("");
                gameNamingWindow.remove();
            }
        });

        // Create button in game naming window functionality on mouse click
        buttonCreate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameName = nameTextField.getText();
                nameTextField.setText("");
                gameNamingWindow.remove();
            }
        });
    }

    @Override
    public void show() {
        // Reading input
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen and update the stage
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Show the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
//        skin.dispose();
    }
}

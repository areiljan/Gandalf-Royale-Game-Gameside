package ee.taltech.screen.screens;

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
import ee.taltech.network.messages.GetLobbies;
import ee.taltech.network.messages.Join;
import ee.taltech.network.messages.LobbyCreation;
import ee.taltech.utilities.Lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LobbyScreen extends ScreenAdapter {

    GandalfRoyale game;
    Map<Integer, Lobby> lobbies;
    Map<Integer, Table> lobbyTables;
    Map<Integer, Label> lobbyPlayerCountLabels;
    Stage stage;
    Table root;
    Table headerTable;
    Window gameNamingWindow;
    TextButton buttonCreateGame;
    TextButton buttonBack;
    TextButton buttonJoin;
    TextButton buttonCancel;
    TextButton buttonCreate;
    TextField nameTextField;
    Label playersLabel;
    TextButton.TextButtonStyle textButtonStyle;
    Label.LabelStyle headingStyle;
    Label.LabelStyle gameStyle;

    Logger logger = Logger.getLogger(getClass().getName()); // Used for debugging

    /**
     * Construct LobbyScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public LobbyScreen(GandalfRoyale game) {
        this.game = game;

        lobbies = new HashMap<>(); // Map where all the lobbies are held by lobby ID
        lobbyTables = new HashMap<>(); // Map where all the lobby tables are held by lobby ID
        lobbyPlayerCountLabels = new HashMap<>(); // Map where all the player count labels are held by lobby ID

        stage = new Stage(new ScreenViewport()); // Creating a stage (place, where things can be put on)

        root = new Table(); // Creating root table that covers the whole stage
        root.top(); // Starts from the top of the screen
        root.setFillParent(true); // Fill the stage
        stage.addActor(root); // Add root to stage (only actor on stage)

//        skin = new Skin(Gdx.files.internal("uiskin.json")); // Skin for UI

        createHeader(); // Creating lobby header (stated only in constructor)
        setupListeners(); // Set up listeners for buttons (stated only in constructor)

        game.nc.sendTCP(new GetLobbies()); // Send GetLobbies message to server
    }

    /**
     * Create header part for LobbyScreen.
     */
    private void createHeader() {
        // Creating a headerTable (place, where things can be put in and positioned)
        headerTable = new Table();

        // Styling labels
        headingStyle = new Label.LabelStyle(game.font, Color.FIREBRICK);
        gameStyle = new Label.LabelStyle(game.font, Color.WHITE);

        // Styling buttons
        textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.getDrawable("button.up")
//        textButtonStyle.down = skin.getDrawable("button.down")
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = game.font;
        textButtonStyle.fontColor = Color.RED;

        // Create pop-up window where player can name their game
        gameNamingWindow = createGameNamingWindow();

        // Create header
        buttonBack = new TextButton("Back", textButtonStyle);
        Label heading = new Label("LOBBY", headingStyle);
        buttonCreateGame = new TextButton("Create game", textButtonStyle);

        // Add header to headerTable
        headerTable.add(buttonBack).pad(10).left();
        headerTable.add(heading).pad(10).expandX();
        headerTable.add(buttonCreateGame).pad(10).right();

        // Add headerTable to root table
        root.add(headerTable).growX().top();
    }

    /**
     * Set up listeners for buttons to work.
     */
    private void setupListeners() {

        // Back button functionality on click
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.screenController.setMenuScreen(); // Change screen to MenuScreen
            }
        });

        // Create game button functionality on click
        buttonCreateGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(gameNamingWindow); // Add gameNamingWindow to stage (show the window)
            }
        });

        // Cancel button in game naming window functionality on mouse click
        buttonCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                nameTextField.setText(""); // Clear input field
                gameNamingWindow.remove(); // Remove gameNamingWindow from screen
            }
        });

        // Create button in game naming window functionality on mouse click
        buttonCreate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Send LobbyCreation message to sever with game name and host ID
                game.nc.sendTCP(new LobbyCreation(nameTextField.getText(), game.nc.clientId));
                nameTextField.setText(""); // Clear input field
                gameNamingWindow.remove(); // Remove game name window
            }
        });
    }

    /**
     * Create visual part of GameNamingWindow.
     *
     * @return done GameNamingWindow
     */
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

        // Returning visual part of the window
        return namingWindow;
    }

    /**
     * Show new lobby when LobbyCreation or GetLobbies message has been sent by sever.
     *
     * @param lobby Lobby class instance
     */
    public void showLobby(Lobby lobby) {
        lobbies.put(lobby.getId(), lobby); // Add lobby to lobbies Map

        // Create lobbyTable
        Table lobbyTable = new Table();
        lobbyTables.put(lobby.getId(), lobbyTable); // Add lobbyTable to lobbyTables Map

        // Create lobby labels
        Label gameNameLabel = new Label(lobby.getName(), gameStyle);
        playersLabel = new Label(lobby.getPlayerCount() + "/10", gameStyle);
        lobbyPlayerCountLabels.put(lobby.getId(), playersLabel); // Add playerCountLabel to lobbyPlayerCountLabels Map

        // Create Join button
        buttonJoin = new TextButton("Join", textButtonStyle);
        buttonJoin.setUserObject(lobby.getId()); // Set game ID as user object for button
        buttonJoin.addListener(buttonJoinClicked); // Add new listener for every button

        // Add lobby elements to lobbyTable
        lobbyTable.add(gameNameLabel).pad(10).expandX().left();
        lobbyTable.add(playersLabel).pad(10).center();
        lobbyTable.add(buttonJoin).pad(10).left();

        // Add lobbyTable to root table
        root.row().padTop(20); // Empty row
        root.add(lobbyTable).growX();
    }

    /**
     * Set up listener for Join button.
     */
    private ClickListener buttonJoinClicked = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            // Get game ID from button user object
            Integer gameId = (Integer) event.getListenerActor().getUserObject();

            // Send Join message to sever
            game.nc.sendTCP(new Join(gameId, game.nc.clientId));
        }
    };

    /**
     * Go to LobbyRoomScreen.
     *
     * @param lobby given lobby
     */
    public void goToLobbyRoomScreen(Lobby lobby) {
        Gdx.app.postRunnable(this::dispose);
        game.screenController.setLobbyRoomScreen(lobby);
    }

    /**
     * Make player join lobby when sever sent Join message.
     *
     * @param gameId which lobby player wants to join
     * @param playerId of the player that wants to join lobby
     */
    public void joinLobby(Integer gameId, Integer playerId) {
        Lobby lobby = lobbies.get(gameId); // Get correct lobby from lobbies Map
        lobby.addPlayer(playerId); // Add player to given lobby
        lobbyPlayerCountLabels.get(gameId).setText(lobby.getPlayerCount() + "/10"); // Show new player count
        if (playerId == game.nc.clientId) {
            goToLobbyRoomScreen(lobby);
        }
    }

    /**
     * Make player leave lobby when sever sent Leave message.
     *
     * @param gameId which lobby player wants to leave
     * @param playerId of the player that wants to leave lobby
     */
    public void leaveLobby(Integer gameId, Integer playerId) {
        Lobby lobby = lobbies.get(gameId); // Get correct lobby from lobbies Map
        lobby.removePlayer(playerId); // Remove player from given lobby
        lobbyPlayerCountLabels.get(gameId).setText(lobby.getPlayerCount() + "/10"); // Show new player count
    }

    /**
     * Dismantle lobby when sever sent LobbyDismantle message.
     *
     * @param gameId which lobby has to be dismantled
     */
    public void dismantleLobby(Integer gameId) {
        lobbyTables.get(gameId).remove(); // Remove correct lobby table from root table
    }

    /**
     * Show screen on initialisation.
     */
    @Override
    public void show() {
        // Reading input
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Render the screen aka update every frame.
     *
     * @param delta time
     */
    @Override
    public void render(float delta) {
        // Clear the screen and update the stage
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Show the stage
        stage.act(delta);
        stage.draw();
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
        game.nc.removeAllListeners(); // Remove listener
//        skin.dispose();
    }
}

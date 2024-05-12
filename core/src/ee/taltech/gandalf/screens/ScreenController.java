package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.Lobby;
import ee.taltech.gandalf.components.StartedGame;
import ee.taltech.gandalf.scenes.SettingsWindow;

import java.util.List;

public class ScreenController {
    GandalfRoyale game;
    private MenuScreen menuScreen;
    private LobbyScreen lobbyScreen;
    private LobbyRoomScreen lobbyRoomScreen;
    private GameScreen gameScreen;
    private GameEndScreen gameEndScreen;
    private ScreenAdapter currentScreen;
    private SettingsWindow settingsWindow;

    /**
     * Construct ScreenController.
     *
     * @param game game instance
     */
    public ScreenController(GandalfRoyale game) {
        this.game = game;
        settingsWindow = new SettingsWindow(game);
    }

    /**
     * Set screen as MenuScreen.
     */
    public void setMenuScreen() {
        Gdx.app.postRunnable(() -> {
            menuScreen = new MenuScreen(game);
            game.setScreen(menuScreen);
            currentScreen = menuScreen;
        });
    }

    /**
     * Set screen as LobbyScreen.
     */
    public void setLobbyScreen() {
        Gdx.app.postRunnable(() -> {
            lobbyScreen = new LobbyScreen(game);
            game.setScreen(lobbyScreen);
            game.nc.addLobbyListener();
            currentScreen = lobbyScreen;
        });
    }

    /**
     * Set screen as LobbyRoomScreen.
     */
    public void setLobbyRoomScreen(Lobby lobby) {
        Gdx.app.postRunnable(() -> {
            lobbyRoomScreen = new LobbyRoomScreen(game, lobby);
            game.setScreen(lobbyRoomScreen);
            game.nc.addLobbyRoomListener();
            currentScreen = lobbyRoomScreen;
        });

    }

    /**
     * Set screen as GameScreen.
     */
    public void setGameScreen(Lobby lobby) {
        Gdx.app.postRunnable(() -> {
            gameScreen = new GameScreen(game, lobby);
            game.setScreen(gameScreen);
            game.nc.addGameListeners();
            currentScreen = gameScreen;
        });
    }

    /**
     * Set screen as GameEndScreen.
     */
    public void setGameEndScreen(StartedGame gameInstance, Integer winnerId) {
        Gdx.app.postRunnable(() -> {
            gameEndScreen = new GameEndScreen(game, gameInstance, winnerId);
            game.setScreen(gameEndScreen);
            currentScreen = gameEndScreen;
        });
    }

    /**
     * Get MenuScreen.
     */
    public MenuScreen getMenuScreen() {
        return menuScreen;
    }

    /**
     * Get LobbyScreen.
     */
    public LobbyScreen getLobbyScreen() {
        return lobbyScreen;
    }

    /**
     * Get LobbyRoomScreen.
     */
    public LobbyRoomScreen getLobbyRoomScreen() {
        return lobbyRoomScreen;
    }

    /**
     * Get GameScreen.
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Get GameEndScreen.
     */
    public GameEndScreen getGameEndScreen() {
        return gameEndScreen;
    }

    /**
     * Get SettingsWindow;
     */
    public SettingsWindow getSettingsWindow() {
        return settingsWindow;
    }

    /**
     * Get current screen.
     */
    public ScreenAdapter getCurrentScreen() {
        return currentScreen;
    }
}

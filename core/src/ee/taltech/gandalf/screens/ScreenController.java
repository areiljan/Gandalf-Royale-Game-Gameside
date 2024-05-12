package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.Lobby;
import ee.taltech.gandalf.components.StartedGame;

import java.util.List;

public class ScreenController {
    GandalfRoyale game;
    private MenuScreen menuScreen;
    private LobbyScreen lobbyScreen;
    private LobbyRoomScreen lobbyRoomScreen;
    private GameScreen gameScreen;
    private GameEndScreen gameEndScreen;

    /**
     * Construct ScreenController.
     *
     * @param game game instance
     */
    public ScreenController(GandalfRoyale game) {
        this.game = game;
    }

    /**
     * Set screen as MenuScreen.
     */
    public void setMenuScreen() {
        Gdx.app.postRunnable(() -> {
            menuScreen = new MenuScreen(game);
            game.setScreen(menuScreen);
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
        });
    }

    /**
     * Set screen as GameEndScreen.
     */
    public void setGameEndScreen(StartedGame gameInstance, Integer winnerId) {
        Gdx.app.postRunnable(() -> {
            gameEndScreen = new GameEndScreen(game, gameInstance, winnerId);
            game.setScreen(gameEndScreen);
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
}

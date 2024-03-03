package ee.taltech.screen;

import com.badlogic.gdx.Gdx;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.screen.screens.GameScreen;
import ee.taltech.screen.screens.LobbyRoomScreen;
import ee.taltech.screen.screens.LobbyScreen;
import ee.taltech.screen.screens.MenuScreen;
import ee.taltech.utilities.Lobby;

public class ScreenController {
    GandalfRoyale game;
    private MenuScreen menuScreen;
    private LobbyScreen lobbyScreen;
    private LobbyRoomScreen lobbyRoomScreen;
    private GameScreen gameScreen;

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
}

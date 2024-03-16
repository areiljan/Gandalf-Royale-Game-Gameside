package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.entities.Item;
import ee.taltech.gandalf.entities.collision.CollisionHandler;
import ee.taltech.gandalf.components.StartedGame;
import ee.taltech.gandalf.network.NetworkClient;
import ee.taltech.gandalf.network.messages.game.MouseClicks;
import ee.taltech.gandalf.entities.PlayerCharacter;
import ee.taltech.gandalf.input.PlayerInput;
import ee.taltech.gandalf.entities.Spell;
import ee.taltech.gandalf.components.SpellTypes;
import ee.taltech.gandalf.components.Lobby;
import ee.taltech.gandalf.scenes.Hud;

import java.util.Map;

public class GameScreen extends ScreenAdapter {

    private final World world;
    private final CollisionHandler collisionHandler;
    GandalfRoyale game;
    NetworkClient nc;
    private final OrthogonalTiledMapRenderer renderer;
    private final ExtendViewport viewport;
    public final OrthographicCamera camera;
    private final Hud hud;
    public final StartedGame startedGame;
    private final Map<Integer, PlayerCharacter> alivePlayers;
    private final PlayerCharacter clientCharacter;
    private final Map<Integer, Spell> spells;
    private final Map<Integer, Item> items;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private Texture img;
    private Texture fireballImg;
    private static Texture fireballBook;
    MouseClicks mouseClicks;

    private final ShapeRenderer shapeRenderer;

    private Box2DDebugRenderer debugRenderer; // For debugging

    /**
     * Construct GameScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public GameScreen(GandalfRoyale game, Lobby lobby) {
        world = new World(new Vector2(0, 0), true); // Create a new Box2D world
        collisionHandler = new CollisionHandler();
        world.setContactListener(new CollisionHandler());

        this.game = game;
        this.nc = game.nc;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ExtendViewport(500, 500, camera);
        viewport.apply();

        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("gameart2d-desert.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map);

        startedGame = new StartedGame(game, lobby, world);
        alivePlayers = startedGame.getAlivePlayers();
        clientCharacter = startedGame.getClientCharacter();
        spells = startedGame.getSpells();
        items = startedGame.getItems();

        hud = new Hud(camera, clientCharacter);

        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
    }

    /**
     * Draw all player character to screen.
     */
    private void drawPlayers() {
        for (PlayerCharacter player : alivePlayers.values()) {
            game.batch.begin();
            // Set the image to 3 times smaller picture and flip it, if player is moving left.
            game.batch.draw(img, player.xPosition, player.yPosition,
                    (float) img.getWidth() / 3, (float) img.getHeight() / 3, 0, 0,
                    img.getWidth(), img.getHeight(), player.moveLeft, false);
            game.batch.end();

            // Draw health and mana bar
            drawBars(player);
        }
    }

    /**
     * Draw health and mana bar for player.
     *
     * @param player any PlayerCharacter
     */
    private void drawBars(PlayerCharacter player) {
        // Render shapes
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw missing health bar
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3 + 10,
                (float) 100 * 2, 5);

        // Draw health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3 + 10,
                (float) player.health * 2, 5);

        // Draw missing mana bar
        shapeRenderer.setColor(Color.NAVY);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3,
                (float) 100 * 2, 5);

        // Draw mana bar
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(player.xPosition, player.yPosition + (float) img.getHeight() / 3,
                (float) player.mana * 2, 5);

        // Stop rendering shapes
        shapeRenderer.end();
    }

    /**
     * Draw spells.
     */
    private void drawSpells() {
        for (Spell spell : spells.values()) {
            if (spell.getType() ==  SpellTypes.FIREBALL) {
                game.batch.begin();
                game.batch.draw(fireballImg, (float) spell.getXPosition() - (float) fireballImg.getWidth() / 6,
                        (float) spell.getYPosition() - (float) fireballImg.getHeight() / 3,
                        (float) fireballImg.getWidth() / 3, (float) fireballImg.getHeight() / 3);
                game.batch.end();
            }
        }
    }

    /**
     * Draw items that are on the ground.
     */
    private void drawItems() {
        for (Item item : items.values()) {
            if (item.getType() == SpellTypes.FIREBALL) {
                game.batch.begin();
                game.batch.draw(fireballBook, item.getXPosition() - (float) fireballBook.getWidth() / 3,
                        item.getYPosition() - (float) fireballBook.getHeight() / 3,
                        fireballBook.getWidth(), fireballBook.getHeight());
                game.batch.end();
            }
        }
    }

    /**
     * Disable players movement aka don't listen to input.
     */
    public void disableClientPlayerCharacter() {
        Gdx.input.setInputProcessor(null);
    }

    public static Texture getFireBallBookTexture() {
        return fireballBook;
    }

    /**
     * Show screen on initialisation.
     */
    @Override
    public void show() {
        img = new Texture("wizard.png");
        fireballImg = new Texture("fireball.png");
        fireballBook = new Texture("fireball_book.png");
        Gdx.input.setInputProcessor(new PlayerInput(game, clientCharacter));
    }

    /**
     * Render the screen aka update every frame.
     *
     * @param delta time
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        world.step(delta, 6, 2);
        startedGame.update(delta);

        if(mouseClicks != null) {
            clientCharacter.setMouseHover(mouseClicks);
        }
        // Update camera position to follow player
        camera.position.x = clientCharacter.xPosition;
        camera.position.y = clientCharacter.yPosition;

        // Update camera matrices
        camera.update();

        // Set camera projection matrix
        game.batch.setProjectionMatrix(camera.combined);

        // Render game objects
        game.batch.begin();
        renderer.setView(camera);
        renderer.render();
        game.batch.end();

        drawPlayers(); // Draw client character.
        if (!spells.isEmpty()) {
            drawSpells(); // Draw spells.
        }
        drawItems();

        hud.draw();

        debugRenderer.render(world, camera.combined);
    }

    /**
     * Correct camera position when resizing window.
     *
     * @param width window width
     * @param height window height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hud.resize(width, height);
    }

    /**
     * Hide screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        img.dispose();
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}

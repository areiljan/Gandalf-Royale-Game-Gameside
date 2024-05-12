package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.*;
import ee.taltech.gandalf.entities.*;
import ee.taltech.gandalf.entities.collision.CollisionHandler;
import ee.taltech.gandalf.input.PlayerInput;
import ee.taltech.gandalf.network.NetworkClient;
import ee.taltech.gandalf.scenes.Hud;
import ee.taltech.gandalf.world.TileData;
import ee.taltech.gandalf.scenes.MenuWindow;
import ee.taltech.gandalf.world.WorldCollision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameScreen extends ScreenAdapter {
    private final World world;
    GandalfRoyale game;
    NetworkClient nc;

    private ExtendViewport viewport;
    private OrthographicCamera camera;

    private final Hud hud;
    private final MenuWindow menuWindow;
    private boolean menuWindowShown;
    public final StartedGame startedGame;

    private final Map<Integer, PlayerCharacter> gamePlayers;
    private final PlayerCharacter clientCharacter;

    private final OrthogonalTiledMapRenderer renderer;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;

    private static Texture otherPlayZoneTexture;
    private static Texture otherExpectedZoneTexture;
    private static Texture fireballTexture;
    private static Texture fireballBook;
    private static Texture coinTexture;
    private static Texture pumpkinAttackingTexture;
    private static Texture pumpkinWalkingTexture;
    private static Texture healingPotionTexture;

    private static Texture firstPlayZoneTexture;
    private static Texture firstExpectedZoneTexture;
    private float elapsedTime;
    private float actionAnimationTime;
    private float mobAttackAnimationTime;
    private Integer attackAnimationCount = 0;
    private final ShapeRenderer shapeRenderer;

    private final Box2DDebugRenderer debugRenderer; // For debugging
    private TextureRegion currentCharacterFrame;
    private TextureRegion currentMobFrame;
    private TextureRegion currentCoinFrame;
    private PlayZone playZone;
    private Integer currentTime;
    private List<TiledMapTileLayer> layersToBeOrdered;


    private final InputMultiplexer inputMultiplexer;

    /**
     * Construct GameScreen.
     *
     * @param game GandalfRoyale game instance
     */
    public GameScreen(GandalfRoyale game, Lobby lobby) {
        world = new World(new Vector2(0, 0), true); // Create a new Box2D world
        CollisionHandler collisionHandler = new CollisionHandler(); // This has to be on a separate line!!!
        world.setContactListener(new CollisionHandler());

        this.game = game;
        this.nc = game.nc;

        setTextures();

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        mapLoader = new TmxMapLoader();

        viewport = new ExtendViewport(Constants.MIN_CHUNKS_SEEN, Constants.MIN_CHUNKS_SEEN,
                Constants.MAX_TILES_SEEN_WIDTH, Constants.MAX_TILES_SEEN_HEIGHT, camera);
        viewport.apply();

        map = mapLoader.load("Gandalf_Royale.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Constants.PPM);
        new WorldCollision(world, map, nc);

        startedGame = new StartedGame(game, lobby, world);
        gamePlayers = startedGame.getGamePlayers();
        clientCharacter = startedGame.getClientCharacter();

        hud = new Hud(clientCharacter, startedGame);
        menuWindow = new MenuWindow(game);
        menuWindowShown = false;

        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new PlayerInput(game, clientCharacter, this));

        layersToBeOrdered = initializeLayersToBeOrdered();
    }

    /**
     * Add specific layers to the list.
     *
     * @return List of layers, that are needed to be ordered.
     */
    private List<TiledMapTileLayer> initializeLayersToBeOrdered() {
        List<TiledMapTileLayer> layers = new ArrayList<>();
        for (String layerName : Constants.LAYERS_TO_BE_ORDERED) {
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
            layers.add(layer);
        }
        return layers;
    }


    /**
     * CurrentTime setter.
     *
     * @param currentTime - set current time.
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }


    /**
     * Get any texture.
     * Give the appropriate type upon calling this method to get the right texture.
     *
     * @return fireballBook
     */
    public static Texture getTexture(TextureType textureType) {
        return switch (textureType) {
            case FIREBALL_BOOK -> fireballBook;
            case FIREBALL -> fireballTexture;
            case COIN -> coinTexture;
            case PUMPKINATTACK -> pumpkinAttackingTexture;
            case PUMPKINWALK -> pumpkinWalkingTexture;
            case HEALING_POTION -> healingPotionTexture;
            default -> new Texture("wizard.png");
        };
    }

    /**
     * Set all textures.
     */
    private static void setTextures() {
        // *------ BOOK TEXTURES ------*
        fireballBook = new Texture("Spells/Fireball/fireball_book.png");

        // *------ SPELL TEXTURES ------*
        fireballTexture = new Texture("Spells/Fireball/spell1_Fireball.png");

        // *------ HEALING POTION TEXTURES ------*
        healingPotionTexture = new Texture("Potion/potion.png");

        // *------ COIN TEXTURE ------*
        coinTexture = new Texture("Coin/Coin_rotating.png");

        // *------ PLAY ZONE TEXTURES ------*
        firstExpectedZoneTexture = new Texture("Zone/expected_zone.png");
        otherExpectedZoneTexture = new Texture("Zone/huge_expected_zone.png");
        otherPlayZoneTexture = new Texture("Zone/hugeNewZone.png");
        firstPlayZoneTexture = new Texture("Zone/safezone.png");

        // *------ PUMPKIN TEXTURE ------*
        pumpkinAttackingTexture = new Texture("Pumpkin/Pumpkin_Attacking.png");
        pumpkinWalkingTexture = new Texture("Pumpkin/Pumpkin_Walks.png");
    }

    /**
     * Set the specific wizards texture.
     * Useful because different wizards have different textures.
     */
    public static Texture getWizardTexture(int userID) {
        // Choose one of the spritesheets for the character
        String filename = "wizardTexture" + userID % 10 + ".png";
        FileHandle fileHandle = Gdx.files.internal("Wizards/" + filename);
        return new Texture(fileHandle);
    }

    /**
     * Draw all player character to screen.
     */
    private void drawPlayer(PlayerCharacter player) {
        // elapsedTime is never reset and used for looping animations.
        // use animationTime and reset it to play an animation once
        PlayerCharacterAnimator playerAnimator = player.getPlayerAnimator();

        // Movement and idle setters.
        if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.IDLE ||
                playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.MOVEMENT) {
            playerAnimator.setState();
        }

        // All state handling. Choosing the frame to display this tick.
        if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.DEATH) {
            currentCharacterFrame = playerAnimator.deathAnimation().getKeyFrame(4 * 0.3f, false);
        } else if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.ACTION) {
            // Increment animationTime
            actionAnimationTime += Gdx.graphics.getDeltaTime();
            currentCharacterFrame = playerAnimator.actionAnimation().getKeyFrame(actionAnimationTime);
            if (playerAnimator.actionAnimation().isAnimationFinished(actionAnimationTime)) {
                playerAnimator.setState(PlayerCharacterAnimator.AnimationStates.IDLE);
                actionAnimationTime = 0;
            }
        } else if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.MOVEMENT) {
            currentCharacterFrame = playerAnimator.movementAnimation().getKeyFrame(elapsedTime, true); // Use elapsedTime
        } else if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.IDLE) {
            currentCharacterFrame = playerAnimator.idleAnimation().getKeyFrame(elapsedTime, true); // Use elapsedTime
        }
        game.batch.begin();
        // Set the image to 3 times smaller picture and flip it, if player is moving left.
        if (playerAnimator.lookRight()) {
            game.batch.draw(currentCharacterFrame, player.getXPosition() - 35f / Constants.PPM,
                    player.getYPosition() - 40f / Constants.PPM,
                    80 / Constants.PPM,
                    80 / Constants.PPM);
        } else {
            game.batch.draw(currentCharacterFrame, player.getXPosition() - 45f / Constants.PPM,
                    player.getYPosition() - 40f / Constants.PPM,
                    80 / Constants.PPM,
                    80 / Constants.PPM);
        }
        game.batch.end();
        // Draw health and mana bar
        drawBars(player);
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
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(player.getXPosition() - 50 / Constants.PPM, player.getYPosition() + 35 / Constants.PPM,
                100 / Constants.PPM, 2 / Constants.PPM);

        // Draw health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(player.getXPosition() - 50 / Constants.PPM, player.getYPosition() + 35 / Constants.PPM,
                player.getHealth() / Constants.PPM, 2 / Constants.PPM);

        // Draw missing mana bar
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(player.getXPosition() - 50 / Constants.PPM, player.getYPosition() + 30 / Constants.PPM,
                100 / Constants.PPM, 2 / Constants.PPM);

        // Draw mana bar
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(player.getXPosition() - 50 / Constants.PPM, player.getYPosition() + 30 / Constants.PPM,
                (float) (player.getMana() / Constants.PPM), 2 / Constants.PPM);

        // Stop rendering shapes
        shapeRenderer.end();
    }

    /**
     * Draw PlayZone.
     */
    private void drawPlayZone() {
        playZone = startedGame.getPlayZone();
        int stage = playZone.getStage();
        game.batch.begin();
        if (stage == 1) {
            game.batch.draw(firstExpectedZoneTexture,
                    playZone.firstPlayZoneX() - Constants.FIRST_ZONE_TEXTURE_SIZE / 2f,
                    playZone.firstPlayZoneY() - Constants.FIRST_ZONE_TEXTURE_SIZE / 2f,
                    Constants.FIRST_ZONE_TEXTURE_SIZE, Constants.FIRST_ZONE_TEXTURE_SIZE);
        }
        if (stage == 2 || stage == 3) {
            game.batch.draw(firstPlayZoneTexture,
                    playZone.firstPlayZoneX() - Constants.FIRST_ZONE_TEXTURE_SIZE / 2f,
                    playZone.firstPlayZoneY() - Constants.FIRST_ZONE_TEXTURE_SIZE / 2f,
                    Constants.FIRST_ZONE_TEXTURE_SIZE, Constants.FIRST_ZONE_TEXTURE_SIZE);
        }
        if (stage == 3) {
            game.batch.draw(otherExpectedZoneTexture,
                    playZone.secondPlayZoneX() - Constants.SECOND_ZONE_TEXTURE_SIZE / 2f,
                    playZone.secondPlayZoneY() - Constants.SECOND_ZONE_TEXTURE_SIZE / 2f,
                    Constants.SECOND_ZONE_TEXTURE_SIZE, Constants.SECOND_ZONE_TEXTURE_SIZE);
        }
        if (stage == 4 || stage == 5) {
            // size is map times 4
            game.batch.draw(otherPlayZoneTexture,
                    playZone.secondPlayZoneX() - Constants.SECOND_ZONE_TEXTURE_SIZE / 2f,
                    playZone.secondPlayZoneY() - Constants.SECOND_ZONE_TEXTURE_SIZE / 2f,
                    Constants.SECOND_ZONE_TEXTURE_SIZE, Constants.SECOND_ZONE_TEXTURE_SIZE);
        }
        if (stage == 5) {
            game.batch.draw(otherExpectedZoneTexture,
                    playZone.thirdPlayZoneX() - Constants.THIRD_ZONE_TEXTURE_SIZE / 2f,
                    playZone.thirdPlayZoneY() - Constants.THIRD_ZONE_TEXTURE_SIZE / 2f,
                    Constants.THIRD_ZONE_TEXTURE_SIZE, Constants.THIRD_ZONE_TEXTURE_SIZE);
        }
        if (stage >= 6) {
            game.batch.draw(otherPlayZoneTexture,
                    playZone.thirdPlayZoneX() - Constants.THIRD_ZONE_TEXTURE_SIZE / 2f,
                    playZone.thirdPlayZoneY() - Constants.THIRD_ZONE_TEXTURE_SIZE / 2f,
                    Constants.THIRD_ZONE_TEXTURE_SIZE, Constants.THIRD_ZONE_TEXTURE_SIZE);
        }

        game.batch.end();
    }

    /**
     * Draw spells.
     */
    private void drawSpell(Spell spell) {
        // The spells need to be changed only outside rendering, otherwise will crash.
        if (spell.rotation().isPresent()) {
            if (spell.getType() == ItemTypes.FIREBALL) {
                game.batch.begin();
                TextureRegion spellCurrentFrame = spell.getFireballAnimation().getKeyFrame(elapsedTime, true);
                game.batch.draw(spellCurrentFrame,
                        spell.getXPosition() - 32 / Constants.PPM / 2,
                        spell.getYPosition() - 17 / Constants.PPM / 2,
                        32 / Constants.PPM / 2,
                        32 / Constants.PPM / 2,
                        32 / Constants.PPM,
                        17 / Constants.PPM,
                        1,
                        1,
                        spell.rotation().get());
                game.batch.end();
            }
        }
    }

    /**
     * Draw items that are on the ground.
     */
    private void drawItem(Item item) {
        if (item.getType() == ItemTypes.COIN) {
            currentCoinFrame = item.getCoinRotationAnimation().getKeyFrame(elapsedTime, true);
            game.batch.begin();
            game.batch.draw(currentCoinFrame,
                    item.getXPosition() - item.getTextureWidth() / 2,
                    item.getYPosition() - item.getTextureHeight() / 2,
                    10 / Constants.PPM, 10 / Constants.PPM);
            game.batch.end();
        } else {
            game.batch.begin();
            game.batch.draw(item.getTexture(),
                    item.getXPosition() - item.getTextureWidth() / 2,
                    item.getYPosition() - item.getTextureHeight() / 2,
                    item.getTextureWidth(), item.getTextureHeight());
            game.batch.end();
        }
    }

    /**
     * Draw mobs with their health bar.
     */
    private void drawMob(Mob mob) {
        MobAnimator mobAnimator = mob.getMobAnimator();
        int shortestDistance = getShortestDistance(mob);
        if (shortestDistance < 2) { // Check if mob is near the player
            // Increment attack animation time
            mobAttackAnimationTime += Gdx.graphics.getDeltaTime();

            // Check if attack animation is finished
            if (mobAnimator.attackAnimation().isAnimationFinished(mobAttackAnimationTime)) {
                // Reset animation time
                mobAttackAnimationTime = 0;
                attackAnimationCount++;
            } else if (attackAnimationCount < 2) {
                // Set current mob frame to attack animation frame
                currentMobFrame = mob.getMobAnimator().attackAnimation().getKeyFrame(mobAttackAnimationTime, true);
            } else {
                // Set current mob frame to movement animation frame
                currentMobFrame = mob.getMobAnimator().movementAnimation().getKeyFrame(elapsedTime, true);
            }
        } else {
            // Reset attack animation count when player moves away
            attackAnimationCount = 0;
            // Set current mob frame to movement animation frame
            currentMobFrame = mob.getMobAnimator().movementAnimation().getKeyFrame(elapsedTime, true);
        }

        // *-------------- MOB ASSET --------------*
        game.batch.begin();
        game.batch.draw(currentMobFrame,
                mob.getXPosition() - Constants.PUMPKIN_WIDTH / 2f,
                mob.getYPosition() - Constants.PUMPKIN_HEIGHT / 2f,
                Constants.PUMPKIN_WIDTH, Constants.PUMPKIN_HEIGHT);
        game.batch.end();

        // *-------------- MOB HEATH BAR --------------*
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Start drawing

        // Missing health
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(
                mob.getXPosition() - 25f / 2f / Constants.PPM,
                mob.getYPosition() + Constants.PUMPKIN_HEIGHT / 2f,
                25 / Constants.PPM, 2 / Constants.PPM);

        // Present health
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(
                mob.getXPosition() - 25f / 2f / Constants.PPM,
                mob.getYPosition() + Constants.PUMPKIN_HEIGHT / 2f,
                mob.getHealth() / 2f / Constants.PPM, 2 / Constants.PPM);

        shapeRenderer.end(); // Stop drawing
    }

    /**
     * Get the shortest distance to a player.
     *
     * @param mob mob whose distance to players is checked
     * @return integer values of shortest distance to player from a mob
     */
    private int getShortestDistance(Mob mob) {
        int shortestDistance = Integer.MAX_VALUE; // Initialize with a large value
        int distanceFromPlayer = 0;
        for (PlayerCharacter playerCharacter : gamePlayers.values()) {
            distanceFromPlayer = (int) Math.sqrt(Math.pow(playerCharacter.getXPosition() - mob.getXPosition(), 2)
                    + Math.pow(playerCharacter.getYPosition() - mob.getYPosition(), 2));
            // Update the shortest distance if the current distance is shorter
            shortestDistance = Math.min(shortestDistance, distanceFromPlayer);
        }
        return shortestDistance;
    }

    /**
     * Display message on the screen.
     *
     * @param message message as a string
     */
    public void displayMessageOnScreen(String message) {
        hud.addMessage(message);
    }

    /**
     * Toggle menu window and input processor.
     */
    public void toggleMenuWindow() {
        menuWindowShown = !menuWindowShown;

        if (menuWindowShown) {
            inputMultiplexer.addProcessor(menuWindow.getStage()); // Listen to input from menu window
        } else {
            inputMultiplexer.removeProcessor(menuWindow.getStage()); // Stop listening to input from menu window
        }
    }

    /**
     * Show screen on initialization.
     */
    @Override
    public void show() {
        // Set input for client
        Gdx.input.setInputProcessor(inputMultiplexer);
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


        // Update camera position to follow player
        camera.position.set(clientCharacter.getXPosition(), clientCharacter.getYPosition(), 0);
        // Update camera matrices
        camera.update();
        viewport.apply();
        // Set camera projection matrix
        game.batch.setProjectionMatrix(camera.combined);

        // Render game objects
        game.batch.begin();
        camera.zoom = 3f; // To render 3X bigger area than seen.
        renderer.setView(camera);
        renderer.render(Constants.BACKGROUND_LAYERS);
        game.batch.end();
        renderByLayering(); // Render entities and tiles
        camera.zoom = 1f; // Reset the camera back to its original state.

        debugRenderer.render(world, camera.combined);

        if (startedGame.getPlayZone() != null) {
            drawPlayZone();
        } else {
            playZone = startedGame.getPlayZone();
        }

        hud.draw(currentTime);

        if (menuWindowShown) {
            menuWindow.draw();
        }
    }

    /**
     * Sort player rendering and layer rendering to make the right visual appearance.
     */
    private void renderByLayering() {
        // Get all game objects, that should be rendered.
        List<Entity> entities = getGameObjects();
        elapsedTime += Gdx.graphics.getDeltaTime();
        for (Entity entity : entities) {
            switch (entity) {
                case PlayerCharacter player -> drawPlayer(player);
                case TileData tileData -> drawTile(tileData);
                case Mob mob -> drawMob(mob);
                case Spell spell -> drawSpell(spell);
                case Item item -> drawItem(item);
                default -> throw new IllegalStateException("Rendering for item not implemented yet: " + entity);
            }
        }
    }

    /**
     * Render tile once.
     *
     * @param tile One tile asset
     */
    private void drawTile(TileData tile) {
        game.batch.begin();
        game.batch.draw(tile.sprite, tile.x, tile.y,
                tile.sprite.getWidth() / Constants.PPM,
                tile.sprite.getHeight() / Constants.PPM);
        game.batch.end();
    }

    /**
     * Sorts the players and tiles based on y position.
     *
     * @return Players and Tiles in render order.
     */
    private List<Entity> getGameObjects() {
        List<Entity> entities = new ArrayList<>();
        entities.addAll(getTilesNearby());
        entities.addAll(startedGame.getGamePlayers().values());
        entities.addAll(startedGame.getSpells().values());
        entities.addAll(startedGame.getMobs().values());
        entities.addAll(startedGame.getItems().values());
        entities.sort(new GameObjectComparator());
        return entities.reversed(); // Higher Y should be rendered first.
    }

    /**
     * Get the tiles that need to be rendered on the screen.
     *
     * @return tiles near client's viewport.
     */
    private List<TileData> getTilesNearby() {
        List<TileData> tilesNearby = new ArrayList<>();
        // Loop through 3 layers "props", "vegetation", "rocks", that are manually set.
        for (TiledMapTileLayer layer : layersToBeOrdered) {
            // Loop x-cords around client's viewport.
            for (int x = (int) (clientCharacter.getXPosition() - Constants.MAX_TILES_SEEN_WIDTH);
                 x < clientCharacter.getXPosition() + Constants.MAX_TILES_SEEN_WIDTH; x++) {
                // Loop y-cords around client's viewport. Y - cords are read from top to bottom.
                for (int y = (int) (clientCharacter.getYPosition() + Constants.MAX_TILES_SEEN_HEIGHT);
                     y > clientCharacter.getYPosition() - Constants.MAX_TILES_SEEN_HEIGHT; y--) {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    if (cell != null) {
                        TiledMapTile tile = cell.getTile();
                        Sprite sprite = new Sprite(tile.getTextureRegion().getTexture());
                        if (cell.getFlipHorizontally()) {
                            sprite.flip(true, false);
                        }
                        // Create TileData object accordingly.
                        tilesNearby.add(new TileData(sprite, x, y, layer));
                    }
                }
            }
        }
        return tilesNearby;
    }

    /**
     * dd
     * Correct camera position when resizing a window.
     *
     * @param width  window width
     * @param height window height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
        menuWindow.resize(width, height);
    }

    /**
     * Hide screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        world.dispose();
        map.dispose();
        debugRenderer.dispose();
        hud.dispose();
        menuWindow.dispose();
        game.nc.removeAllListeners();
    }
}

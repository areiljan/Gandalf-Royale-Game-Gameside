package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import ee.taltech.gandalf.components.*;
import ee.taltech.gandalf.entities.*;
import ee.taltech.gandalf.entities.collision.CollisionHandler;
import ee.taltech.gandalf.network.NetworkClient;
import ee.taltech.gandalf.input.PlayerInput;
import ee.taltech.gandalf.scenes.Hud;
import ee.taltech.gandalf.world.WorldCollision;

import java.util.Map;

public class GameScreen extends ScreenAdapter {

    private final World world;
    GandalfRoyale game;
    NetworkClient nc;

    private final ExtendViewport viewport;
    public final OrthographicCamera camera;

    private final Hud hud;
    public final StartedGame startedGame;

    private final Map<Integer, PlayerCharacter> gamePlayers;
    private final PlayerCharacter clientCharacter;
    private final Map<Integer, Spell> spells;
    private final Map<Integer, Item> items;
    private final Map<Integer, Mob> mobs;

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

    private static Integer pumpkinWidth;
    private static Integer pumpkinHeight;

    private static Texture firstPlayZoneTexture;
    private static Texture firstExpectedZoneTexture;
    private float elapsedTime;
    private float animationTime;
    private final ShapeRenderer shapeRenderer;

    private final Box2DDebugRenderer debugRenderer; // For debugging
    private TextureRegion currentCharacterFrame;
    private TextureRegion currentMobFrame;
    private PlayZone playZone;
    private Integer currentTime;

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
        pumpkinWidth = 50;
        pumpkinHeight = 50;

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ExtendViewport(150, 150, camera);
        viewport.apply();

        this.mapLoader = new TmxMapLoader();
        this.map = mapLoader.load("Gandalf_Royale.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map);

        new WorldCollision(world, map);

        startedGame = new StartedGame(game, lobby, world);
        gamePlayers = startedGame.getGamePlayers();
        clientCharacter = startedGame.getClientCharacter();
        spells = startedGame.getSpells();
        items = startedGame.getItems();
        mobs = startedGame.getMobs();

        hud = new Hud(clientCharacter);

        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
    }

    /**
     * CurrentTime setter.
     * @param currentTime - set current time.
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Get any texture.
     * Give the appropriate type upon calling this method to get the right texture.
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
        coinTexture = new Texture("Coin/coin.png");

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
    private void drawPlayers() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        for (PlayerCharacter player : gamePlayers.values()) {
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
                animationTime += Gdx.graphics.getDeltaTime();
                currentCharacterFrame = playerAnimator.actionAnimation().getKeyFrame(animationTime);
                if (playerAnimator.actionAnimation().isAnimationFinished(animationTime)) {
                    playerAnimator.setState(PlayerCharacterAnimator.AnimationStates.IDLE);
                    animationTime = 0;
                }
            } else if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.MOVEMENT) {
                currentCharacterFrame = playerAnimator.movementAnimation().getKeyFrame(elapsedTime, true); // Use elapsedTime
            } else if (playerAnimator.getState() == PlayerCharacterAnimator.AnimationStates.IDLE) {
                currentCharacterFrame = playerAnimator.idleAnimation().getKeyFrame(elapsedTime, true); // Use elapsedTime
            }


            game.batch.begin();
            // Set the image to 3 times smaller picture and flip it, if player is moving left.
            if (playerAnimator.lookRight()) {
                game.batch.draw(currentCharacterFrame, (float) player.xPosition - 30,
                        (float) player.yPosition - 15, 80, 80);
            } else {
                game.batch.draw(currentCharacterFrame, (float) player.xPosition - 40,
                        (float) player.yPosition - 15, 80, 80);
            }
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
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect( (float) player.xPosition - 45, (float) player.yPosition + 60,
                100, 2);

        // Draw health bar
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect((float) player.xPosition - 45, (float) player.yPosition + 60,
                player.getHealth(), 2);

        // Draw missing mana bar
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect((float) player.xPosition - 45, (float) player.yPosition + 55,
                100, 2);

        // Draw mana bar
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect((float) player.xPosition - 45, (float) player.yPosition + 55,
                (float) player.getMana(), 2);

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
            game.batch.draw(firstExpectedZoneTexture, playZone.firstPlayZoneX() - 6000, playZone.firstPlayZoneY() - 6000, 12000, 12000);
        }
        if (stage == 2 || stage == 3) {
            game.batch.draw(firstPlayZoneTexture, playZone.firstPlayZoneX() - 6000, playZone.firstPlayZoneY() - 6000, 12000, 12000);
        }
        if (stage == 3) {
            game.batch.draw(otherExpectedZoneTexture, playZone.secondPlayZoneX() - 19200, playZone.secondPlayZoneY() - 19200, 38400, 38400);
        }
        if (stage == 4 || stage == 5) {
            // size is map times 4
            game.batch.draw(otherPlayZoneTexture, playZone.secondPlayZoneX() - 19200, playZone.secondPlayZoneY() - 19200, 38400, 38400);
        }
        if (stage == 5) {
            game.batch.draw(otherExpectedZoneTexture, playZone.thirdPlayZoneX() - 7350, playZone.thirdPlayZoneY() - 7350, 14700, 14700);
        }
        if (stage >= 6) {
            game.batch.draw(otherPlayZoneTexture, playZone.thirdPlayZoneX() - 7350, playZone.thirdPlayZoneY() - 7350, 14700, 14700);
        }

        game.batch.end();
    }

    /**
     * Draw spells.
     */
    private void drawSpells() {
        for (Spell spell : spells.values()) {
            if(spell.rotation().isPresent()) {
                if (spell.getType() == ItemTypes.FIREBALL) {
                    game.batch.begin();
                    TextureRegion spellCurrentFrame = spell.getFireballAnimation().getKeyFrame(elapsedTime, true);
                    game.batch.draw(spellCurrentFrame,
                            (float) spell.getXPosition(),
                            (float) spell.getYPosition() - 32,
                            0,
                            0,
                            32,
                            17,
                            1,
                            1,
                            spell.rotation().get());
                    game.batch.end();
                }
            }
        }
    }

    /**
     * Draw items that are on the ground.
     */
    private void drawItems() {
        for (Item item : items.values()) {
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
    private void drawMobs() {
        for (Mob mob : mobs.values()) {
            for (PlayerCharacter playerCharacter : gamePlayers.values()) {
                int distanceFromPlayer = (int) Math.sqrt(Math.pow(playerCharacter.xPosition - mob.getXPosition(), 2)
                        + Math.pow(playerCharacter.yPosition - mob.getYPosition(), 2));
                if (distanceFromPlayer < 50) {
                    currentMobFrame = mob.getMobAnimator().attackAnimation().getKeyFrame(elapsedTime, true);
                } else {
                    currentMobFrame = mob.getMobAnimator().movementAnimation().getKeyFrame(elapsedTime, true);
                }

            }
            // *-------------- MOB ASSET --------------*
            game.batch.begin();
            game.batch.draw(currentMobFrame, (float) mob.getXPosition(),
                    (float) mob.getXPosition(), 100, 100);
            game.batch.end();

            // *-------------- MOB HEATH BAR --------------*
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Start drawing

            // Missing health
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(
                    mob.getXPosition() - (float) 25 / 2,
                    mob.getYPosition() + (float) pumpkinHeight / 2,
                    25, 2);

            // Present health
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(
                    mob.getXPosition() - (float) 25 / 2,
                    mob.getYPosition() + (float) pumpkinHeight / 2,
                    (float) mob.getHealth() / 2, 2);

            shapeRenderer.end(); // Stop drawing
        }
    }

    /**
     * Disable players movement aka don't listen to input.
     */
    public void disableClientPlayerCharacter() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Show screen on initialisation.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new PlayerInput(game, clientCharacter));
    }

    /**
     * Render the screen aka update every frame.
     *
     * @param delta time
     */
    @Override
    public void render(float delta) {
        viewport.apply();
        ScreenUtils.clear(0, 0, 0, 0);
        viewport.apply();
        world.step(delta, 6, 2);
        startedGame.update(delta);

        // Update camera position to follow player
        camera.position.x = clientCharacter.xPosition;
        camera.position.y = clientCharacter.yPosition;

        // Update camera matrices
        camera.update();

        // Set camera projection matrix
        game.batch.setProjectionMatrix(camera.combined);


        // Render game objects
        game.batch.begin();
        camera.zoom = 12f; // To render 4X bigger area than seen.
        renderer.setView(camera);
        renderer.render();
        camera.zoom = 3f; // Reset the camera back to its original state.
        game.batch.end();

        drawPlayers(); // Draw client character.
        drawMobs();
        if (!spells.isEmpty()) {
            drawSpells(); // Draw spells.
        }

        debugRenderer.render(world, camera.combined);

        drawItems();

        if (startedGame.getPlayZone() != null) {
            drawPlayZone();
        } else {
            playZone = startedGame.getPlayZone();
        }

        debugRenderer.render(world, camera.combined);

        hud.draw(currentTime);
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
        world.dispose();
        debugRenderer.dispose();
        hud.dispose();
    }
}

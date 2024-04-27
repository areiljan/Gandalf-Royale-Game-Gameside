package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.gandalf.entities.Item;
import ee.taltech.gandalf.entities.PlayerCharacter;

import javax.swing.*;

public class Hud {

    private static final Texture ACTIVE_INVENTORY = new Texture("Hud/ActiveInventorySlot.png");
    private static final Texture INACTIVE_INVENTORY = new Texture("Hud/NotActiveInventorySlot.png");
    private static final Texture COIN = new Texture("Coin/Coin_rotating.png");

    private final PlayerCharacter player;
    private final Viewport viewport;
    private final Stage stage;

    private Image coinImage;
    private Label coinLabel;

    private Label timeLabel;

    private final Group inventoryGroup;
    private final Group coinCounterGroup;
    private final Group timeCounterGroup;

    private final Image[] inventorySlotImages;
    private final Image[] itemImages;

    /**
     * Construct hud for client's game.
     *
     * @param player client's player character
     */
    public Hud(PlayerCharacter player) {
        this.player = player;

        // *--------- VIEWPORT ---------*
        viewport = new ScreenViewport();

        // *--------- STAGE ---------*
        stage = new Stage(viewport);

        // *--------- INVENTORY ---------*
        inventorySlotImages = new Image[3];
        itemImages = new Image[3];

        // *--------- GROUPS ---------*
        inventoryGroup = new HorizontalGroup();
        coinCounterGroup = new HorizontalGroup();
        timeCounterGroup = new HorizontalGroup();

        // *--------- CREATE UI ---------*
        createInventory();
        createCoinCounter();
        createTimeCounter();

        // *--------- SET UI POSITION ON THE SCREEN ---------*
        inventoryGroup.setPosition(0, viewport.getScreenHeight() - 32);
        coinCounterGroup.setPosition(0, 32);
        timeCounterGroup.setPosition(viewport.getScreenWidth() - 100, viewport.getScreenHeight() - 32);

        // *--------- ADD UI TO STAGE ---------*
        stage.addActor(inventoryGroup);
        stage.addActor(coinCounterGroup);
        stage.addActor(timeCounterGroup);
    }

    /**
     * Create inventory.
     */
    public void createInventory() {
        for (int i = 0; i < 3; i++) {
            Stack stack = new Stack(); // Create new stack for every inventory slot
            inventoryGroup.addActor(stack); // Put stack on table

            inventorySlotImages[i] = new Image(); // Create placeholder image for inventory slot
            itemImages[i] = new Image(); // Creat placeholder image for item

            stack.add(inventorySlotImages[i]); // Put placeholder image into a stack
            stack.add(itemImages[i]); // Put placeholder image into a stack
        }
    }

    /**
     * Create coin counter.
     */
    private void createCoinCounter() {
        coinImage = new Image(); // Coin image
        coinLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK)); // Label to show int

        // Resize coinImage in a weird way because it didn't change in any other way
        coinImage.setDrawable(new TextureRegionDrawable(new TextureRegion(COIN)));
        coinImage.getDrawable().setMinWidth(64);
        coinImage.getDrawable().setMinHeight(64);

        // Resize label using font
        coinLabel.setFontScale(4f);

        // Add label and image to group
        coinCounterGroup.addActor(coinImage);
        coinCounterGroup.addActor(coinLabel);
    }

    /**
     * Create time counter.
     */
    private void createTimeCounter() {
        timeLabel = new Label("0", new Label.LabelStyle(new BitmapFont(), Color.BLACK)); // Label to show int

        // Resize label using font
        timeLabel.setFontScale(4f);

        // Add label to group
        timeCounterGroup.addActor(timeLabel);
    }

    /**
     * Draw inventory.
     */
    private void drawInventory() {
        for (int i = 0; i < 3; i++) {
            Image inventorySlotImage = inventorySlotImages[i]; // Get inventory image for that slot
            Texture inventoryTexture;

            if (player.getSelectedSlot() == i) { // If the slot is selected make it yellow
                inventoryTexture = ACTIVE_INVENTORY;
            } else { // If the slot is not selected make it gray
                inventoryTexture = INACTIVE_INVENTORY;
            }

            // Put correct texture to inventory image
            inventorySlotImage.setDrawable(new TextureRegionDrawable(new TextureRegion(inventoryTexture)));

            Image itemImage = itemImages[i]; // Get item image for that slot
            itemImage.setDrawable(drawItem(i)); // Put correct texture to item image
        }
    }

    /**
     * Create drawable for item, if slot has item in it.
     *
     * @param i what slot is looked at
     * @return items drawable or null if there is no item in that slot
     */
    private Drawable drawItem(int i) {
        Texture itemTexture;
        if (player.getInventory().get(i) != null) { // Get items texture if item exists
            Item item = player.getInventory().get(i);
            itemTexture = item.getTexture();
            return new TextureRegionDrawable(new TextureRegion(itemTexture));
        }
        return null; // Return null if there is no item in that slot
    }

    /**
     * Draw coin counter.
     */
    private void drawCoinCounter() {
        coinLabel.setText(String.valueOf(player.getCoins())); // Update label
    }

    /**
     * Draw time counter.
     */
    private void drawTimeCounter(Integer currentTime) {
        timeLabel.setText(String.valueOf(currentTime)); // Update label
    }

    /**
     * Draw hud aka update the values in it.
     */
    public void draw(Integer currentTime) {
        drawInventory();
        drawCoinCounter();
        drawTimeCounter(currentTime);

        stage.act();
        stage.draw();
    }

    /**
     * Resize hud.
     *
     * @param width new width
     * @param height new height
     */
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Update viewport size

        // Update group positions in new viewport
        inventoryGroup.setPosition(0, viewport.getScreenHeight() - 32);
        coinCounterGroup.setPosition(0, 32);
        timeCounterGroup.setPosition(viewport.getScreenWidth() - 100, viewport.getScreenHeight() - 32);
    }

    /**
     * Dispose hud.
     */
    public void dispose() {
        stage.dispose();
    }
}

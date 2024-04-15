package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.gandalf.entities.PlayerCharacter;

public class Hud {

    private final PlayerCharacter player;
    private final Stage stage;
    private final Table root;
    private final Image[] inventorySlotImages;
    private final Image[] itemImages;
    private Viewport viewport;
    private int currentTime;

    /**
     * Construct hud for client's game.
     *
     * @param camera camera that is following player character
     * @param player client's player character
     */
    public Hud(Camera camera, PlayerCharacter player) {
        this.viewport = new ScreenViewport(camera);
        this.player = player;
        this.currentTime = 0;

        inventorySlotImages = new Image[3];
        itemImages = new Image[3];

        stage = new Stage(viewport);
        root = createInventoryTable();
        stage.addActor(root);

        viewport.setWorldSize(viewport.getWorldWidth() * 20, viewport.getWorldHeight() * 20);
    }

    /**
     * Set the current Time.
     * @param currentTime - time right now.
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * Create inventory table.
     *
     * @return inventory table
     */
    public Table createInventoryTable() {
        Table table = new Table();
        table.top().center();
        table.setFillParent(true);

        for (int i = 0; i < 3; i++) {
            Stack stack = new Stack(); // Create new stack for every inventory slot

            inventorySlotImages[i] = new Image(); // Create placeholder image for inventory slot
            itemImages[i] = new Image(); // Creat placeholder image for item

            stack.add(inventorySlotImages[i]); // Put placeholder image into a stack
            stack.add(itemImages[i]); // Put placeholder image into a stack

            table.add(stack).size(256, 256); // Put stack on table
        }
        return table;
    }

    /**
     * Draw inventory.
     */
    private void drawInventory() {
        for (int i = 0; i < 3; i++) {
            Image inventorySlotImage = inventorySlotImages[i]; // Get inventory image for that slot
            Texture inventoryTexture;

            if (player.getSelectedSlot() == i) { // If the slot is selected make it yellow
                inventoryTexture = new Texture("ActiveInventorySlot.png");
            } else { // If the slot is not selected make it gray
                inventoryTexture = new Texture("NotActiveInventorySlot.png");
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
        Texture itemTexture = null;
        if (player.getInventory().get(i) != null) { // Get items texture if item exists
            itemTexture = player.getInventory().get(i).getTexture();
            return new TextureRegionDrawable(new TextureRegion(itemTexture));
        }
        return null; // Return null if there is no item in that slot
    }

    /**
     * Draw hud aka update the values in it.
     */
    public void draw() {
        drawInventory();

        // Move the table so hud moves with player.
        root.setPosition(player.xPosition - (float) viewport.getScreenWidth() / 2,
                player.yPosition - (float) viewport.getScreenHeight() * 1.8f);

        viewport.apply();
        stage.act();
        stage.draw();
    }

    /**
     * Resize hud accordingly to window size.
     *
     * @param width window width
     * @param height window height
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        viewport.setWorldSize(viewport.getWorldWidth() * 10, viewport.getWorldHeight() * 10);
    }

    /**
     * Dispose hud.
     */
    public void dispose() {
        stage.dispose();
    }
}

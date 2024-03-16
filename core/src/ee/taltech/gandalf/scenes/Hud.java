package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
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

    public Hud(Camera camera, PlayerCharacter player) {
        Viewport viewport = new ScreenViewport(camera);
        this.player = player;

        inventorySlotImages = new Image[3];
        itemImages = new Image[3];

        stage = new Stage(viewport);
        root = createInventoryTable();
        stage.addActor(root);
    }

    public Table createInventoryTable() {
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        for (int i = 0; i < 3; i++) {
            Stack stack = new Stack();
            inventorySlotImages[i] = new Image();
            itemImages[i] = new Image();

            stack.add(inventorySlotImages[i]);
            stack.add(itemImages[i]);

            table.add(stack).size(64, 64);
        }
        return table;
    }

    private void drawInventory() {
        for (int i = 0; i < 3; i++) {
            Image inventorySlotImage = inventorySlotImages[i];
            Texture inventoryTexture;
            if (player.getSelectedSlot() == i) {
                inventoryTexture = new Texture("ActiveInventorySlot.png");
            } else {
                inventoryTexture = new Texture("NotActiveInventorySlot.png");
            }
            inventorySlotImage.setDrawable(new TextureRegionDrawable(new TextureRegion(inventoryTexture)));

            Image itemImage = itemImages[i];
            itemImage.setDrawable(getDrawable(i));
        }
    }

    private Drawable getDrawable(int i) {
        Texture itemTexture = null;
        // Set texture for item image when necessary
        if (i == 0 && player.getInventory()[i] != null) {
            itemTexture = player.getInventory()[i].getTexture();
        }
        if (itemTexture != null) {
            return new TextureRegionDrawable(new TextureRegion(itemTexture));
        }
        return null;
    }

    public void draw() {
        drawInventory();

        root.setPosition(player.xPosition - (float) Gdx.graphics.getWidth() / 2,
                player.yPosition - (float) Gdx.graphics.getHeight() / 2);

        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    public void dispose() {
        stage.dispose();
    }
}


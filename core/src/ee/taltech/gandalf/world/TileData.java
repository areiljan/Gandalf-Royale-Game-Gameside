package ee.taltech.gandalf.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ee.taltech.gandalf.entities.Entity;

/**
 * Class for storing information about one tile.
 * Implements entity for sorting purposes.
 */
public class TileData implements Entity {
    public Sprite sprite;
    public int x;
    public int y;
    private final TiledMapTileLayer tileLayer;

    /**
     * @param sprite    containing an image
     * @param x         cord of a whole Tiled map
     * @param y         cord of a whole Tiled map
     * @param tileLayer tile is in
     */
    public TileData(Sprite sprite, int x, int y, TiledMapTileLayer tileLayer) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.tileLayer = tileLayer;
    }

    /**
     * @return TileLayer name created in Tiled
     */
    public String getTileName() {
        return tileLayer.getName();
    }

    /**
     * @return integer y cord
     */
    @Override
    public float getYPosition() {
        return y;
    }

    /**
     * @return integer x cord
     */
    @Override
    public float getXPosition() {
        return x;
    }

    /**
     * @return Asset height
     */
    @Override
    public float getHeight() {
        return sprite.getHeight();
    }
}

package ee.taltech.gandalf.world;

public class MapObjectData {
    public String type;
    public float x;
    public float y;
    public float width;
    public float height;
    public float[] vertices;
    public int tileX;
    public int tileY;
    public float textureRegionWidth;

    public MapObjectData(String type, float x, float y, float width, float height, int tileX, int tileY, float textureRegionWidth, float[] vertices) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tileX = tileX;
        this.tileY = tileY;
        this.textureRegionWidth = textureRegionWidth;
        this.vertices = vertices;
    }
    public MapObjectData(String type, float x, float y, float width, float height, int tileX, int tileY, float textureRegionWidth) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tileX = tileX;
        this.tileY = tileY;
        this.textureRegionWidth = textureRegionWidth;
    }
}

package ee.taltech.gandalf.components;

import java.util.List;

public class Constants {
    public static float PPM = 32;
    public static Integer tileWidthOrHeight = 32;
    public static Integer mapWidthOrHeight = 300;
    public static float movementSpeed = 5;
    public static float interpFactor = 0.3f; // For overwrite smoothness
    public static float maxTilesSeenWidth = 22;
    public static float maxTilesSeenHeight = 12;
    public static float minChunksSeen = 10f;
    public static float statusBarWidthPx = 60;
    public static float MOVEMENT_THRESHOLD = 0.5f;
    public static final int FIRST_ZONE_TEXTURE_SIZE = 375;
    public static final int SECOND_ZONE_TEXTURE_SIZE = 1200;
    public static final int THIRD_ZONE_TEXTURE_SIZE = 460;
    public static final int[] BACKGROUND_LAYERS = {0, 1, 2, 3, 4, 5, 6, 7, 10}; // Magic order of layers :).
    public static final List<String> LAYERS_TO_BE_ORDERED = List.of("props on props", "props", "Merchant", "rocks", "vegetation");
    public static final int PUMPKIN_WIDTH = 1;
    public static final int PUMPKIN_HEIGHT = 1;

}

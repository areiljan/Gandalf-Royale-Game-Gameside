package ee.taltech.gandalf.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.gandalf.entities.PlayerCharacter;

public class Hud {

    private Stage stage;
    private final Viewport viewport;
    private final PlayerCharacter player;

    public Hud(SpriteBatch spriteBatch, Viewport viewport, PlayerCharacter player) {
        this.viewport = viewport;
        this.player = player;

        stage = new Stage(viewport, spriteBatch);

        Table root = new Table();
        root.top();
    }
}

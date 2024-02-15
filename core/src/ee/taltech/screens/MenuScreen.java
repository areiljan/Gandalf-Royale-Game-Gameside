package ee.taltech.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.taltech.gandalf.GandalfRoyale;


public class MenuScreen extends ScreenAdapter {

    GandalfRoyale game;
    Stage stage;

    // Background picture constants
    private static final Texture BACKGROUND_TEXTURE = new Texture("menuBackground.png");
    private static final Sprite BACKGROUND_SPRITE = new Sprite(BACKGROUND_TEXTURE);

    public MenuScreen(GandalfRoyale game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Creating a Stage (place, where things can be put on)
        stage = new Stage();

        // Reading input
        Gdx.input.setInputProcessor(stage);

        // Creating a Table (place, where things can be put in and positioned)
        Table table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Styling heading
        Label.LabelStyle headingStyle = new Label.LabelStyle(game.font, Color.FIREBRICK);

        // Creating heading
        Label heading = new Label("GandalfRoyale", headingStyle);
        heading.setFontScale(6);

        // Styling button
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
//        textButtonStyle.up = skin.getDrawable("button.up")
//        textButtonStyle.down = skin.getDrawable("button.down")
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = game.font;
        textButtonStyle.fontColor = Color.BLACK;

        // Creating Play button
        TextButton buttonPlay = new TextButton("Play", textButtonStyle);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LobbyScreen(game));
            }
        });
        buttonPlay.pad(20);
        buttonPlay.getLabel().setFontScale(3);

        // Creating Settings button
        TextButton buttonSettings = new TextButton("Settings", textButtonStyle);
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                game.setScreen(new SettingsScreen(game));
            }
        });
        buttonSettings.pad(20);
        buttonSettings.getLabel().setFontScale(3);


        // Creating Exit button
        TextButton buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.pad(20);
        buttonExit.getLabel().setFontScale(3);

        // Creating credits(creator names) label
        Label credits = new Label("*Artur_Reiljan*Rasmus_Kilkson*Ramus_Raasuke*", headingStyle);

        // Adding elements to the Table
        table.add(heading);
        table.row();
        table.padBottom(50);
        table.add(buttonPlay);
        table.row();
        table.add(buttonSettings);
        table.row();
        table.add(buttonExit);
        table.row();
        table.add(credits);

        // Adding the Table to the Stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Draw background picture
        game.batch.begin();
        BACKGROUND_SPRITE.draw(game.batch);
        game.batch.end();

        // Start stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}

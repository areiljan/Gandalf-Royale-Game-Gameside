package ee.taltech.gandalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.gandalf.components.StartedGame;

import java.util.ArrayList;
import java.util.List;

public class GameEndScreen extends ScreenAdapter {
    private final GandalfRoyale game;
    private final StartedGame gameInstance;
    private final Integer winnerId;

    private final List<Integer> deadPlayers;

    private final Stage stage;
    private final Table root;

    private TextButton buttonBack;

    public GameEndScreen(GandalfRoyale game, StartedGame gameInstance, Integer winnerId) {
        this.game = game;
        this.gameInstance = gameInstance;
        this.winnerId = winnerId;

        deadPlayers = gameInstance.getDeadPlayers();

        stage = new Stage(game.viewport, game.batch);

        root = new Table();
        root.top();
        root.setFillParent(true);

        stage.addActor(root);

        createUI(); // Create UI
        setupButtonListener(); // Set listener for menu button
    }

    private void createUI() {
        // Styling heading
        Label.LabelStyle labelStyle = new Label.LabelStyle(GandalfRoyale.font, Color.FIREBRICK);

        // Styling buttons
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.pressedOffsetX = 2;
        textButtonStyle.pressedOffsetY = -2;
        textButtonStyle.font = GandalfRoyale.font;
        textButtonStyle.fontColor = Color.WHITE;

        // Heading label
        Label heading = new Label(String.format("Player %s has won the game", winnerId), labelStyle);
        heading.setFontScale(3);

        // Death order heading label
        Label deathOrderHeading = new Label("Player Ranking:", labelStyle);
        deathOrderHeading.setFontScale(2);

        // Death order label
        Label deathOrder = new Label("", labelStyle);
        deathOrder.setText(getDeathOrderText());

        // Back button
        buttonBack = new TextButton("Back to Menu", textButtonStyle);
        buttonBack.pad(20);
        buttonBack.getLabel().setFontScale(2);

        // Adding elements to the Table
        root.add(heading);
        root.row();
        root.add(deathOrderHeading);
        root.row();
        root.add(deathOrder);
        root.row();
        root.add(buttonBack);
    }

    /**
     * Create death order text.
     *
     * @return string of death order text
     */
    private String getDeathOrderText() {
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < deadPlayers.size(); i++) {
            int ranking = 1 + deadPlayers.size() - i;
            String row = String.format("%s.  player  %s", ranking, deadPlayers.get(i));
            if (i != 0) {
                row += "\n";
            }
            stringList.add(row);
        }

        stringList = stringList.reversed();
        StringBuilder result = new StringBuilder();
        for (String row : stringList) {
            result.append(row);
        }
        return result.toString();
    }

    /**
     * Set up listeners for buttons to work.
     */
    private void setupButtonListener() {
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.screenController.setMenuScreen();
            }
        });
    }

    /**
     * Show GameEndScreen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Render GameEndScreen.
     *
     * @param delta delta time
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        stage.act();
        stage.draw();
    }

    /**
     * Resize GameEndScreen.
     *
     * @param width new width
     * @param height new height
     */
    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    /**
     * Hide GameEndScreen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        dispose();
    }

    /**
     * Dispose GameEndScreen.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

package ee.taltech.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.gandalf.GandalfRoyale;
import ee.taltech.network.NetworkClient;
import jdk.internal.net.http.common.ConnectionExpiredException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameScreen extends ScreenAdapter {

    GandalfRoyale game;
    NetworkClient nc;
    Texture img;
    Texture opponentImg;
    int x, y;

    private Map<Integer, SpriteBatch> opponentBatches;
    public GameScreen(GandalfRoyale game) {
        this.game = game;
        this.nc = game.nc;
        opponentBatches = new HashMap<>();
        nc.addListener();
        x = 0;
        y = 0;
    }

    @Override
    public void show() {
        img = new Texture("wizard.png");
        opponentImg = new Texture("opponent.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 10;
            nc.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 10;
            nc.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 10;
            nc.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 10;
            nc.sendUDP(x + "," + y);
        }
        game.batch.begin();
        game.batch.draw(img, x, y);
        game.batch.end();
        for (Map.Entry<Integer, String> entry : nc.receivedGameObjects.entrySet()) {
            int id = entry.getKey();
            if (id == nc.clientId) continue;
            if (!opponentBatches.containsKey(id)) {
                opponentBatches.put(id, new SpriteBatch());
            }
            SpriteBatch b = opponentBatches.get(id);
            String[] coordinates = entry.getValue().split(",");
            b.begin();
            b.draw(opponentImg, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
            b.end();

        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        img.dispose();
        opponentImg.dispose();
    }
}

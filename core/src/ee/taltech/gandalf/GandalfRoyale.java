package ee.taltech.gandalf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GandalfRoyale extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Texture opponentImg;

    private int x = 0, y = 0;
    private Client client;

    private String lastReceived;
    private Map<Integer, String> receivedGameObjects = new HashMap<>();
    private Map<Integer, SpriteBatch> opponentBatches = new HashMap<>();

    private int clientId;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("wizard.png");
        opponentImg = new Texture("opponent.png");
        client = new Client();
        client.start();
        try {
            client.connect(5000, "localhost", 8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Kryo kryo = client.getKryo();
        kryo.register(HashMap.class);
        clientId = client.getID();
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                System.out.println(object);
                if (object instanceof Map) {
                    receivedGameObjects = (Map<Integer, String>) object;
                }
            }
        }));
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= 10;
            client.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += 10;
            client.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += 10;
            client.sendUDP(x + "," + y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= 10;
            client.sendUDP(x + "," + y);
        }
        batch.begin();
        batch.draw(img, x, y);
        batch.end();
        for (Map.Entry<Integer, String> entry : receivedGameObjects.entrySet()) {
            int id = entry.getKey();
            if (id == clientId) continue;
            if (!opponentBatches.containsKey(id)) {
                opponentBatches.put(id, new SpriteBatch());
            }
            SpriteBatch b = opponentBatches.get(id);
            String[] coordinates = entry.getValue().split(",");
            b.begin();
            b.draw(opponentImg, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
            b.end();
        }
//        if (lastReceived != null) {
//            System.out.println(lastReceived);
//            String[] opponentDatas = lastReceived.split("\\|");
//            for (String opponentData : opponentDatas) {
//                if (opponentData.isEmpty()) continue;
//                String [] parts = opponentData.split(":");
//                int id = Integer.parseInt(parts[0]);
//                if (id == clientId) continue;
//                if (!opponentBatches.containsKey(id)) {
//                    opponentBatches.put(id, new SpriteBatch());
//                }
//                String[] coordinates = parts[1].split(",");
//                opponentBatches.get(id).begin();
//                opponentBatches.get(id).draw(opponentImg, Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
//                opponentBatches.get(id).end();
//            }
//            lastReceived = null;
//        }
//        for (SpriteBatch b : opponentBatches.values()) {
//            b.begin();
//            b.draw();
//        }
    }

    @Override
    public void dispose() {
        client.close();
        try {
            client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        batch.dispose();
        img.dispose();
    }
}

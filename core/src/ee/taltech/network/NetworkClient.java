package ee.taltech.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import jdk.internal.net.http.common.ConnectionExpiredException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetworkClient {

    public Client client;
    public Map<Integer, String> receivedGameObjects = new HashMap<>();
    public int clientId;

    public NetworkClient() throws RuntimeException {
        client = new Client();
        client.start();
        this.connect();
        Kryo kryo = client.getKryo();
        kryo.register(HashMap.class);
        clientId = client.getID();
    }

    public void connect() {
        try {
            client.connect(5000, "localhost", 8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        client.close();
    }

    public void sendTCP(Object sendable) {
        client.sendTCP(sendable);
    }

    public void sendUDP(Object sendable) {
        client.sendUDP(sendable);
    }

    public void addListener() {
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Map) {
                    receivedGameObjects = (Map<Integer, String>) object;
                }
            }
        }));
    }
}

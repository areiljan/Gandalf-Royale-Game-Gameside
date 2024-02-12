package ee.taltech.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.KeyPress;
import ee.taltech.network.messages.Position;

import java.io.IOException;
import java.util.HashMap;

public class NetworkClient {

    public Client client;
    public Position receivedPosition;
    public int clientId;

    public NetworkClient() throws RuntimeException {
        client = new Client();
        client.start();
        connect();
        registerKryos();
        clientId = client.getID();
    }

    public void registerKryos(){
        // Add sendable data structures.
        Kryo kryo = client.getKryo();
        kryo.register(HashMap.class);
        kryo.register(KeyPress.class);
        kryo.register(KeyPress.Direction.class);
        kryo.register(Position.class);
        kryo.addDefaultSerializer(KeyPress.Direction.class, DefaultSerializers.EnumSerializer.class);
    }
    public void connect() {
        // Connect client with the server.
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
        // Listen to incoming data from the connected server.
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                // Check if the incoming object is a Position message.
                if (object instanceof Position) {
                    receivedPosition = (Position) object;
                }
            }
        }));
    }
}

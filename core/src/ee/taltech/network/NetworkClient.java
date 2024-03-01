package ee.taltech.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.listeners.HealthAndManaListener;
import ee.taltech.network.listeners.LobbyListener;
import ee.taltech.network.listeners.LobbyRoomListener;
import ee.taltech.network.listeners.PlayerPositionListener;
import ee.taltech.network.messages.*;
import ee.taltech.screen.ScreenController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NetworkClient {

    ScreenController screenController;
    public Client client;
    public int clientId;
    List<Listener> listeners;
    LobbyListener lobbyListener;
    LobbyRoomListener lobbyRoomListener;
    PlayerPositionListener playerPositionListener;
    HealthAndManaListener healthAndManaListener;


    /**
     * Construct NetworkClient.
     *
     * @throws RuntimeException if connecting ti server fails
     */
    public NetworkClient(ScreenController screenController) throws RuntimeException {
        this.screenController = screenController;

        client = new Client(); // Create new client
        client.start(); // Start client

        connect(); // Connect to server
        registerKryos(); // Register sendable data

        clientId = client.getID(); // Client id variable
        listeners = new ArrayList<>();
    }

    /**
     * Register acceptable classes for sending between server and client.
     */
    public void registerKryos(){
        // Add sendable data structures.
        Kryo kryo = client.getKryo();
        kryo.register(java.util.ArrayList.class);
        kryo.register(Position.class);
        kryo.register(Join.class);
        kryo.register(Leave.class);
        kryo.register(LobbyCreation.class);
        kryo.register(LobbyDismantle.class);
        kryo.register(GetLobbies.class);
        kryo.register(StartGame.class);
        kryo.register(KeyPress.class);
        kryo.register(KeyPress.Direction.class);
        kryo.register(UpdateHealth.class);
        kryo.register(UpdateMana.class);
        kryo.addDefaultSerializer(KeyPress.Direction.class, DefaultSerializers.EnumSerializer.class);
    }

    /**
     * Create connection to server.
     */
    public void connect() {
        // Connect client with the server.
        try {
            client.connect(5000, "localhost", 8080, 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Disconnect client.
     */
    public void disconnect() {
        client.close();
    }

    /**
     * Sending things to server through TCP.
     *
     * @param sendable object that will be sent through TCP to server
     */
    public void sendTCP(Object sendable) {
        client.sendTCP(sendable);
    }

    /**
     * Sending things to server through UDP.
     *
     * @param sendable object that will be sent through UDP to server
     */
    public void sendUDP(Object sendable) {
        client.sendUDP(sendable);
    }

    /**
     * Add LobbyListener.
     */
    public void addLobbyListener() {
        lobbyListener = new LobbyListener(screenController);
        listeners.add(lobbyListener);
        client.addListener(lobbyListener);
    }

    /**
     * Add LobbyRoomListener.
     */
    public void addLobbyRoomListener() {
        lobbyRoomListener = new LobbyRoomListener(screenController);
        listeners.add(lobbyRoomListener);
        client.addListener(lobbyRoomListener);
    }

    /**
     * Add Game listeners.
     */
    public void addGameListeners() {
        playerPositionListener = new PlayerPositionListener(screenController);
        healthAndManaListener = new HealthAndManaListener(screenController);

        listeners.add(playerPositionListener);
        listeners.add(healthAndManaListener);

        client.addListener(playerPositionListener);
        client.addListener(healthAndManaListener);
        // Add here more game listeners
    }

    /**
     * Remove all listeners.
     */
    public void removeAllListeners() {
        for (Listener listener : listeners) {
            client.removeListener(listener);
        }
    }
}

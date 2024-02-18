package ee.taltech.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.*;
import ee.taltech.screens.LobbyScreen;
import ee.taltech.utilities.Lobby;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkClient {

    public Client client;
    public Position receivedPosition;
    public int clientId;
    public Map<String, Object> listeners;
    public Listener.ThreadedListener threadedListener;

    /**
     * Construct NetworkClient.
     *
     * @throws RuntimeException if connecting ti server fails
     */
    public NetworkClient() throws RuntimeException {
        client = new Client(); // Create new client
        client.start(); // Start client

        connect(); // Connect to server
        registerKryos(); // Register sendable data

        clientId = client.getID(); // Client id variable
        listeners = new HashMap<>(); // Map of all listeners
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
        kryo.register(KeyPress.class);
        kryo.register(KeyPress.Direction.class);
        kryo.addDefaultSerializer(KeyPress.Direction.class, DefaultSerializers.EnumSerializer.class);
    }

    /**
     * Register new client side listener for NetworkClient.
     *
     * @param type string that tells which class is registering as listener
     * @param listener class instants that wants to register as listener
     */
    public void registerListener(String type, Object listener) {
        listeners.put(type, listener); // Add new listener to listener Map
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
     * Create listener for listening messages from sever.
     */
    public void addListener() {
        // Listen to incoming data from the connected server.
        threadedListener = new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object incomingData) {
                Lobby lobby;
                LobbyScreen lobbyScreen;

                switch (incomingData){
                    case Position position: // Position message
                        receivedPosition = position;
                        break;
                    case GetLobbies givenLobby: // GetLobbies message
                        lobby = new Lobby(givenLobby.name, givenLobby.gameId, givenLobby.players);
                        lobbyScreen = (LobbyScreen) listeners.get("LobbyScreen");
                        lobbyScreen.showLobby(lobby);
                        break;
                    case LobbyCreation newLobby: // LobbyCreation message
                        lobby = new Lobby(newLobby.gameName, newLobby.gameId,
                                new ArrayList<>(List.of(newLobby.hostId)));
                        lobbyScreen = (LobbyScreen) listeners.get("LobbyScreen");
                        lobbyScreen.showLobby(lobby);
                        break;
                    case Join join: // Join message
                        lobbyScreen = (LobbyScreen) listeners.get("LobbyScreen");
                        lobbyScreen.joinLobby(join.gameId, join.playerId);
                        break;
                    case Leave leave: // Leave message
                        lobbyScreen = (LobbyScreen) listeners.get("LobbyScreen");
                        lobbyScreen.leaveLobby(leave.gameId, leave.playerId);
                        break;
                    case LobbyDismantle givenLobby: // LobbyDismantle message
                        lobbyScreen = (LobbyScreen) listeners.get("LobbyScreen");
                        lobbyScreen.dismantleLobby(givenLobby.gameId);
                        break;
                    case FrameworkMessage.KeepAlive ignored: // KeepAlive message
                        break;
                    default: // If something else comes through
                        throw new IllegalStateException("Unexpected value: " + incomingData);
                }
            }
        });
        client.addListener(threadedListener); // Add created listener to client
    }

    /**
     * Remove sever listener.
     */
    public void removeListener() {
        client.removeListener(threadedListener);
    }
}

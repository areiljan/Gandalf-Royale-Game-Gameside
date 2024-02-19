package ee.taltech.network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.network.messages.*;
import ee.taltech.screen.ScreenController;
import ee.taltech.screen.screens.LobbyScreen;
import ee.taltech.utilities.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyListener extends Listener {

    ScreenController screenController;

    /**
     * Construct LobbyListener.
     *
     * @param screenController game screen controller
     */
    public LobbyListener(ScreenController screenController) {
        this.screenController = screenController;
    }

    /**
     * Received messages from server.
     *
     * @param connection server connection
     * @param incomingData message from server
     */
    @Override
    public void received(Connection connection, Object incomingData) {
        Lobby lobby;
        LobbyScreen lobbyScreen = screenController.getLobbyScreen();
        switch (incomingData){
            case GetLobbies givenLobby: // GetLobbies message
                lobby = new Lobby(givenLobby.name, givenLobby.gameId, givenLobby.players);
                lobbyScreen.showLobby(lobby);
                break;
            case LobbyCreation newLobby: // LobbyCreation message
                lobby = new Lobby(newLobby.gameName, newLobby.gameId,
                        new ArrayList<>(List.of(newLobby.hostId)));
                if (newLobby.hostId == connection.getID()) {
                    lobbyScreen.goToLobbyRoomScreen(lobby);
                } else {
                    lobbyScreen.showLobby(lobby);
                }
                break;
            case Join join: // Join message
                lobbyScreen.joinLobby(join.gameId, join.playerId);
                break;
            case Leave leave: // Leave message
                lobbyScreen.leaveLobby(leave.gameId, leave.playerId);
                break;
            case LobbyDismantle givenLobby: // LobbyDismantle message
                lobbyScreen.dismantleLobby(givenLobby.gameId);
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

package ee.taltech.gandalf.network.listeners.lobby;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.gandalf.network.messages.lobby.StartGame;
import ee.taltech.gandalf.network.messages.lobby.Join;
import ee.taltech.gandalf.network.messages.lobby.Leave;
import ee.taltech.gandalf.screens.ScreenController;
import ee.taltech.gandalf.screens.LobbyRoomScreen;

public class LobbyRoomListener extends Listener {

    ScreenController screenController;

    /**
     * Construct LobbyRoomListener.
     *
     * @param screenController game screen controller
     */
    public LobbyRoomListener(ScreenController screenController) {
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
        LobbyRoomScreen lobbyRoomScreen = screenController.getLobbyRoomScreen();
        switch (incomingData){
            case Join join: // Join message
                lobbyRoomScreen.joinLobby(join.playerId);
                break;
            case Leave leave: // Leave message
                lobbyRoomScreen.leaveLobby(leave.playerId);
                break;
            case StartGame game: // StartGame message
                lobbyRoomScreen.startGame();
                break;
            default: // Ignore if something else comes through
                break;
        }
    }
}

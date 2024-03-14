package ee.taltech.gandalf.network.messages.lobby;

public class StartGame {
    public Integer gameId;

    public StartGame(){
    }
    /**
     * Construct StartGame message.
     *
     * @param gameId given game ID
     */
    public StartGame(Integer gameId) {
        this.gameId = gameId;
    }
}
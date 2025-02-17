package pelea.pelien.globals;
import pelea.pelien.network.NetworkActionsListener;
import pelea.pelien.network.NetworkManager;

public abstract class GameData {

    public static GameState gameState = GameState.WAITING;
    public static NetworkActionsListener networkListener = new NetworkManager();

}

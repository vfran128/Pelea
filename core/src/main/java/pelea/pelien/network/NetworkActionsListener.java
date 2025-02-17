package pelea.pelien.network;

public interface NetworkActionsListener {

    void startGame();
    void gameOver();
    void spawnEntity(float x, float y, String luchador);
    void moveEntity(float x, float y, String luchador);

    void flipEntity(String facingRight, String luchador);
}

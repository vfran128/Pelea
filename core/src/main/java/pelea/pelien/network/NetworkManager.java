package pelea.pelien.network;

import com.badlogic.gdx.Gdx;
import pelea.pelien.MainScreen;
import pelea.pelien.ScreenManager;

public class NetworkManager implements NetworkActionsListener{

    private MainScreen mainScreen;
    @Override
    public void startGame() {
        Gdx.app.postRunnable(() -> {
            ScreenManager.getInstance().setScreen(new MainScreen());
            if (ScreenManager.getInstance().getGame().getScreen() instanceof MainScreen) {
                this.mainScreen = (MainScreen) ScreenManager.getInstance().getGame().getScreen();
            }
        });
    }

    @Override
    public void gameOver() {

    }

    @Override
    public void spawnEntity(float x, float y, String luchador) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.spawnEntity(x,y,luchador);
        });
    }

    @Override
    public void moveEntity(float x, float y, String luchador) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.moveEntity(x,y,luchador);
        });
    }

    @Override
    public void flipEntity(String facingRight, String luchador) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.flipEntity(facingRight, luchador);
        });
    }

    @Override
    public void barraVida(String nombre, float porcentaje, String color) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.barraVida(nombre, porcentaje, color);
        });
    }

    @Override
    public void timerValor(int valor) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.timerValor(valor);
        });
    }
    @Override
    public void terminarJuego(String mensjae) {
        Gdx.app.postRunnable(() -> {
            this.mainScreen.terminarJuego(mensjae);
        });
    }
}

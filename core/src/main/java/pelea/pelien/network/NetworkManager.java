package pelea.pelien.network;
import com.badlogic.gdx.Gdx;
import jdk.tools.jmod.Main;
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
    public void endGame() {

    }

    @Override
    public void jugadorDesconectado() {
        Gdx.app.postRunnable(() -> {
          this.mainScreen.jugadorDesconectado();
        });
    }
}

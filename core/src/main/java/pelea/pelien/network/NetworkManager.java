package pelea.pelien.network;
import com.badlogic.gdx.Gdx;
import pelea.pelien.MainScreen;
import pelea.pelien.ScreenManager;

public class NetworkManager implements NetworkActionsListener{

    @Override
    public void startGame() {

        Gdx.app.postRunnable(() -> {
            ScreenManager.getInstance().setScreen(new MainScreen());
        });
    }


    @Override
    public void endGame() {

    }
}

package pelea.pelien;

import com.badlogic.gdx.Game;
import pelea.pelien.globals.NetworkData;
import pelea.pelien.network.ServerThread;

public class Pelea extends Game {
    @Override
    public void create() {
        // Inicializar el ScreenManager
        ScreenManager.getInstance().initialize(this);

        // Establecer la pantalla inicial
        ScreenManager.getInstance().setScreen(new MenuScreen());
    }
    @Override
    public void dispose() {
        ScreenManager.getInstance().dispose();
        super.dispose();
        NetworkData.serverThread.sendMessageToAll("terminarjuego!" + "SERVIDOR DESCONECTADO");
        NetworkData.serverThread.end();
    }
}

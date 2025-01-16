package pelea.pelien;

import com.badlogic.gdx.Game;

public class Pelea extends Game {
    @Override
    public void create() {
        // Inicializar el ScreenManager
        ScreenManager.getInstance().initialize(this);

        // Establecer la pantalla inicial
        ScreenManager.getInstance().setScreen(new MenuScreen());
    }
}

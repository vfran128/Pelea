package pelea.pelien;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private static ScreenManager instance; // Instancia única del ScreenManager
    private Game game; // Referencia al Game principal

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Inicializa el ScreenManager con la referencia al juego principal
    public void initialize(Game game) {
        this.game = game;
    }

    // Cambia la pantalla actual
    public void setScreen(Screen newScreen) {
        if (game != null) {
            Screen currentScreen = game.getScreen();
            if (currentScreen != null) {
                currentScreen.dispose(); // Libera recursos de la pantalla actual
            }
            game.setScreen(newScreen); // Establece la nueva pantalla
        }
    }
}

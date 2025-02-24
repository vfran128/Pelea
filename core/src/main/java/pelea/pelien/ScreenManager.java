package pelea.pelien;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.InputProcessor;
import pelea.pelien.globals.GameData;
import pelea.pelien.globals.NetworkData;

public class ScreenManager {
    private static ScreenManager instance; // Instancia única del ScreenManager
    private Game game; // Referencia al Game principal
    private Stage stage; // El Stage para manejar la UI

    private ScreenManager() {}

    // Devuelve la instancia única del ScreenManager
    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    // Inicializa el ScreenManager con la referencia al juego principal
    public void initialize(Game game) {
        this.game = game;
        this.stage = new Stage(); // Inicializa el Stage
    }

    public Game getGame() {
        return game;
    }

    // Cambia la pantalla actual
    public void setScreen(Screen newScreen) {
        if (game != null) {
            Screen currentScreen = game.getScreen();
            if (currentScreen != null) {
                currentScreen.dispose(); // Libera recursos de la pantalla actual
            }
            game.setScreen(newScreen); // Establece la nueva pantalla

            // Establece el input processor para la nueva pantalla
            if (newScreen instanceof InputProcessor) {
                com.badlogic.gdx.Gdx.input.setInputProcessor((InputProcessor) newScreen);
            } else {
                com.badlogic.gdx.Gdx.input.setInputProcessor(stage);
            }
        }
    }

    // Devuelve el Stage actual
    public Stage getStage() {
        return stage;
    }

    // Limpiar recursos cuando se cierra el ScreenManager
    public void dispose() {
        if (stage != null) {
            System.out.println("¡Saliendo de la pantalla!");

            if (NetworkData.clientThread != null) {
                NetworkData.clientThread.sendMessage("disconnect!" + GameData.clientNumber);
                NetworkData.clientThread.end();
                NetworkData.clientThread = null;
            }
            stage.dispose();
        }

    }
}

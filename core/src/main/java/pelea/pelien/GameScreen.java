package pelea.pelien;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends ScreenAdapter {
    @Override
    public void show() {
        System.out.println("Pantalla de Juego mostrada");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(-1)) { // Cambiar pantalla al presionar una tecla
            ScreenManager.getInstance().setScreen(new MainScreen());
        }
    }

    @Override
    public void dispose() {
        System.out.println("Pantalla de Juego liberada");
    }
}

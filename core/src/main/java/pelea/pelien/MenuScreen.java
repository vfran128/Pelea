package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import pelea.pelien.globals.GameData;
import pelea.pelien.globals.NetworkData;
import pelea.pelien.network.ClientThread;


public class MenuScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Stage stage;
    private Botonera botonera;

    @Override
    public void show() {
        batch = new SpriteBatch();
        image = new Texture("cat.png");

        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Establece el procesador de entradas

        botonera = new Botonera(stage);
        botonera.createButton("Conectarse", Color.RED, Color.TEAL, Gdx.graphics.getWidth() / 2f - 310, Gdx.graphics.getHeight() / 2f + 25, this::accionBoton1);
        botonera.createButton("Salir", Color.RED, Color.TEAL, Gdx.graphics.getWidth() / 2f - 310, Gdx.graphics.getHeight() / 2f - 75, this::accionSalir);
        NetworkData.clientThread = new ClientThread();
        NetworkData.clientThread.start();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Gdx.input.setInputProcessor(stage);

        batch.begin();
        batch.draw(image, 0, 0);
        batch.end();



        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        stage.dispose();
    }

    private void accionBoton1() {
        System.out.println("¡Botón 1 presionado!, conectando...");
        NetworkData.clientThread.sendMessage("connect");
    }

    private void accionSalir() {
        System.out.println("¡Saliendo de la pantalla!");
        Gdx.app.exit();
    }
}


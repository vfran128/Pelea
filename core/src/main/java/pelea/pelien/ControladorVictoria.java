package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class ControladorVictoria extends ScreenAdapter {
    private SpriteBatch batch = new SpriteBatch();
    private Botonera botonera;
    private String mensaje;
    private BitmapFont font = new BitmapFont();

    @Override
    public void show() {
        batch = new SpriteBatch();
        botonera = new Botonera(ScreenManager.getInstance().getStage());

        // Crear botones de "Reiniciar" y "Salir"
        botonera.createButton("Reiniciar", Color.GREEN, Color.DARK_GRAY, Gdx.graphics.getWidth() / 2f , Gdx.graphics.getHeight() / 2f + 25, this::accionReiniciar);
        botonera.createButton("Volver al menu", Color.RED, Color.DARK_GRAY, Gdx.graphics.getWidth() / 2f , Gdx.graphics.getHeight() / 2f - 25, this::accionMenu);

    }

    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        // Asegúrate de que el InputProcessor esté configurado
        Gdx.input.setInputProcessor(ScreenManager.getInstance().getStage());

        batch.begin();
        // Mostrar el mensaje en la pantalla
        font.draw(batch, mensaje, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + 70);
        batch.end();
        show();
        // Actualizar y dibujar el Stage
        ScreenManager.getInstance().getStage().act(delta);
        ScreenManager.getInstance().getStage().draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void accionReiniciar() {
        System.out.println("¡Reiniciando el juego!");
        // Lógica para reiniciar el juego
        ScreenManager.getInstance().setScreen(new MainScreen());
    }

    private void accionMenu() {
        System.out.println("Volviendo al menu");
        ScreenManager.getInstance().setScreen(new MenuScreen());
    }
}


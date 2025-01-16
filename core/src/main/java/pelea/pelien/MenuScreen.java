package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Stage stage;

    @Override
    public void show() {
        // Fondo
        batch = new SpriteBatch();
        image = new Texture("cat.png");

        // Configuración del Stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Crear los botones
        createButton("Botón 1", Color.RED, Color.TEAL, Gdx.graphics.getWidth() / 2f - 310, Gdx.graphics.getHeight() / 2f + 25, this::accionBoton1);
        createButton("Botón 2", Color.RED, Color.TEAL, Gdx.graphics.getWidth() / 2f - 310, Gdx.graphics.getHeight() / 2f - 25, this::accionBoton2);
        createButton("Salir", Color.RED, Color.TEAL, Gdx.graphics.getWidth() / 2f - 310, Gdx.graphics.getHeight() / 2f - 75, this::accionSalir);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);


        batch.begin();
        batch.draw(image, 0, 0);
        batch.end();

        // Actualizar y dibujar el Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
        stage.dispose();
    }

    // Método para crear botones genéricos
    private void createButton(String text, Color upColor, Color downColor, float x, float y, Runnable action) {
        // Configuración del estilo del botón
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = createColoredDrawable(upColor);
        textButtonStyle.down = createColoredDrawable(downColor);
        textButtonStyle.fontColor = Color.BLACK;

        // Crear el botón
        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);

        // Agregar un listener para ejecutar la acción
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float buttonX, float buttonY) {
                action.run();
            }
        });

        // Agregar el botón al Stage
        stage.addActor(button);
    }

    // Acción del primer botón
    private void accionBoton1() {
        System.out.println("¡Botón 1 presionado!");
        ScreenManager.getInstance().setScreen(new MainScreen());
    }

    // Acción del segundo botón
    private void accionBoton2() {
        System.out.println("¡Botón 2 presionado!");
        // Agregar lógica específica aquí
    }

    // Acción del botón de salir
    private void accionSalir() {
        System.out.println("¡Saliendo de la pantalla!");
        Gdx.app.exit(); // Cierra la aplicación
    }

    // Crear un Drawable con un color específico
    private Drawable createColoredDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }
}


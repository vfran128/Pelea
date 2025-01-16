package pelea.pelien;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Jugador jugadorA;
    private Jugador jugadorB;
    private Piso piso;

    @Override
    public void show() {
        batch = new SpriteBatch();
        jugadorA = new Jugador("sagatstand.png", 100, 200, Input.Keys.W, Input.Keys.A, Input.Keys.D);
        jugadorB = new Jugador("sagatstand.png", 300, 200, Input.Keys.UP, Input.Keys.LEFT, Input.Keys.RIGHT);
        piso = new Piso(0, 50, Gdx.graphics.getWidth(), 20, "piso.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.42f, 0.63f, 0.86f, 1f);

        // Actualizar jugadores
        jugadorA.actualizar(delta);
        jugadorB.actualizar(delta);

        // Colisiones con el piso
        Rectangle pisoRect = piso.getRectangulo();
        jugadorA.resolverColision(pisoRect);
        jugadorB.resolverColision(pisoRect);

        // Dibujar objetos
        batch.begin();
        jugadorA.renderizar(batch);
        jugadorB.renderizar(batch);
        piso.renderizar(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        jugadorA.dispose();
        jugadorB.dispose();
        piso.dispose();
    }
}

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
    private float timeElapsed = 0f;
    @Override
    public void show() {
        batch = new SpriteBatch();
        jugadorA = new Jugador("sagatstand.png", "sagatkick.png","sagatshot.png", 150, 150, Input.Keys.W, Input.Keys.A, Input.Keys.D, Input.Keys.P, Input.Keys.O);
        jugadorB = new Jugador("sagatstand.png", "sagatkick.png","sagatshot.png", 400, 150, Input.Keys.UP, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.L, Input.Keys.K);
        piso = new Piso(0, 50, Gdx.graphics.getWidth(), 20, "piso.png");
    }

    @Override
    public void render(float delta) {
        float attackCooldown = 0.5f;
        ScreenUtils.clear(0.42f, 0.63f, 0.86f, 1f);

        jugadorA.actualizar(delta);
        jugadorB.actualizar(delta);

        Rectangle pisoRect = piso.getRectangulo();
        jugadorA.resolverColision(pisoRect);
        jugadorB.resolverColision(pisoRect);

        jugadorA.tomarOrientacion(jugadorB);
        jugadorB.tomarOrientacion(jugadorA);

        if (jugadorA.isEnMovimiento()) {
            jugadorA.resolverColisionConJugador(jugadorB);
        } else if (jugadorB.isEnMovimiento()) {
            jugadorB.resolverColisionConJugador(jugadorA);
        }


        timeElapsed += delta;
        if (timeElapsed >= attackCooldown) {
            jugadorA.detectarGolpe(jugadorB);
            jugadorB.detectarGolpe(jugadorA);
            timeElapsed = 0f;
        }

        batch.begin();
        jugadorA.renderizar(batch);
        jugadorB.renderizar(batch);
        piso.renderizar(batch);
        batch.end();

        // Renderizar hitboxes en modo debug
        jugadorA.renderizarDebug();
        jugadorB.renderizarDebug();
    }


    @Override
    public void dispose() {
        batch.dispose();
        jugadorA.dispose();
        jugadorB.dispose();
        piso.dispose();
    }
}

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
    private final Sagat sagat1 = new Sagat(150, 150);
    private final Sagat sagat2 = new Sagat(600, 150);
    private float timeElapsed = 0f;
    //150 150
    //450 150
    @Override
    public void show() {
        batch = new SpriteBatch();
        jugadorA = new Jugador(sagat1, Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.P, Input.Keys.O);
        jugadorB = new Jugador(sagat2, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.L, Input.Keys.K);
        piso = new Piso(0, 50, Gdx.graphics.getWidth(), 20, "piso.png");
    }

    @Override
    public void render(float delta) {
        float attackCooldown = 0.5f;
        ScreenUtils.clear(0.42f, 0.63f, 0.86f, 1f);

        jugadorA.actualizar(delta);
        jugadorB.actualizar(delta);

        Rectangle pisoRect = piso.getRectangulo();
        jugadorA.getLuchador().resolverColision(pisoRect);
        jugadorB.getLuchador().resolverColision(pisoRect);

        jugadorA.getLuchador().tomarOrientacion(jugadorB.getLuchador());
        jugadorB.getLuchador().tomarOrientacion(jugadorA.getLuchador());

        if (jugadorA.getLuchador().enMovimiento) {
            jugadorA.getLuchador().resolverColisionConLuchador (jugadorB.getLuchador());
        } else if (jugadorB.getLuchador().enMovimiento) {
            jugadorB.getLuchador().resolverColisionConLuchador(jugadorA.getLuchador());
        }

        timeElapsed += delta;
        if (timeElapsed >= attackCooldown) {
            jugadorA.getLuchador().detectarGolpe(jugadorB.getLuchador());
            jugadorB.getLuchador().detectarGolpe(jugadorA.getLuchador());
            timeElapsed = 0f;
        }

        batch.begin();
        jugadorA.getLuchador().renderizar(batch);
        jugadorB.getLuchador().renderizar(batch);
        piso.renderizar(batch);
        batch.end();

        // Renderizar hitboxes en modo debug
        jugadorA.getLuchador().renderizarDebug();
        jugadorB.getLuchador().renderizarDebug();
    }


    @Override
    public void dispose() {
        batch.dispose();
        jugadorA.getLuchador().dispose();
        jugadorB.getLuchador().dispose();
        piso.dispose();
    }
}

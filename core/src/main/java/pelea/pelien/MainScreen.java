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
    private final Samurai samurai1 = new Samurai(150, 150);
    private final Ninja ninja = new Ninja(600, 150);
    private float timeElapsed = 0f;
    private BarraDeVida barraJugadorA;
    private BarraDeVida barraJugadorB;
    private int espacioEntreBarras = 100;
    private int anchoBarra = (Gdx.graphics.getWidth() - espacioEntreBarras) / 2;
    private ControladorVictoria controladorVictoria = new ControladorVictoria();

    @Override
    public void show() {
        batch = new SpriteBatch();
        jugadorA = new Jugador(samurai1, Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.P, Input.Keys.O);
        jugadorB = new Jugador(ninja, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.L, Input.Keys.K);
        piso = new Piso(0, 50, Gdx.graphics.getWidth(), 20, "piso.png");
        // Barra del Jugador A (esquina superior izquierda)
        barraJugadorA = new BarraDeVida(0, Gdx.graphics.getHeight() - 50, anchoBarra, 20, 500);
    // Barra del Jugador B (esquina superior derecha)
        barraJugadorB = new BarraDeVida(Gdx.graphics.getWidth() - anchoBarra, Gdx.graphics.getHeight() - 50, anchoBarra, 20, 500);
    }

    @Override
    public void render(float delta) {
        float attackCooldown = 0.5f;
        ScreenUtils.clear(0.42f, 0.63f, 0.86f, 1f);

        jugadorA.actualizar(delta);
        jugadorB.actualizar(delta);

        // Actualizar las barras de vida
        barraJugadorA.actualizarVida(jugadorA.getLuchador().getVida());
        barraJugadorB.actualizarVida(jugadorB.getLuchador().getVida());


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
        barraJugadorA.renderizar();
        barraJugadorB.setInvertida(true);
        barraJugadorB.renderizar();
        if (jugadorA.getLuchador().getVida() <= 0){
            controladorVictoria.setMensaje("Jugador B gano");
            controladorVictoria.render(delta);
        }
        if (jugadorB.getLuchador().getVida() <= 0){
            controladorVictoria.setMensaje("Jugador A gano");
            controladorVictoria.render(delta);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        jugadorA.getLuchador().dispose();
        jugadorB.getLuchador().dispose();
        piso.dispose();
        barraJugadorA.dispose();
        barraJugadorB.dispose();
    }

}

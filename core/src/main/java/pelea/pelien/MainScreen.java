package pelea.pelien;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import pelea.pelien.globals.GameData;
import pelea.pelien.globals.GameState;
import pelea.pelien.globals.NetworkData;
import pelea.pelien.network.NetworkActionsListener;
import pelea.pelien.network.ServerThread;

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
    private float timer = 0;
    private BitmapFont timerFont = new BitmapFont();
    private ManagerAudio audio = new ManagerAudio();

    @Override
    public void show() {
        batch = new SpriteBatch();
        jugadorA = new Jugador(samurai1, Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.P, Input.Keys.O);
        NetworkData.serverThread.sendMessageToAll("spawnentity!" + jugadorA.getLuchador().getPosicion().x + "!" + jugadorA.getLuchador().getPosicion().y + "!" + jugadorA.getLuchador().getClass().getSimpleName());
        jugadorB = new Jugador(ninja, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.L, Input.Keys.K);
        NetworkData.serverThread.sendMessageToAll("spawnentity!" + jugadorB.getLuchador().getPosicion().x + "!" + jugadorB.getLuchador().getPosicion().y + "!" + jugadorB.getLuchador().getClass().getSimpleName());
        piso = new Piso(0, 50, Gdx.graphics.getWidth(), 20, "piso.png");
        // Barra del Jugador A (esquina superior izquierda)
        barraJugadorA = new BarraDeVida(0, Gdx.graphics.getHeight() - 50, anchoBarra, 20, 500);
        // Barra del Jugador B (esquina superior derecha)
        barraJugadorB = new BarraDeVida(Gdx.graphics.getWidth() - anchoBarra, Gdx.graphics.getHeight() - 50, anchoBarra, 20, 500);
        audio.reproducirMusica("musica.mp3");
    }

    @Override
    public void render(float delta) {
        float attackCooldown = 0.5f;
        ScreenUtils.clear(0.42f, 0.63f, 0.86f, 1f);
        timer += delta;
        int timerTruncado = (int) Math.floor(timer);
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
        timerFont.draw(batch,String.valueOf(60 - timerTruncado) , (float) Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 25);
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
            audio.disposeMusica();
            controladorVictoria.setMensaje("Jugador B gano");
            controladorVictoria.render(delta);
        }
        if (jugadorB.getLuchador().getVida() <= 0){
            audio.disposeMusica();
            controladorVictoria.setMensaje("Jugador A gano");
            controladorVictoria.render(delta);
        }
        if (timer >= 60){
            audio.disposeMusica();
            controladorVictoria.setMensaje("TIEMPO FUERA");
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

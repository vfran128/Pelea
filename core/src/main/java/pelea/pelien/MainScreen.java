package pelea.pelien;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;  // Importa Texture
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private BarraDeVida barraJugadorA;
    private BarraDeVida barraJugadorB;
    private int espacioEntreBarras = 100;
    private int anchoBarra = (Gdx.graphics.getWidth() - espacioEntreBarras) / 2;
    private float porcentajeVidaA;
    private float porcentajeVidaB;
    private String colorA;
    private String colorB;
    private ControladorVictoria controladorVictoria = new ControladorVictoria();
    private boolean victoriaCheck = false;
    private int timer = 999;
    private BitmapFont timerFont = new BitmapFont();
    private Array<Luchador> luchadores = new Array<>();

    private Texture fondoTextura;

    @Override
    public void show() {
        batch = new SpriteBatch();
        barraJugadorA = new BarraDeVida(0, Gdx.graphics.getHeight() - 50, anchoBarra, 20);
        barraJugadorB = new BarraDeVida(Gdx.graphics.getWidth() - anchoBarra, Gdx.graphics.getHeight() - 50, anchoBarra, 20);
        barraJugadorB.setInvertida(true);
        fondoTextura = new Texture("fondo.png");
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(fondoTextura, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  // Dibuja el fondo a pantalla completa

        for (Luchador luchador : luchadores) {
            if (luchador != null) {
                System.out.println("Renderizando: " + luchador.getClass().getSimpleName());
                luchador.renderizar(batch);
            }
        }
        timerFont.draw(batch,String.valueOf(timer) , (float) Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 25);

        barraJugadorA.renderizar(colorA, porcentajeVidaA);
        barraJugadorB.renderizar(colorB, porcentajeVidaB);

        batch.end();

        if (victoriaCheck) {
            controladorVictoria.render(delta);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        barraJugadorA.dispose();
        barraJugadorB.dispose();
        fondoTextura.dispose();
        for (Luchador luchador : luchadores) {
            if (luchador != null) {
                luchador.dispose();
            }
        }
    }

    public void spawnEntity(float x, float y, String luchador) {
        Luchador luchador1 = null;
        if (luchador.equalsIgnoreCase("Samurai")) {
            System.out.println("samurai Spawn");
            luchador1 = new Samurai(x, y);
        } else if (luchador.equalsIgnoreCase("Ninja")) {
            System.out.println("ninja spawn");
            luchador1 = new Ninja(x, y);
        }

        luchadores.add(luchador1);
    }

    public void moveEntity(float x, float y, String luchador) {
        for (Luchador luchador1 : luchadores) {
            if (luchador1.getClass().getSimpleName().equalsIgnoreCase(luchador)) {
                luchador1.setX(x);
                luchador1.setY(y);
            }
        }
    }

    public void flipEntity(String facingRight, String luchador) {
        for (Luchador luchador1 : luchadores) {
            if (luchador1.getClass().getSimpleName().equalsIgnoreCase(luchador)) {
                luchador1.voltearAnimaciones(facingRight);
            }
        }
    }

    public void barraVida(String nombre, float porcentaje, String color) {
        if (nombre.equals("a")) {
            porcentajeVidaA = porcentaje;
            colorA = color;
        }
        if (nombre.equals("b")) {
            porcentajeVidaB = porcentaje;
            colorB = color;
        }
    }

    public void timerValor(int valor) {
        timer = valor;
        System.out.println("TimerValor =" + valor);
    }

    public void terminarJuego(String mensaje) {
        controladorVictoria.setMensaje(mensaje);
        victoriaCheck = true;
    }
}

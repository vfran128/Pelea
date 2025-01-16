package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Jugador {
    private Animation<TextureRegion> animacion;
    private float tiempoAnimacion;
    private Vector2 posicion;
    private Vector2 velocidad;
    private Vector2 posicionAnterior;
    private float velocidadMovimiento = 200f;
    private float velocidadVertical;
    private static final float GRAVEDAD = -500f;
    private static final float FUERZA_SALTO = 400f;
    private boolean enElPiso;

    private int teclaArriba;
    private int teclaIzquierda;
    private int teclaDerecha;

    private Texture textura;
    private Rectangle rectangulo;

    public Jugador(String texturaPath, float x, float y, int teclaArriba, int teclaIzquierda, int teclaDerecha) {
        textura = new Texture(Gdx.files.internal(texturaPath));
        TextureRegion[][] frames = TextureRegion.split(textura, textura.getWidth() / 4, textura.getHeight());
        animacion = new Animation<>(0.1f, frames[0]);
        animacion.setPlayMode(Animation.PlayMode.LOOP);

        posicion = new Vector2(x, y);
        posicionAnterior = new Vector2(x, y);
        velocidad = new Vector2(0, 0);

        this.teclaArriba = teclaArriba;
        this.teclaIzquierda = teclaIzquierda;
        this.teclaDerecha = teclaDerecha;

        rectangulo = new Rectangle(posicion.x, posicion.y, textura.getWidth() / 4f, textura.getHeight());
    }

    public void actualizar(float delta) {
        tiempoAnimacion += delta;
        posicionAnterior.set(posicion);

        // Movimiento horizontal
        if (Gdx.input.isKeyPressed(teclaIzquierda)) {
            velocidad.x = -velocidadMovimiento;
        } else if (Gdx.input.isKeyPressed(teclaDerecha)) {
            velocidad.x = velocidadMovimiento;
        } else {
            velocidad.x = 0;
        }

        // Salto
        if (Gdx.input.isKeyPressed(teclaArriba) && enElPiso) {
            velocidadVertical = FUERZA_SALTO;
            enElPiso = false;
        }

        // Gravedad
        if (!enElPiso) {
            velocidadVertical += GRAVEDAD * delta;
        } else {
            velocidadVertical = 0;
        }

        // Actualizar posición
        posicion.x += velocidad.x * delta;
        posicion.y += velocidadVertical * delta;

        // Actualizar rectángulo
        rectangulo.setPosition(posicion.x, posicion.y);
    }

    public void resolverColision(Rectangle obstaculo) {
        if (rectangulo.overlaps(obstaculo)) {
            if (posicionAnterior.y >= obstaculo.y + obstaculo.height) {
                // Cayendo sobre el obstáculo
                posicion.y = obstaculo.y + obstaculo.height;
                enElPiso = true;
            } else if (posicionAnterior.y + rectangulo.height <= obstaculo.y) {
                // Chocando desde abajo
                posicion.y = obstaculo.y - rectangulo.height;
                velocidadVertical = 0;
            } else if (posicionAnterior.x + rectangulo.width <= obstaculo.x) {
                // Chocando desde la izquierda
                posicion.x = obstaculo.x - rectangulo.width;
            } else if (posicionAnterior.x >= obstaculo.x + obstaculo.width) {
                // Chocando desde la derecha
                posicion.x = obstaculo.x + obstaculo.width;
            }
            rectangulo.setPosition(posicion.x, posicion.y);
        }
    }

    public void renderizar(SpriteBatch batch) {
        TextureRegion frameActual = animacion.getKeyFrame(tiempoAnimacion);
        batch.draw(frameActual, posicion.x, posicion.y);
    }

    public Rectangle getRectangulo() {
        return rectangulo;
    }

    public void dispose() {
        textura.dispose();
    }
}

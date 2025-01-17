package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Jugador {
    private Animation<TextureRegion> animacionBase;
    private Animation<TextureRegion> animacionGolpe;
    private float tiempoAnimacion;
    private boolean golpeando;

    private Vector2 posicion;
    private Vector2 velocidad;
    private boolean enElPiso;
    private static final float GRAVEDAD = -500f;
    private static final float FUERZA_SALTO = 400f;

    private int teclaArriba, teclaIzquierda, teclaDerecha, teclaGolpe;

    private ShapeRenderer shapeRenderer;

    private Rectangle hitboxPrincipal; // Hitbox principal dinámica
    private Rectangle hitboxPatada;   // Hitbox específica para la pierna

    public Jugador(String texturaBasePath, String texturaGolpePath, float x, float y,
                   int teclaArriba, int teclaIzquierda, int teclaDerecha, int teclaGolpe) {
        Texture texturaBase = new Texture(texturaBasePath);
        TextureRegion[][] framesBase = TextureRegion.split(texturaBase, texturaBase.getWidth() / 4, texturaBase.getHeight());
        animacionBase = new Animation<>(0.1f, framesBase[0]);
        animacionBase.setPlayMode(Animation.PlayMode.LOOP);

        Texture texturaGolpe = new Texture(texturaGolpePath);
        TextureRegion[][] framesGolpe = TextureRegion.split(texturaGolpe, texturaGolpe.getWidth() / 5, texturaGolpe.getHeight());
        animacionGolpe = new Animation<>(0.1f, framesGolpe[0]);
        animacionGolpe.setPlayMode(Animation.PlayMode.NORMAL);

        tiempoAnimacion = 0;
        golpeando = false;

        posicion = new Vector2(x, y);
        velocidad = new Vector2(0, 0);
        enElPiso = false;

        this.teclaArriba = teclaArriba;
        this.teclaIzquierda = teclaIzquierda;
        this.teclaDerecha = teclaDerecha;
        this.teclaGolpe = teclaGolpe;

        TextureRegion frameInicial = framesBase[0][0];
        hitboxPrincipal = new Rectangle(posicion.x, posicion.y, frameInicial.getRegionWidth(), frameInicial.getRegionHeight());
        hitboxPatada = new Rectangle(); // Inicialmente vacía

        shapeRenderer = new ShapeRenderer();
    }

    public void actualizar(float delta) {
        if (golpeando) {
            tiempoAnimacion += delta;
            if (animacionGolpe.isAnimationFinished(tiempoAnimacion)) {
                golpeando = false;
                tiempoAnimacion = 0;
            }
            actualizarHitboxGolpe();
            return; // No actualizar movimiento mientras golpea
        }

        if (Gdx.input.isKeyPressed(teclaIzquierda)) {
            posicion.x -= 200 * delta;
        }
        if (Gdx.input.isKeyPressed(teclaDerecha)) {
            posicion.x += 200 * delta;
        }

        if (Gdx.input.isKeyPressed(teclaArriba) && enElPiso) {
            velocidad.y = FUERZA_SALTO;
            enElPiso = false;
        }

        if (!enElPiso) {
            velocidad.y += GRAVEDAD * delta;
            posicion.y += velocidad.y * delta;
        }

        if (Gdx.input.isKeyPressed(teclaGolpe) && !golpeando) {
            golpeando = true;
            tiempoAnimacion = 0;
        }

        limitarMovimientoEnPantalla();
        actualizarHitbox();
    }

    public void renderizar(SpriteBatch batch) {
        TextureRegion frameActual;

        if (golpeando) {
            frameActual = animacionGolpe.getKeyFrame(tiempoAnimacion);
        } else {
            tiempoAnimacion += Gdx.graphics.getDeltaTime();
            frameActual = animacionBase.getKeyFrame(tiempoAnimacion);
        }

        batch.draw(frameActual, posicion.x, posicion.y);
    }

    public void resolverColision(Rectangle obstaculo) {
        if (hitboxPrincipal.overlaps(obstaculo)) {
            if (velocidad.y < 0) {
                posicion.y = obstaculo.y + obstaculo.height;
                enElPiso = true;
                velocidad.y = 0;
            }
        }
        actualizarHitbox();
    }

    public void detectarGolpe(Jugador otroJugador) {
        if (golpeando && hitboxPatada.overlaps(otroJugador.hitboxPrincipal)) {
            System.out.println("Jugador A pateó a Jugador B");
        }
    }

    private void actualizarHitbox() {
        TextureRegion frameActual = golpeando ? animacionGolpe.getKeyFrame(tiempoAnimacion) : animacionBase.getKeyFrame(tiempoAnimacion);
        hitboxPrincipal.set(posicion.x, posicion.y, frameActual.getRegionWidth(), frameActual.getRegionHeight());
    }

    private void actualizarHitboxGolpe() {
        if (golpeando) {
            // Ajustar la hitboxPatada en base al frame actual
            TextureRegion frameActual = animacionGolpe.getKeyFrame(tiempoAnimacion);

            // Determinar la posición relativa de la pierna en este frame
            float anchoPierna = frameActual.getRegionWidth() * 0.3f; // Proporción de la pierna
            float altoPierna = frameActual.getRegionHeight() * 0.5f; // Altura de la pierna

            hitboxPatada.set(posicion.x + frameActual.getRegionWidth(), // Posición al lado de la hitbox principal
                posicion.y + frameActual.getRegionHeight() * 0.25f, // Ajuste vertical
                anchoPierna, altoPierna);
        } else {
            hitboxPatada.set(0, 0, 0, 0); // Hitbox desactivada
        }
    }

    private void limitarMovimientoEnPantalla() {
        if (posicion.x < 0) {
            posicion.x = 0;
        }
        if (posicion.x + hitboxPrincipal.width > Gdx.graphics.getWidth()) {
            posicion.x = Gdx.graphics.getWidth() - hitboxPrincipal.width;
        }
    }

    public void renderizarDebug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Hitbox principal
        shapeRenderer.setColor(1, 0, 0, 1); // Rojo
        shapeRenderer.rect(hitboxPrincipal.x, hitboxPrincipal.y, hitboxPrincipal.width, hitboxPrincipal.height);

        // Hitbox de la patada
        shapeRenderer.setColor(0, 1, 0, 1); // Verde
        shapeRenderer.rect(hitboxPatada.x, hitboxPatada.y, hitboxPatada.width, hitboxPatada.height);

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

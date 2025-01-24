package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Luchador {
    protected int VELOCIDAD_MOVIMIENTO = 20;
    protected Animation<TextureRegion> animacionBase;
    protected Animation<TextureRegion> animacionGolpe1;
    protected Animation<TextureRegion> animacionGolpe2;
    protected Animation<TextureRegion> animacionActual;
    protected boolean enMovimiento = false;
    protected int vida;
    protected boolean facingRight = false;

    protected float tiempoAnimacion;
    protected boolean golpeando;

    protected Vector2 posicion;
    protected Vector2 velocidad;
    protected boolean enElPiso;
    protected static final float GRAVEDAD = -500f;
    protected static final float FUERZA_SALTO = 400f;

    protected ShapeRenderer shapeRenderer;

    protected Rectangle hitboxPrincipal;
    protected Golpe golpeActual;

    public Luchador(float x, float y, int vidaInicial) {
        this.vida = vidaInicial;
        this.tiempoAnimacion = 0;
        this.golpeando = false;

        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0, 0);
        this.enElPiso = false;

        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void cargarAnimaciones();

    public void actualizar(float delta) {
        if (golpeando) {
            tiempoAnimacion += delta;

            if (golpeActual != null) {
                golpeActual.actualizar(delta);
                if (!golpeActual.estaActivo()) {
                    golpeActual = null;
                    golpeando = false;
                    animacionActual = animacionBase;
                    tiempoAnimacion = 0;
                }
            }
            return;
        }

        if (!enElPiso) {
            velocidad.y += GRAVEDAD * delta;
            posicion.y += velocidad.y * delta;
        }

        limitarMovimientoEnPantalla();
        actualizarHitbox();
    }

    public void renderizar(SpriteBatch batch) {
        TextureRegion frameActual = animacionActual.getKeyFrame(tiempoAnimacion);
        batch.draw(frameActual, posicion.x, posicion.y);
    }

    public void renderizarDebug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Hitbox principal
        shapeRenderer.setColor(1, 0, 0, 1); // Rojo
        shapeRenderer.rect(hitboxPrincipal.x, hitboxPrincipal.y, hitboxPrincipal.width, hitboxPrincipal.height);

        // Hitbox de golpe (si existe)
        if (golpeActual != null) {
            golpeActual.renderizarDebug();
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    protected void actualizarHitbox() {
        TextureRegion frameActual = animacionActual.getKeyFrame(tiempoAnimacion);
        hitboxPrincipal.set(posicion.x, posicion.y, frameActual.getRegionWidth(), frameActual.getRegionHeight());
    }

    protected void limitarMovimientoEnPantalla() {
        if (posicion.x < 0) {
            posicion.x = 0;
        }
        if (posicion.x + hitboxPrincipal.width > Gdx.graphics.getWidth()) {
            posicion.x = Gdx.graphics.getWidth() - hitboxPrincipal.width;
        }
    }

    public void modificarVida(int daño) {
        this.vida -= daño;
    }

    public int getVida() {
        return vida;
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void tomarOrientacion(Luchador objetivo) {
        if (this.posicion.x > objetivo.getPosicion().x) {
            facingRight = false;
        } else {
            facingRight = true;
        }
        cambiarOrientacion();
    }

    private void cambiarOrientacion() {
        if (!facingRight) {
            voltearAnimaciones(true);
        } else {
            voltearAnimaciones(false);
        }
    }

    private void voltearAnimaciones(boolean flip) {
        for (TextureRegion frame : animacionBase.getKeyFrames()) {
            if (frame.isFlipX() != flip) {
                frame.flip(true, false);
            }
        }
        for (TextureRegion frame : animacionGolpe1.getKeyFrames()) {
            if (frame.isFlipX() != flip) {
                frame.flip(true, false);
            }
        }
        for (TextureRegion frame : animacionGolpe2.getKeyFrames()) {
            if (frame.isFlipX() != flip) {
                frame.flip(true, false);
            }
        }
    }

    // Métodos para movimiento
    public void moverIzquierda(float delta) {
        facingRight = false;
        velocidad.x = -VELOCIDAD_MOVIMIENTO;
        posicion.x += velocidad.x * delta;
        animacionActual = animacionBase;
        tiempoAnimacion += delta;
    }

    public void moverDerecha(float delta) {
        facingRight = true;
        velocidad.x = VELOCIDAD_MOVIMIENTO;
        posicion.x += velocidad.x * delta;
        animacionActual = animacionBase;
        tiempoAnimacion += delta;
    }

    public void saltar() {
        if (enElPiso) {
            velocidad.y = FUERZA_SALTO;
            enElPiso = false;
        }
    }

    public void detenerMovimiento() {
        velocidad.x = 0;
        enMovimiento = false;
    }

    // Métodos para golpes
    public void ejecutarGolpe1() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe1;
            tiempoAnimacion = 0;
        }
    }

    public void ejecutarGolpe2() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe2;
            tiempoAnimacion = 0;
        }
    }
}

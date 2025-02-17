package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Luchador {
    protected Animation<TextureRegion> animacionBase;
    protected Animation<TextureRegion> animacionGolpe1;
    protected Animation<TextureRegion> animacionGolpe2;
    protected Animation<TextureRegion> animacionActual;

    protected float tiempoAnimacion;

    protected Vector2 posicion;

    protected ShapeRenderer shapeRenderer;



    public Luchador(float x, float y) {
        this.tiempoAnimacion = 0;
        this.posicion = new Vector2(x, y);
        this.shapeRenderer = new ShapeRenderer();
    }

    public abstract void cargarAnimaciones();

    public void setX(float x) {
        this.posicion.x = x;
    }

    public void setY(float y) {
        this.posicion.y = y;
    }

    public void renderizar(SpriteBatch batch) {
        // Solo se renderiza la animación del luchador (sin la hitbox del golpe)
        TextureRegion frameActual = animacionActual.getKeyFrame(tiempoAnimacion);
        batch.draw(frameActual, posicion.x, posicion.y);
    }

    /*public void renderizarDebug() {
        // Renderizamos las hitboxes solo en modo debug
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Hitbox principal del luchador
        shapeRenderer.setColor(1, 0, 0, 1); // Rojo
        shapeRenderer.rect(hitboxPrincipal.x, hitboxPrincipal.y, hitboxPrincipal.width, hitboxPrincipal.height);

        // Renderizar hitbox de golpe si está activa
        if (golpeActual != null && golpeActual.estaActivo()) {
            golpeActual.renderizarDebug();  // Llamamos a renderizarDebug de Golpe
        }

        shapeRenderer.end();
    }*/


   /* public void detectarGolpe(Luchador otroLuchador) {
        if (golpeActual != null && golpeActual.hitbox.overlaps(otroLuchador.hitboxPrincipal)) {
            golpeActual.aplicarEfecto(otroLuchador);
        }
    }*/

    public void dispose() {
        shapeRenderer.dispose();
    }

   /* protected void actualizarHitbox() {
        if (animacionActual != null) {
            TextureRegion frameActual = animacionActual.getKeyFrame(tiempoAnimacion);
            hitboxPrincipal.set(posicion.x, posicion.y, frameActual.getRegionWidth(), frameActual.getRegionHeight());
        }
    }*/


    /*protected void limitarMovimientoEnPantalla() {
        if (posicion.x < 0) {
            posicion.x = 0;
        }
        if (posicion.x + hitboxPrincipal.width > Gdx.graphics.getWidth()) {
            posicion.x = Gdx.graphics.getWidth() - hitboxPrincipal.width;
        }
    }*/

    /*public void resolverColisionConLuchador(Luchador otroLuchador) {
        if (this.hitboxPrincipal.overlaps(otroLuchador.hitboxPrincipal)) {
            if (this.posicion.x < otroLuchador.posicion.x) {
                this.posicion.x = otroLuchador.posicion.x - this.hitboxPrincipal.width;
            } else {
                this.posicion.x = otroLuchador.posicion.x + otroLuchador.hitboxPrincipal.width;
            }

            this.actualizarHitbox();
            otroLuchador.actualizarHitbox();
        }
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

    public void modificarVida(int dano) {
        this.vida -= dano;
    }

    public int getVida() {
        return vida;
    }*/

    public Vector2 getPosicion() {
        return posicion;
    }

   /* public void tomarOrientacion(Luchador objetivo) {
        facingRight = this.posicion.x <= objetivo.getPosicion().x;
        cambiarOrientacion();
    }*/

    /*private void cambiarOrientacion() {
        voltearAnimaciones(!facingRight);
    }*/

    public void voltearAnimaciones(String facingRight) {
        boolean flip = Boolean.parseBoolean(facingRight);
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

    /*public void moverIzquierda(float delta) {
        enMovimiento = true;
        facingRight = false;
        velocidad.x = -VELOCIDAD_MOVIMIENTO;
        posicion.x += velocidad.x * delta;
        animacionActual = animacionBase;
        tiempoAnimacion += delta;
    }

    public void moverDerecha(float delta) {
        enMovimiento = true;
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

    public void ejecutarGolpe1() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe1;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe1(); // Crear golpe dinámicamente
        }
    }

    public void ejecutarGolpe2() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe2;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe2(); // Crear golpe dinámicamente
        }
    }*/

}

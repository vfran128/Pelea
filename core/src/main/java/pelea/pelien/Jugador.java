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
    private Animation<TextureRegion> animacionPatada;
    private Animation<TextureRegion> animacionPuño;
    private Animation<TextureRegion> animacionActual;
    private boolean enMovimiento = false;
    private int vida = 500;
    private boolean facingRight = false;


    private float tiempoAnimacion;
    private boolean golpeando;

    private Vector2 posicion;
    private Vector2 velocidad;
    private boolean enElPiso;
    private static final float GRAVEDAD = -500f;
    private static final float FUERZA_SALTO = 400f;

    private int teclaArriba, teclaIzquierda, teclaDerecha, teclaPatada, teclaPuño;

    private ShapeRenderer shapeRenderer;

    private Rectangle hitboxPrincipal;
    private Golpe golpeActual;

    public Jugador(String texturaBasePath, String texturaPatadaPath, String texturaPuñoPath, float x, float y,
                   int teclaArriba, int teclaIzquierda, int teclaDerecha, int teclaPatada, int teclaPuño) {
        // Cargar textura base y animación
        Texture texturaBase = new Texture(texturaBasePath);
        TextureRegion[][] framesBase = TextureRegion.split(texturaBase, texturaBase.getWidth() / 4, texturaBase.getHeight());
        animacionBase = new Animation<>(0.1f, framesBase[0]);
        animacionBase.setPlayMode(Animation.PlayMode.LOOP);

        // Cargar textura y animación para la patada
        Texture texturaPatada = new Texture(texturaPatadaPath);
        TextureRegion[][] framesPatada = TextureRegion.split(texturaPatada, texturaPatada.getWidth() / 5, texturaPatada.getHeight());
        animacionPatada = new Animation<>(0.1f, framesPatada[0]);
        animacionPatada.setPlayMode(Animation.PlayMode.NORMAL);

        // Cargar textura y animación para el puño
        Texture texturaPuño = new Texture(texturaPuñoPath);
        TextureRegion[][] framesPuño = TextureRegion.split(texturaPuño, texturaPuño.getWidth() / 3, texturaPuño.getHeight());
        animacionPuño = new Animation<>(0.1f, framesPuño[0]);
        animacionPuño.setPlayMode(Animation.PlayMode.NORMAL);
    //fran pelotudo
        // Inicialización general
        tiempoAnimacion = 0;
        golpeando = false;

        posicion = new Vector2(x, y);
        velocidad = new Vector2(0, 0);
        enElPiso = false;

        this.teclaArriba = teclaArriba;
        this.teclaIzquierda = teclaIzquierda;
        this.teclaDerecha = teclaDerecha;
        this.teclaPatada = teclaPatada;
        this.teclaPuño = teclaPuño;

        TextureRegion frameInicial = framesBase[0][0];
        this.hitboxPrincipal = new Rectangle(posicion.x, posicion.y, frameInicial.getRegionWidth(), frameInicial.getRegionHeight());
        this.golpeActual = null;
        this.animacionActual = animacionBase;
        this.shapeRenderer = new ShapeRenderer();
    }

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

        if (Gdx.input.isKeyPressed(teclaIzquierda)) {
            posicion.x -= 200 * delta;
            enMovimiento = true;
        }
        if (Gdx.input.isKeyPressed(teclaDerecha)) {
            posicion.x += 200 * delta;
            enMovimiento = true;
        }

        if (Gdx.input.isKeyPressed(teclaArriba) && enElPiso) {
            velocidad.y = FUERZA_SALTO;
            enElPiso = false;
        }

        if (!enElPiso) {
            velocidad.y += GRAVEDAD * delta;
            posicion.y += velocidad.y * delta;
        }

        if (Gdx.input.isKeyPressed(teclaPatada) && !golpeando) {
            iniciarPatada();
        } else if (Gdx.input.isKeyPressed(teclaPuño) && !golpeando) {
            iniciarPuño();
        }

        if (!golpeando && enMovimiento) {
            animacionActual = animacionBase;
            tiempoAnimacion += delta;
        }

        if (!Gdx.input.isKeyPressed(teclaIzquierda) && !Gdx.input.isKeyPressed(teclaDerecha)) {
            enMovimiento = false;
        }

        limitarMovimientoEnPantalla();
        actualizarHitbox();
    }

    private void iniciarPatada() {
        golpeando = true;
        tiempoAnimacion = 0;
        animacionActual = animacionPatada;

        float anchoPierna = hitboxPrincipal.width * 0.5f;
        float altoPierna = hitboxPrincipal.height * 0.5f;
        float xPierna;
        float yPierna = posicion.y + hitboxPrincipal.height * 0.25f;;
        if (facingRight) {
            xPierna = posicion.x + hitboxPrincipal.width;
        } else {
            xPierna = posicion.x - hitboxPrincipal.width+20;
        }



        golpeActual = new Patada(xPierna, yPierna, anchoPierna, altoPierna, 0.5f,20);
    }

    private void iniciarPuño() {
        golpeando = true;
        tiempoAnimacion = 0;
        animacionActual = animacionPuño;

        float anchoPuño = hitboxPrincipal.width * 0.3f;
        float altoPuño = hitboxPrincipal.height * 0.3f;
        float xPuño;
        float yPuño = posicion.y + hitboxPrincipal.height * 0.5f;
        if (facingRight) {
            xPuño = posicion.x + hitboxPrincipal.width;
        } else {
            xPuño = posicion.x - hitboxPrincipal.width+20;
        }


        golpeActual = new Puño(xPuño, yPuño, anchoPuño, altoPuño, 0.3f,10);
    }

    public void resolverColisionConJugador(Jugador otroJugador) {
        if (this.hitboxPrincipal.overlaps(otroJugador.hitboxPrincipal)) {
            // Resolver colisión horizontal
            if (this.posicion.x < otroJugador.posicion.x) {
                this.posicion.x = otroJugador.posicion.x - this.hitboxPrincipal.width;
            } else {
                this.posicion.x = otroJugador.posicion.x + otroJugador.hitboxPrincipal.width;
            }

            // Actualizar hitboxes
            this.actualizarHitbox();
            otroJugador.actualizarHitbox();
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

    public void detectarGolpe(Jugador otroJugador) {
        if (golpeActual != null && golpeActual.hitbox.overlaps(otroJugador.hitboxPrincipal)) {
            golpeActual.aplicarEfecto(otroJugador);
        }
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

    private void actualizarHitbox() {
        TextureRegion frameActual = animacionActual.getKeyFrame(tiempoAnimacion);
        hitboxPrincipal.set(posicion.x, posicion.y, frameActual.getRegionWidth(), frameActual.getRegionHeight());
    }

    private void limitarMovimientoEnPantalla() {
        if (posicion.x < 0) {
            posicion.x = 0;
        }
        if (posicion.x + hitboxPrincipal.width > Gdx.graphics.getWidth()) {
            posicion.x = Gdx.graphics.getWidth() - hitboxPrincipal.width;
        }
    }
    public void modificarVida(int daño){
        this.vida -= daño;

    }



    //funcion para que siempre se esten mirando los jugadores
    public void tomarOrientacion(Jugador objetivo){
        if (this.posicion.x > objetivo.getPosicion().x) {
            facingRight = false;
        }else {
            facingRight = true;
        }
        cambiarOrientacion();
    }

    private void cambiarOrientacion() {
        if (!facingRight) {
            for (TextureRegion frame : animacionBase.getKeyFrames()) {
                if (!frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
            for (TextureRegion frame : animacionPatada.getKeyFrames()) {
                if (!frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
            for (TextureRegion frame : animacionPuño.getKeyFrames()) {
                if (!frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
        } else {
            // Si está mirando a la derecha, aseguramos que no esté reflejada
            for (TextureRegion frame : animacionBase.getKeyFrames()) {
                if (frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
            for (TextureRegion frame : animacionPatada.getKeyFrames()) {
                if (frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
            for (TextureRegion frame : animacionPuño.getKeyFrames()) {
                if (frame.isFlipX()) {
                    frame.flip(true, false); // Revertimos el flip horizontal
                }
            }
        }
    }

    public boolean isEnMovimiento() {
        return enMovimiento;
    }

    public int getVida() {
        return vida;
    }

    public Vector2 getPosicion() {
        return posicion;
    }
}

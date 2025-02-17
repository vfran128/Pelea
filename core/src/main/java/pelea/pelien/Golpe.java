package pelea.pelien;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Golpe {
    protected Rectangle hitbox; // Hitbox del golpe
    protected float duracion;  // Duración del golpe en segundos
    protected float tiempoActivo; // Tiempo transcurrido desde el inicio del golpe
    protected ShapeRenderer shapeRenderer;
    protected int daño;
    protected String sonidoNombre;

    // Nuevo: Animación o textura del golpe
    protected Texture texturaGolpe;
    protected TextureRegion frameGolpe;
    protected ManagerAudio audio;

    public Golpe(float x, float y, float ancho, float alto, float duracion, String texturaPath,int daño, String sonidoNombre) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.duracion = duracion;
        this.tiempoActivo = 0;
        this.shapeRenderer = new ShapeRenderer();
        this.texturaGolpe = new Texture(texturaPath);
        this.frameGolpe = new TextureRegion(texturaGolpe);
        this.daño = daño;
        this.sonidoNombre = sonidoNombre;
        this.audio = new ManagerAudio();
    }

    // Método para actualizar el estado del golpe
    public void actualizar(float delta) {
        tiempoActivo += delta;
    }

    // Método para verificar si el golpe sigue activo
    public boolean estaActivo() {
        return tiempoActivo < duracion;
    }

    // Método para aplicar efectos del golpe
    public void aplicarEfecto(Luchador objetivo){
        System.out.println("¡ataque impactó al jugador!");
        //objetivo.modificarVida(daño);
        //System.out.println(objetivo.getVida());
    }

    public void sonidoGolpe (){
        audio.reproducirSonido(sonidoNombre);
    }

    // Renderizar el golpe
    public void renderizar(SpriteBatch batch) {
        // Solo se renderizan las texturas del golpe dentro del SpriteBatch
        if (estaActivo()) {
            audio.reproducirSonido(sonidoNombre);
            batch.draw(frameGolpe, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }

    public void renderizarDebug() {
        // Solo se dibujan las hitboxes con el ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Rojo para la hitbox del golpe
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();
    }


    // Liberar recursos
    public void dispose() {
        texturaGolpe.dispose();
        shapeRenderer.dispose();
    }
}

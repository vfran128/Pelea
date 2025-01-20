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

    // Nuevo: Animación o textura del golpe
    protected Texture texturaGolpe;
    protected TextureRegion frameGolpe;

    public Golpe(float x, float y, float ancho, float alto, float duracion, String texturaPath) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.duracion = duracion;
        this.tiempoActivo = 0;
        this.shapeRenderer = new ShapeRenderer();
        this.texturaGolpe = new Texture(texturaPath);
        this.frameGolpe = new TextureRegion(texturaGolpe);
    }

    // Método para actualizar el estado del golpe
    public void actualizar(float delta) {
        tiempoActivo += delta;
    }

    // Método para verificar si el golpe sigue activo
    public boolean estaActivo() {
        return tiempoActivo < duracion;
    }

    // Método abstracto para aplicar efectos del golpe
    public abstract void aplicarEfecto(Jugador objetivo);

    // Renderizar el golpe
    public void renderizar(SpriteBatch batch) {
        batch.draw(frameGolpe, hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    // Renderizado en modo debug
    public void renderizarDebug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1); // Verde para la hitbox del golpe
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();
    }

    // Liberar recursos
    public void dispose() {
        texturaGolpe.dispose();
        shapeRenderer.dispose();
    }
}

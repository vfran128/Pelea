package pelea.pelien;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Golpe {
    protected Rectangle hitbox; // Hitbox del golpe
    protected float duracion;  // Duración del golpe en segundos
    protected float tiempoActivo; // Tiempo transcurrido desde el inicio del golpe
    protected ShapeRenderer shapeRenderer;

    public Golpe(float x, float y, float ancho, float alto, float duracion) {
        this.hitbox = new Rectangle(x, y, ancho, alto);
        this.duracion = duracion;
        this.tiempoActivo = 0;
        this.shapeRenderer = new ShapeRenderer();
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

    // Renderizado en modo debug
    public void renderizarDebug() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1); // Verde para la hitbox del golpe
        shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shapeRenderer.end();
    }
}

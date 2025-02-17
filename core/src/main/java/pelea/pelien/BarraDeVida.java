package pelea.pelien;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BarraDeVida {
    private float x, y, ancho, alto;
    private int vidaMaxima;
    private int vidaActual;
    private ShapeRenderer shapeRenderer;
    private boolean invertida;

    public BarraDeVida(float x, float y, float ancho, float alto, int vidaMaxima) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.invertida = false;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setInvertida(boolean invertida) {
        this.invertida = invertida;
    }

    public void actualizarVida(int nuevaVida) {
        this.vidaActual = Math.max(0, nuevaVida); // Asegurarse de que no sea negativa
    }

    public void renderizar() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fondo de la barra (gris oscuro)
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, ancho, alto);

        // Barra de vida (cambia de color según el porcentaje)
        float porcentajeVida = (float) vidaActual / vidaMaxima;
        if (porcentajeVida > 0.5f) {
            shapeRenderer.setColor(Color.GREEN); // Verde si tiene más del 50% de vida
        } else if (porcentajeVida > 0.2f) {
            shapeRenderer.setColor(Color.YELLOW); // Amarillo si tiene entre 20% y 50%
        } else {
            shapeRenderer.setColor(Color.RED); // Rojo si tiene menos del 20% de vida
        }

        // Dibujar la barra de vida con orientación según el estado de "invertida"
        if (invertida) {
            shapeRenderer.rect(x + ancho * (1 - porcentajeVida), y, ancho * porcentajeVida, alto);
        } else {
            shapeRenderer.rect(x, y, ancho * porcentajeVida, alto);
        }

        shapeRenderer.end();
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

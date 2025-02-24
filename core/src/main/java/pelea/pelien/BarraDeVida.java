package pelea.pelien;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BarraDeVida {
    private float x, y, ancho, alto;
    private ShapeRenderer shapeRenderer;
    private boolean invertida;

    public BarraDeVida(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.invertida = false;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void setInvertida(boolean invertida) {
        this.invertida = invertida;
    }

    public void renderizar(String color, float porcentajeVida) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, ancho, alto);

        if ("verde".equals(color) || color == null) {
            shapeRenderer.setColor(Color.GREEN);
        } else if ("amarillo".equals(color)) {
            shapeRenderer.setColor(Color.YELLOW);
        } else if ("rojo".equals(color)) {
            shapeRenderer.setColor(Color.RED);
        }


        if (invertida) {
            shapeRenderer.rect(x + ancho * (1 - porcentajeVida), y, ancho * porcentajeVida, alto);
        } else {
            shapeRenderer.rect(x, y, ancho * porcentajeVida, alto);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

}

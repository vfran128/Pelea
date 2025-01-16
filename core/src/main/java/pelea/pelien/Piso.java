package pelea.pelien;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Piso {
    private Rectangle rectangulo;
    private Texture textura;

    public Piso(float x, float y, float ancho, float alto, String texturaPath) {
        rectangulo = new Rectangle(x, y, ancho, alto);
        textura = new Texture(texturaPath);
    }

    public void renderizar(SpriteBatch batch) {
        batch.draw(textura, rectangulo.x, rectangulo.y, rectangulo.width, rectangulo.height);
    }

    public Rectangle getRectangulo() {
        return rectangulo;
    }

    public void dispose() {
        textura.dispose();
    }
}



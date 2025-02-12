package pelea.pelien;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;

public class Samurai extends Luchador {

    public Samurai(float x, float y) {
        super(x, y, 500);
        cargarAnimaciones();
        TextureRegion frameInicial = animacionBase.getKeyFrame(0);
        this.hitboxPrincipal = new Rectangle(posicion.x, posicion.y, frameInicial.getRegionWidth(), frameInicial.getRegionHeight());
    }

    @Override
    public void cargarAnimaciones() {
        // Cargar y recortar textura base
        Texture texturaBase = new Texture("P1Idle.png");
        TextureRegion[][] framesBase = splitAndTrim(texturaBase, 8, 1);
        animacionBase = new Animation<>(0.1f, framesBase[0]);
        animacionBase.setPlayMode(Animation.PlayMode.LOOP);

        // Cargar y recortar textura golpe1 (patada)
        Texture texturaGolpe1 = new Texture("P1Attack1.png");
        TextureRegion[][] framesGolpe1 = splitAndTrim(texturaGolpe1, 6, 1);
        animacionGolpe1 = new Animation<>(0.1f, framesGolpe1[0]);
        animacionGolpe1.setPlayMode(Animation.PlayMode.NORMAL);

        // Cargar y recortar textura golpe2 (puño)
        Texture texturaGolpe2 = new Texture("P1Attack2.png");
        TextureRegion[][] framesGolpe2 = splitAndTrim(texturaGolpe2, 6, 1);
        animacionGolpe2 = new Animation<>(0.1f, framesGolpe2[0]);
        animacionGolpe2.setPlayMode(Animation.PlayMode.NORMAL);

        animacionActual = animacionBase;
    }

    private TextureRegion[][] splitAndTrim(Texture textura, int cols, int rows) {
        TextureRegion[][] regions = TextureRegion.split(textura, textura.getWidth() / cols, textura.getHeight() / rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                regions[i][j] = trimTextureRegion(regions[i][j]);
            }
        }
        return regions;
    }

    private TextureRegion trimTextureRegion(TextureRegion region) {
        Texture texture = region.getTexture();
        TextureData textureData = texture.getTextureData();

        // Asegurar que la textura está lista para ser leída
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        Pixmap pixmap = textureData.consumePixmap(); // Obtener Pixmap de la textura
        int width = region.getRegionWidth();
        int height = region.getRegionHeight();

        Pixmap regionPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        regionPixmap.drawPixmap(pixmap, 0, 0, region.getRegionX(), region.getRegionY(), width, height);

        int minX = width, minY = height, maxX = 0, maxY = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((regionPixmap.getPixel(x, y) & 0x000000FF) != 0) { // Si el píxel no es transparente
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        TextureRegion trimmedRegion = new TextureRegion(texture, region.getRegionX() + minX, region.getRegionY() + minY, maxX - minX + 1, maxY - minY + 1);

        // Limpiar los pixmaps para evitar fugas de memoria
        regionPixmap.dispose();
        pixmap.dispose();

        return trimmedRegion;
    }

    @Override
    public Golpe crearGolpe1() {
        // Crear una patada en la posición de Sagat
        float ancho = 50;  // Tamaño del golpe
        float alto = 20;   // Altura del golpe
        float x = facingRight ? posicion.x + hitboxPrincipal.width : posicion.x - ancho;
        float y = posicion.y + hitboxPrincipal.height / 2;

        return new Golpe1P1(x, y, ancho, alto, 0.5f, 20);
    }

    @Override
    public Golpe crearGolpe2() {
        // Crear un puño en la posición de Sagat
        float ancho = 40;  // Tamaño del golpe
        float alto = 20;   // Altura del golpe
        float x = facingRight ? posicion.x + hitboxPrincipal.width : posicion.x - ancho;
        float y = posicion.y + hitboxPrincipal.height / 1.5f;

        return new Golpe2P1(x, y, ancho, alto, 0.5f, 15);
    }

    @Override
    public void ejecutarGolpe1() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe1;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe1(); // Asignar golpe
            golpeActual.sonidoGolpe();
        }
    }

    @Override
    public void ejecutarGolpe2() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe2;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe2(); // Asignar golpe
            golpeActual.sonidoGolpe();
        }
    }
}

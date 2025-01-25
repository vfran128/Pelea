package pelea.pelien;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;

public class Sagat extends Luchador {

    public Sagat(float x, float y) {
        super(x, y, 500);
        cargarAnimaciones();
        TextureRegion frameInicial = animacionBase.getKeyFrame(0);
        this.hitboxPrincipal = new Rectangle(posicion.x, posicion.y, frameInicial.getRegionWidth(), frameInicial.getRegionHeight());
    }

    @Override
    public void cargarAnimaciones() {
        // Cargar textura base y animación
        Texture texturaBase = new Texture("sagatstand.png");
        TextureRegion[][] framesBase = TextureRegion.split(texturaBase, texturaBase.getWidth() / 4, texturaBase.getHeight());
        animacionBase = new Animation<>(0.1f, framesBase[0]);
        animacionBase.setPlayMode(Animation.PlayMode.LOOP);

        // Cargar textura y animación para golpe1 (patada)
        Texture texturaGolpe1 = new Texture("sagatkick.png");
        TextureRegion[][] framesGolpe1 = TextureRegion.split(texturaGolpe1, texturaGolpe1.getWidth() / 5, texturaGolpe1.getHeight());
        animacionGolpe1 = new Animation<>(0.1f, framesGolpe1[0]);
        animacionGolpe1.setPlayMode(Animation.PlayMode.NORMAL);

        // Cargar textura y animación para golpe2 (puño)
        Texture texturaGolpe2 = new Texture("sagatshot.png");
        TextureRegion[][] framesGolpe2 = TextureRegion.split(texturaGolpe2, texturaGolpe2.getWidth() / 3, texturaGolpe2.getHeight());
        animacionGolpe2 = new Animation<>(0.1f, framesGolpe2[0]);
        animacionGolpe2.setPlayMode(Animation.PlayMode.NORMAL);

        animacionActual = animacionBase;
    }

    @Override
    public Golpe crearGolpe1() {
        // Crear una patada en la posición de Sagat
        float ancho = 50;  // Tamaño del golpe
        float alto = 20;   // Altura del golpe
        float x = facingRight ? posicion.x + hitboxPrincipal.width : posicion.x - ancho;
        float y = posicion.y + hitboxPrincipal.height / 2;

        return new Patada(x, y, ancho, alto, 0.5f, 20);
    }

    @Override
    public Golpe crearGolpe2() {
        // Crear un puño en la posición de Sagat
        float ancho = 40;  // Tamaño del golpe
        float alto = 20;   // Altura del golpe
        float x = facingRight ? posicion.x + hitboxPrincipal.width : posicion.x - ancho;
        float y = posicion.y + hitboxPrincipal.height / 1.5f;

        return new Puño(x, y, ancho, alto, 0.5f, 15);
    }

    @Override
    public void ejecutarGolpe1() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe1;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe1(); // Asignar golpe
        }
    }

    @Override
    public void ejecutarGolpe2() {
        if (!golpeando) {
            golpeando = true;
            animacionActual = animacionGolpe2;
            tiempoAnimacion = 0;
            golpeActual = crearGolpe2(); // Asignar golpe
        }
    }
}

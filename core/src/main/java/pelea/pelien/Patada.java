package pelea.pelien;

public class Patada extends Golpe {
    public Patada(float x, float y, float ancho, float alto, float duracion) {
        super(x, y, ancho, alto, duracion, "sagatkick.png");
    }

    @Override
    public void aplicarEfecto(Jugador objetivo) {
        System.out.println("¡Patada impactó al jugador!");
    }
}

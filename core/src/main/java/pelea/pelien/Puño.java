package pelea.pelien;

public class Puño extends Golpe {
    public Puño(float x, float y, float ancho, float alto, float duracion) {
        super(x, y, ancho, alto, duracion, "sagatshot.png"); // Ruta de la textura del puño
    }

    @Override
    public void aplicarEfecto(Jugador objetivo) {
        System.out.println("¡Puño impactó al jugador!");
    }
}

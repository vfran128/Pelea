package pelea.pelien;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pelea.pelien.globals.GameData;
import pelea.pelien.globals.NetworkData;

public class Jugador {
    private Luchador luchador;
    private int teclaMoverIzquierda;
    private int teclaMoverDerecha;
    private int teclaSaltar;
    private int teclaGolpe1;
    private int teclaGolpe2;

    public Jugador(Luchador luchador, int teclaMoverIzquierda, int teclaMoverDerecha, int teclaSaltar, int teclaGolpe1, int teclaGolpe2) {
        this.luchador = luchador;
        this.teclaMoverIzquierda = teclaMoverIzquierda;
        this.teclaMoverDerecha = teclaMoverDerecha;
        this.teclaSaltar = teclaSaltar;
        this.teclaGolpe1 = teclaGolpe1;
        this.teclaGolpe2 = teclaGolpe2;
    }

    public Luchador getLuchador() {
        return luchador;
    }

    public void actualizar(float delta) {
        if (Gdx.input.isKeyPressed(teclaMoverIzquierda)) {
            luchador.moverIzquierda(delta);
        } else if (Gdx.input.isKeyPressed(teclaMoverDerecha)) {
            luchador.moverDerecha(delta);
        } else {
            luchador.detenerMovimiento();
        }

        if (Gdx.input.isKeyJustPressed(teclaSaltar)) {
            luchador.saltar();
        }

        if (Gdx.input.isKeyJustPressed(teclaGolpe1)) {
            luchador.ejecutarGolpe1();
        }

        if (Gdx.input.isKeyJustPressed(teclaGolpe2)) {
            luchador.ejecutarGolpe2();
            NetworkData.serverThread.sendMessageToAll("moveentity!" + luchador.getPosicion().x + "!" + luchador.getPosicion().y + "!" + luchador.getClass().getSimpleName() + "!");
        }

        luchador.actualizar(delta);

        NetworkData.serverThread.sendMessageToAll("moveentity!" + luchador.getPosicion().x + "!" + luchador.getPosicion().y + "!" + luchador.getClass().getSimpleName());
    }

    public void renderizar(SpriteBatch batch) {
        luchador.renderizar(batch);
    }

    public void renderizarDebug() {
        luchador.renderizarDebug();
    }

    public void dispose() {
        luchador.dispose();
    }
}

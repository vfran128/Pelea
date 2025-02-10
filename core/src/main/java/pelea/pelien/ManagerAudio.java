package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

public class ManagerAudio {
    private Sound efectoSonido;
    private Music musicaFondo;

    public void reproducirSonido (String nombre){
        efectoSonido = Gdx.audio.newSound(Gdx.files.internal(nombre));
        efectoSonido.play(1.0f);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                efectoSonido.dispose();
            }
        }, 2.0f);
    }

    public void reproducirMusica (String nombre){
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal(nombre));
        musicaFondo.play();
        musicaFondo.setLooping(true);
    }

    public void disposeMusica () {
        musicaFondo.dispose();
    }
    public void disposeSonido (String sonidoNombre){
        efectoSonido.dispose();
    }
}

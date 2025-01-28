package pelea.pelien;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Botonera {
    private Stage stage;

    public Botonera(Stage stage) {
        this.stage = stage;
    }

    public TextButton createButton(String text, Color upColor, Color downColor, float x, float y, Runnable action) {
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = createColoredDrawable(upColor);
        textButtonStyle.down = createColoredDrawable(downColor);
        textButtonStyle.fontColor = Color.BLACK;

        TextButton button = new TextButton(text, textButtonStyle);
        button.setPosition(x, y);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float buttonX, float buttonY) {
                action.run();
            }
        });

        stage.addActor(button);
        return button;
    }

    private Drawable createColoredDrawable(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(texture);
    }
}

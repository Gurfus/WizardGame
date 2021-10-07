package cat.xtec.ioc.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Settings;

public class Spacecraft extends Actor {

    // Distintes posicions de la spacecraft, recta, pujant i baixant
    public static final int SPACECRAFT_STRAIGHT = 0;
    public static final int SPACECRAFT_UP = 1;
    public static final int SPACECRAFT_DOWN = 2;
    TextureRegion[] regionsMov;

    // Paràmetres de la spacecraft
    private Vector2 position;
    private int width, height;
    private int direction;

    private Rectangle collisionRect;

    //TODO Exercici 4- variable per a establir l'estat pause
    private Action pauseAction;
    private boolean pause;


    public Spacecraft(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width =width;
        this.height = height;
        position = new Vector2(x, y);
        // Inicialitzem la spacecraft a l'estat normal
        direction = SPACECRAFT_STRAIGHT;

        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();

        // Per a la gestio de hit
        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        //TODO Exercici 4- Definim l'estat inicial
        pause = false;

    }


    //TODO Exercici 4- s'activa o desactiva l'estat de pause
    public void startPause() {
        pause = true;
        pauseAction = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.5f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        this.addAction(pauseAction);
    }
    public void stopPause() {
        pause = false;
        this.removeAction(pauseAction);
        Color color = this.getColor();
        this.setColor(color.r, color.g, color.b, 1.0f);
    }

    //TODO Exercici 4 - Si el joc està pausat no s'actualitza
    public void act(float delta) {
        // TODO: Exercici 4 - Condició per controlar l'estat
        super.act(delta);
        if(!pause) {

            // Movem la spacecraft depenent de la direcció controlant que no surti de la pantalla
            switch (direction) {
                case SPACECRAFT_UP:
                    if (this.position.y - Settings.SPACECRAFT_VELOCITY * delta >= 0) {
                        this.position.y -= Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_DOWN:
                    if (this.position.y + height + Settings.SPACECRAFT_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                        this.position.y += Settings.SPACECRAFT_VELOCITY * delta;
                    }
                    break;
                case SPACECRAFT_STRAIGHT:
                    break;
            }

            collisionRect.set(position.x, position.y + 3, width, 10);
            setBounds(position.x, position.y, width, height);
        }

    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Canviem la direcció de la spacecraft: Puja
    public void goUp() {
        direction = SPACECRAFT_UP;
    }

    // Canviem la direcció de la spacecraft: Baixa
    public void goDown() {
        direction = SPACECRAFT_DOWN;
    }

    // Posem la spacecraft al seu estat original
    public void goStraight() {
        direction = SPACECRAFT_STRAIGHT;
    }

    // Obtenim el TextureRegion depenent de la posició de la spacecraft
    public TextureRegion getSpacecraftTexture() {

        switch (direction) {

            case SPACECRAFT_STRAIGHT:
            //TODO Exercici 1.2  Carrega el wizard
                return  AssetManager.wizardR;
            default:
                return   AssetManager.wizardR;
        }
    }

    public void reset() {

        // La posem a la posició inicial i a l'estat normal
        position.x = Settings.SPACECRAFT_STARTX;
        position.y = Settings.SPACECRAFT_STARTY;
        direction = SPACECRAFT_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        //TODO Exercici 1.2  dibuixa el wizard
        batch.draw(getSpacecraftTexture(), position.x, position.y, width, height);
    }


    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public TextureRegion[] getRegionsMov() {
        return regionsMov;
    }

    public void setRegionsMov(TextureRegion[] regionsMov) {
        this.regionsMov = regionsMov;
    }
}

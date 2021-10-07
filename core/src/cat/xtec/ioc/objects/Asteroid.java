package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;


import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class Asteroid extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetAsteroid;
    //TODO Exercici 4- variable per a establir l'estat pause i l'animació a l'objecte
    private boolean pause;
    private Action AccioPausa;
    private RepeatAction repe;

    public Asteroid(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        assetAsteroid = r.nextInt(2);

        setOrigin();


        //TODO Exercici 4-: Definim l'estat inicial
        pause = false;

    }

    public void setOrigin() {

        this.setOrigin(width/2 + 1, height/2);

    }
    //TODO Exercici 4 - Si el joc està pausat, no actualitzem l'estat
    @Override
    public void act(float delta) {
        if(!pause){
            super.act(delta);
            // Actualitzem el cercle de col·lisions (punt central de l'asteroid i el radi.
            collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);

        }

    }



    @Override
    public void reset(float newX) {
        super.reset(newX);
        // Obtenim un número al·leatori entre MIN i MAX
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID);
        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = height = 34 * newSize;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        assetAsteroid = r.nextInt(2);
        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // TODO Exericici 1.2 -dibuixa el fantasma
        batch.draw(AssetManager.ghost[assetAsteroid], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    // Retorna true si hi ha col·lisió
    public boolean collides(Spacecraft nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan l'asteroid estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }
    //TODO Exercici 4- StarPause/StopPause activa i desactiva la pausa
    public void startPause() {
        pause = true;
        AccioPausa = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.5f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        this.addAction(AccioPausa);
        this.removeAction(repe);
    }
    public void stopPause() {
        pause = false;
        this.clearActions();
        Color color = this.getColor();
        this.setColor(color.r, color.g, color.b, 1.0f);
    }

}

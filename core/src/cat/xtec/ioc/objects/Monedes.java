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
//TODO Exercici 3- Bonus, classe que definex la moneda
public class Monedes extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int monedesAsset,probabilitat;
    //TODO Exercici 4- variable per a establir l'estat pause
    private boolean pause;
    private Action AccioPausa;
    private RepeatAction Repe;

    public Monedes(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        // Creem el cercle
        collisionCircle = new Circle();

        /* Accions */
        r = new Random();
        monedesAsset = r.nextInt(2);

        setOrigin();

        //TODO Exercici 4- Definim l'estat inicial del pause
        pause = false;

    }

    public void setOrigin() {

        this.setOrigin(width/2 + 1, height/2);

    }
    //TODO Exercici 4 -No s'actualitza si el joc esta pausat
    @Override
    public void act(float delta) {
        if(!pause){
            super.act(delta);
            // Actualitzem el cercle de col·lisions (punt central de la moneda i el radi.
            collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);

        }

    }



    @Override
    public void reset(float newX) {
        super.reset(newX);
        // Obtenim un número al·leatori entre MIN i MAX
        float newSize = Methods.randomFloat(Settings.MIN_MONEDES, Settings.MAX_MONEDES);
        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = height = 32 *newSize;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        //TODO Exercici 3.1 probabilitat de que 90% moneda groga, 10% blava i velecitat
        probabilitat = r.nextInt(100);
        if(probabilitat >= 90){
            monedesAsset = 0;
            this.velocity= Settings.SCORE_SPEED50;//bonus speed -250
        }else{
            monedesAsset = 1;
            this.velocity = Settings.SCORE_SPEED10;//bonus speed -175
        }

        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //TODO Exercici 3.1 dibuixa la moneda random i amb probabilitat, que ha sortit
        batch.draw(AssetManager.monedesR[monedesAsset], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    // Retorna true si hi ha col·lisió
    //TODO Exercici 3.1 detecta si hi ha col·lisió amb una moneda
    public boolean collides(Spacecraft nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan la moneda estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }
    //TODO Exercici 4- StarPause/StopPause activa i desactiva la pausa
    public void startPause() {
        pause = true;
        AccioPausa = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.5f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        //this.addAction(pauseAction);
        this.removeAction(Repe);
    }
    public void stopPause() {
        pause = false;
        this.clearActions();
//        this.addAction(repeat);
        Color color = this.getColor();
        this.setColor(color.r, color.g, color.b, 1.0f);
    }
    //TODO Exercici 3.1 getter per poder saber quina moneda es blava o groga
    public int getMonedesAsset() {
        return monedesAsset;
    }

}

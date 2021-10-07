package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;

import cat.xtec.ioc.helpers.AssetManager;

public class Background extends Scrollable {
    //TODO Exercici 4- variable per a establir l'estat pause
    private boolean pause;
    public Background(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.disableBlending();
        batch.draw(AssetManager.regionsMov, position.x, position.y, width, height);
        batch.enableBlending();
    }
    //TODO Exercici 4 - Si el joc està pausat, no actualitzem l'estat
    @Override
    public void act(float delta) {
        if(!pause) {
            super.act(delta);
        }
    }

    //TODO Exercici 4 - Activa i desactiva el pause
    public void startPause() {
        pause = true;
    }
    public void stopPause() { pause = false; }
}

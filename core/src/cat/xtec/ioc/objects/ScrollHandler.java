package cat.xtec.ioc.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;
    //TODO Exercici 4- Variable per saber el estat de pausa
    private boolean pause;

    // Asteroides i monedes
    int numAsteroids,numMonedes,assetMoneda;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Monedes> monedes;
    int posMonedesArray;

    // Objecte Random
    Random r,m;

    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();
        m = new Random();
        // Comencem amb 3 asteroids
        numAsteroids = 3;
        numMonedes = 3;
        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();
        monedes = new ArrayList<Monedes>();
        //TODO Exercici 4- estat inicial pausa
        pause = false;

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
        //TODO Exercici 3.1 - Mida aleatoria per la moneda
        float newSizeM = Methods.randomFloat(Settings.MIN_MONEDES, Settings.MAX_MONEDES) * 32;

        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
        asteroids.add(asteroid);
        addActor(asteroid);
        //TODO Exercici 3.1- S'afegeix la primera moneda al array i al grup
        Monedes moneda = new Monedes(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSizeM), newSizeM, newSizeM, Settings.ASTEROID_SPEED);
        monedes.add(moneda);
        addActor(moneda);

        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }
        //TODO Exercici 3.1- Es creen monedes aleatoriament amb una mida molt semblant
        for (int i = 1; i < numMonedes; i++) {
            // Creem la mida al·leatòria
            newSizeM = Methods.randomFloat(Settings.MIN_MONEDES, Settings.MAX_MONEDES) * 32;
            // Afegim l'asteroid.
            moneda = new Monedes(monedes.get(monedes.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSizeM), newSizeM, newSizeM, Settings.SCORE_SPEED10);
            // Afegim l'asteroide a l'ArrayList
            monedes.add(moneda);
            // Afegim l'asteroide al grup d'actors
            addActor(moneda);
        }

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }
        for (int i = 0; i < asteroids.size(); i++) {

            Asteroid asteroid = asteroids.get(i);
            if (asteroid.isLeftOfScreen()) {
                if (i == 0) {
                    asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
        for (int i = 0; i < monedes.size(); i++) {

            Monedes monede = monedes.get(i);
            if (monede.isLeftOfScreen()) {
                if (i == 0) {
                    monede.reset(monedes.get(monedes.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    monede.reset(monedes.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
    }

    public boolean collides(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau)) {
                return true;
            }
        }
        return false;
    }
    public boolean MonedesIn(Spacecraft nau) {

        //TODO Exercici 3.1- Comprovem si hi ha col·lisió entre el wizard i la moneda
        for (Monedes monedas : monedes) {
            if (monedas.collides(nau)) {
                assetMoneda = monedas.getMonedesAsset();
                posMonedesArray = monedes.indexOf(monedas);
                return true;
            }
        }
        return false;
    }

    public void reset() {

        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        monedes.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {

            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);

        }
        //TODO Exercici 3-1 calculem les noves monedes
        for (int i = 1; i < monedes.size(); i++) {
            monedes.get(i).reset(monedes.get(i - 1).getTailX() + Settings.ASTEROID_GAP);

        }
    }

    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
    public ArrayList<Monedes> getMonedes() {
        return monedes;
    }

    //TODO Exercici 4- Pausem o no els elements definits
    public void setPause(){
        pause = true;
        bg.startPause();
        bg_back.startPause();
        for (Asteroid a : asteroids){
            a.startPause();
        }
        for (Monedes a : monedes){
            a.startPause();
        }

    }
    public void stopPause(){
        pause = false;
        bg.stopPause();
        bg_back.stopPause();
        for (Asteroid a : asteroids){
            a.stopPause();
        }
        for (Monedes a : monedes){
            a.stopPause();
        }
    }

    public int getAssetMoneda() {
        return assetMoneda;
    }

}
package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Monedes;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Settings;

import static cat.xtec.ioc.screens.GameScreen.GameState.PAUSE;
import static cat.xtec.ioc.screens.GameScreen.GameState.RUNNING;


public class GameScreen implements Screen {

    // Els estats del joc

    public enum GameState {

        READY, RUNNING, GAMEOVER, PAUSE, RESUME

    }

    private GameState currentState;
    //TODO Exercici 5.2 i 5.3 - Declarem la peferences per fer persistent
    Preferences preferences = Gdx.app.getPreferences("My preferences");
    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;
    private ScrollHandler scrollHandler;

    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;

    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    //TODO Exercici 5.1- 5.2 - 5.3 variables label
    private GlyphLayout textLayout,score,highScore;
    //TODO Exercici 3.1 Declaracio de les variables
    boolean tocat = false;
    int contador,highScoreInt;
    ArrayList<Monedes> monedesArrayList;

    public GameScreen(Batch prevBatch, Viewport prevViewport) {

        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);

        batch = stage.getBatch();

        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT);
        scrollHandler = new ScrollHandler();

        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);
        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");
        scrollHandler.setName("scroll");
        //TODO Exercici 4- Carrega la imatege de pause
        Image pause = new Image(AssetManager.pause);
        pause.setName("pause");
        pause.setPosition((Settings.GAME_WIDTH) - pause.getWidth() - 5, 5);
        stage.addActor(pause);

        // Iniciem el GlyphLayout
        //TODO Exercici 5.1 utilitzo el label per mostrar el missatege
        textLayout = new GlyphLayout();
        textLayout.setText(AssetManager.font, "Are you\nready?");
        //TODO Exercici 5.2 i 5.3 Delcarem els labels per escriure
        highScore = new GlyphLayout();
        highScore.setText(AssetManager.font,"High Score: "+ preferences.getInteger("highScore",0));
        score = new GlyphLayout();
        score.setText(AssetManager.font,"Score:"+contador);
        currentState = GameState.READY;

        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());

        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;
        //TODO Exercici 3.1 Recollim totes les monedes
        monedesArrayList = scrollHandler.getMonedes();
        Monedes monedes;

        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }
        shapeRenderer.end();

        for (int i = 0; i < monedesArrayList.size(); i++) {

            monedes = monedesArrayList.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(monedes.getX() + monedes.getWidth() / 2, monedes.getY() + monedes.getWidth() / 2, monedes.getWidth() / 2);
        }
        shapeRenderer.end();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        // Dibuixem tots els actors de l'stage
        stage.draw();

        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {

            case GAMEOVER:
                updateGameOver(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case READY:
                updateReady();
                break;
            case PAUSE:
                //TODO Exercici 4- Escriu pause
                textLayout.setText(AssetManager.font, "Pause");
                updatePause(delta);
                break;
            case RESUME:
                resume();
                break;


        }

        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        //TODO Exericci 5.2 i 5.3 escriu la puntuacio més alta guardada
        AssetManager.font.draw(batch, highScore, 8,8);
        //stage.addActor(textLbl);
        batch.end();

    }

    private void updateRunning(float delta) {
        stage.act(delta);

        if (scrollHandler.collides(spacecraft)) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            textLayout.setText(AssetManager.font, "Game Over :'(");
            //TODO Exercici 5.1 s'usutilitza el mateix glyph per posar el msg despres de perder
            if(contador < 100){
                textLayout.setText(AssetManager.font, "Looooser");
            }else if(contador >= 100 && contador < 150 ){
                textLayout.setText(AssetManager.font, "Good Game");
            }else if(contador >= 150){
                textLayout.setText(AssetManager.font, "You are pro player !!");
            }

            currentState = GameState.GAMEOVER;
        }
        //TODO Exercici 3.1 i 3.2- Banderes per detectar nomes un hit, suma nomes un cop(50 i 10 punts) i fa sonar el so un cop
        if(scrollHandler.MonedesIn(spacecraft) && !tocat){
            tocat = true;
            if(scrollHandler.getAssetMoneda() == 0){
                contador += 50;
                AssetManager.coinPlus.play();

            }else if(scrollHandler.getAssetMoneda() == 1){
                contador += 10;
                AssetManager.coinSound.play();
            }

        }else if(!scrollHandler.MonedesIn(spacecraft) && tocat){
            tocat = false;
        }
        // TODO Exercici 5.2 i 5.3 Guarda i mostra la puntuacio més alta respecte a l'actual
        highScoreInt = preferences.getInteger("highScore",0);
        if (highScoreInt >= contador){
            highScore.setText(AssetManager.font,"High Score: "+highScoreInt);
        }else{
            preferences.putInteger("highScore",contador);
            preferences.flush();
        }
        TextureRegion flip = (TextureRegion) AssetManager.wizardR;
        batch.begin();
        //TODO Exercici 3.1 Suma cada moneda al contador i dibuixa el label a la esquerra
        score.setText(AssetManager.font,"Score:"+contador);
        AssetManager.font.draw(batch, score, 8,8);
        batch.draw(flip,(spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();
    }



    private void updateGameOver(float delta) {
        stage.act(delta);

        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH - textLayout.width) / 2, (Settings.GAME_HEIGHT - textLayout.height) / 2);
        // TODO Exercici 3.1 Quan perds posa el contador a 0
        contador = 0;
        score.setText(AssetManager.font,"Score:"+contador);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        TextureRegion flip = (TextureRegion) AssetManager.explosionAnim.getKeyFrame(explosionTime,false);
        batch.draw(flip , (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();

        explosionTime += delta;

    }

    public void reset() {

        // Posem el text d'inici
        textLayout.setText(AssetManager.font, "Are you\nready?");
        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.reset();

        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;

    }
    // TODO Exercici 4- Actualització del estat pause
    private void updatePause(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textLayout, (Settings.GAME_WIDTH / 2) - textLayout.width / 2, (Settings.GAME_HEIGHT / 2) - textLayout.height / 2);
        batch.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        // TODO Exercici 4- si el joc passa en 2n pla es pausa la música i el joc
        AssetManager.music.pause();
        if (getCurrentState() == RUNNING) {
            setCurrentState(PAUSE);
        }
    }

    @Override
    public void resume() {
        // TODO Exercici 4- Si tornem torna tmb la música
        AssetManager.music.play();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        switch (currentState) {
            case PAUSE:
                // TODO Exercici 4-  ocultem els elements
                amagar();
                //TODO Exercici 4- Tots els elements en pausa
                spacecraft.startPause();
                scrollHandler.setPause();
                break;
            case READY:
                amagar();
                break;
            case RUNNING:
                // TODO Exercici 4- Torna tot al seu estat
                enseyar();
                //TODO Exercici 4 Tots elements fora d'estat pausa
                spacecraft.stopPause();
                scrollHandler.stopPause();
                break;

        }

        this.currentState = currentState;
    }
    private void amagar() {
        stage.getRoot().findActor("pause").setVisible(false);
    }

    private void enseyar() {
        stage.getRoot().findActor("pause").setVisible(true);
    }

}

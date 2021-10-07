package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cat.xtec.ioc.Anim.ExploAnimation;

public class AssetManager {



    // Sprite Sheet
    public static Texture ghostS,monedesS,wizardS;

    //TODO Exercici 1- classe creada per fer la animacio de la explocio
    public static ExploAnimation explo = new ExploAnimation();
    //TODO Exercici 1- Textura del fons joc
    public static Texture background;
    public static TextureRegion regionsMov;

    //  //TODO Exercici 1- Textura wizard
    public  static  Texture wizard;
    public static TextureRegion wizardR;
    public static TextureRegion wizardRR;

    //  //TODO Exercici 1- Textura fantasma
    public static TextureRegion[] ghost;
    public static Animation ghostAnima;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    // Sons
    public static Sound explosionSound;
    public static Music music;

    //TODO Exercici 1- Textura i sons monedes
    public static Texture monedes;
    public static TextureRegion[] monedesR;
    public static Sound coinSound;
    public static Sound coinPlus;

    // Font
    public static BitmapFont font;

    // TODO Exercici 4- Textura de pausa
    public static Texture pause;



    public static void load() {

        //TODO Exercici 1- Carreguem textures, amb el seu sprite
        ghostS = new Texture(Gdx.files.internal("fantasmaV2.png"));
        ghostS.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //TODO Exercici 1- Sprites Monedes
        monedesS = new Texture(Gdx.files.internal("monedesBA.png"));
        monedesS.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        monedesR = new TextureRegion[2];

        //TODO Exercici 1- Capturem els pixels del sprite, es coloca be amb un flip
        monedesR[0] = new TextureRegion(monedesS,  0, 0, 64, 64);
        monedesR[0].flip(false, true);
        monedesR[1] = new TextureRegion(monedesS,  64, 0, 64, 64);
        monedesR[1].flip(false, true);

        //TODO Exercici 1- Sprites Wizard
        wizardS = new Texture(Gdx.files.internal("wiz.png"));
        wizardS.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //TODO Exercici 1- Capturem els pixels del sprite, es coloca be amb un flip
        wizardRR = new TextureRegion(wizardS,0,0,250,250);
        wizardRR.flip(false,true);

        //TODO Exercici 1 - Carrega els 4 estats del enemics
        ghost = new TextureRegion[4];
        ghost[0] = new TextureRegion(ghostS,  0, 0, 64, 80);
        ghost[0].flip(false, true);
        ghost[1] = new TextureRegion(ghostS,  0, 80, 64, 80);
        ghost[1].flip(false, true);
        ghost[2] = new TextureRegion(ghostS,  0, 160, 64, 80);
        ghost[2].flip(false, true);
        ghost[3] = new TextureRegion(ghostS,  0, 240, 64, 80);
        ghost[3].flip(false, true);

        //  //TODO Exercici 1- Animacio fantasma
        ghostAnima = new Animation(0.025f, ghost);
        ghostAnima.setPlayMode(Animation.PlayMode.LOOP);

        // TODO Exercici 4 - Textura de pausa
        pause = new Texture(Gdx.files.internal("pause.png"));


        //TODO Exercici 1- Es crea l'animació amb la classe
        explo.create();
        explosion = explo.getWalkFrames();

        // Finalment creem l'animació
        explosionAnim = new Animation(0.05f,
                explosion);
        explosionAnim.setPlayMode(Animation.PlayMode.NORMAL);

        // //TODO Exercici 1- Capturem els pixels del sprite, es coloca be amb un flip, wizard
        wizard = new Texture(Gdx.files.internal("wiz.png"));
        wizardR = new TextureRegion(wizard,250,250);
        wizard.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        wizardR.flip(false,true);

        // TODO Exercici 1- Capturem els pixels del sprite, es coloca be amb un flip, fons
        background = new Texture(Gdx.files.internal("bosc.png"));
        regionsMov = new TextureRegion(background);
        regionsMov.flip(false,true);


        /******************************* Sounds *************************************/
        // Explosió
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        coinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/coin1.wav"));
        coinPlus = Gdx.audio.newSound(Gdx.files.internal("sounds/coinplus.wav"));
        // Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/TheForest.wav"));
        music.setVolume(1f);
        music.setLooping(true);



        /******************************* Text *************************************/
        // Font space
        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);


    }



    public static void dispose() {

        // Alliberem els recursos gràfics i de audio
        explo.dispose();
        ghostS.dispose();
        explosionSound.dispose();
        music.dispose();


    }
}

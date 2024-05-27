package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Screens.SignUp.CreateSignUpScreen;
import com.mygdx.game.Screens.SignUp.SignUpScreen;
import com.mygdx.game.Screens.Splash.SplashScreen;
import com.mygdx.game.Utils.DataBaseAdapter;
import com.mygdx.game.Utils.Toast;


public class Main extends Game {
    public Viewport screenPort;
    public float volume = 1f;
    public int WIDTH;
    public int HEIGHT;
    private boolean accountCreated = false;
    private Preferences prefs;

    @Override
    public void create() {

        prefs = Gdx.app.getPreferences("Preferences");
        screenPort = new ScreenViewport();
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        DataBaseAdapter.game = this;

        if (!prefs.getString("UserID").isEmpty()) {
            Account.setName(prefs.getString("UserName"));
            setScreen(new SplashScreen(this));
        } else {
            setScreen(new SignUpScreen(this));
        }
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public boolean isAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(boolean accountCreated) {
        this.accountCreated = accountCreated;
    }

    public Toast.ToastFactory createToastFactory(Color backgroundColor, Color fontColor, float fadingDuration) {
        return new Toast.ToastFactory.Builder()
                .font(CreateSignUpScreen.createCustomFont(35))
                .backgroundColor(backgroundColor)
                .fadingDuration(fadingDuration)
                .fontColor(fontColor)
                .margin(20)
                .positionY(100)
                .build();
    }

    public static Texture getCurrentIdleTexture() {
        String currentSkin = Account.getCurrentSkin();
        Texture spriteSheet;
        if (currentSkin != null) {
            switch (currentSkin) {
                case "doux":
                    spriteSheet = new Texture("spritesheet/doux/Idle.png");
                    break;
                case "vita":
                    spriteSheet = new Texture("spritesheet/vita/Idle.png");
                    break;
                case "tard":
                    spriteSheet = new Texture("spritesheet/tard/Idle.png");
                    break;
                default:
                    spriteSheet = new Texture("spritesheet/mort/Idle.png");
                    break;
            }
        } else {
            spriteSheet = new Texture("spritesheet/mort/Idle.png");
        }
        return spriteSheet;
    }

    public static Texture getCurrentRunTexture() {
        String currentSkin = Account.getCurrentSkin();
        Texture spriteSheet;
        if (currentSkin != null) {
            switch (currentSkin) {
                case "doux":
                    spriteSheet = new Texture("spritesheet/doux/Run.png");
                    break;
                case "vita":
                    spriteSheet = new Texture("spritesheet/vita/Run.png");
                    break;
                case "tard":
                    spriteSheet = new Texture("spritesheet/tard/Run.png");
                    break;
                default:
                    spriteSheet = new Texture("spritesheet/mort/Run.png");
                    break;
            }
        } else {
            spriteSheet = new Texture("spritesheet/mort/Run.png");
        }

        return spriteSheet;
    }

    public static Texture getBirdTexture(){
        return new Texture("spritesheet/bird/Flying.png");
    }

}

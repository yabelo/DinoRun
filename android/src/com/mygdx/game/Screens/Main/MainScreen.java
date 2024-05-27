package com.mygdx.game.Screens.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.ChangeSkin.ChangeSkinScreen;


public class MainScreen implements Screen {
    public SpriteBatch batch;
    private Stage stage;
    private Button playButton;
    private Button settingsButton;
    private final Main game;
    private Image characterImage;
    private Animation<TextureRegion> characterAnimation;
    private float stateTime = 0f;
    private Texture backgroundImage;

    public MainScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundImage = new Texture("assets/Images/background_image.jpg");
        show();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        CreateMainScreen createMainScreen = new CreateMainScreen(game);
        playButton = createMainScreen.createPlayButton();
        Label playLabel = createMainScreen.createPlayLabel();

        Texture characterTexture = Main.getCurrentIdleTexture();
        TextureRegion[][] characterFrames = TextureRegion.split(characterTexture, characterTexture.getWidth() / 3, characterTexture.getHeight());
        characterAnimation = new Animation<>(0.25f, characterFrames[0]);

        characterImage = new Image(new Sprite(characterAnimation.getKeyFrame(stateTime, true)));
        characterImage.setScale(20f);
        characterImage.setPosition(0,
                (game.HEIGHT - characterImage.getHeight() * characterImage.getScaleY()) / 2);

        characterImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChangeSkinScreen(game));
            }
        });

        Image greenBackground = createMainScreen.createGreenBackgroundImage(characterImage);
        Label changeSkinLabel = createMainScreen.createChangeSkinLabel();
        settingsButton = createMainScreen.createSettingsButton();
        Label playerNameLabel = createMainScreen.createPlayerNameLabel();
        Label coinsLabel = createMainScreen.createCoinsLabel();
        Button coinsButton = createMainScreen.createCoinsButton();
        Button leaderboardButton = createMainScreen.createLeaderboardButton();

        stage.addActor(greenBackground);
        stage.addActor(changeSkinLabel);
        stage.addActor(characterImage);
        stage.addActor(playButton);
        stage.addActor(playLabel);
        stage.addActor(settingsButton);
        stage.addActor(playerNameLabel);
        stage.addActor(coinsLabel);
        stage.addActor(coinsButton);
        stage.addActor(leaderboardButton);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (stage != null) {
            stage.act();
            stage.draw();
        }

        stateTime += delta;
        TextureRegion currentFrame = characterAnimation.getKeyFrame(stateTime, true);
        characterImage.setDrawable(new TextureRegionDrawable(currentFrame));
    }


    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        playButton.setDisabled(true);
        settingsButton.setDisabled(true);
        backgroundImage.dispose();
        batch.dispose();
    }

    public static BitmapFont createCustomFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/04B_30__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }


}

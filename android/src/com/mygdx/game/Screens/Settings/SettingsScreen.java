package com.mygdx.game.Screens.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.mygdx.game.Main;

public class SettingsScreen implements Screen {
    private Stage stage;
    private final Main game;
    private Button backButton;
    private Slider volumeSlider;
    private ImageButton.ImageButtonStyle volumeStyle;
    private final Preferences prefs = Gdx.app.getPreferences("Preferences");
    public SettingsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        CreateSettingsScreen createSettingsScreen = new CreateSettingsScreen(game);
        backButton = createSettingsScreen.createBackButton();
        Button exitButton = createSettingsScreen.createExitButton();
        volumeSlider = createSettingsScreen.createVolumeSlider();

        volumeStyle = new ImageButton.ImageButtonStyle();
        volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_on.png")));
        ImageButton volume = createSettingsScreen.createVolumeImageButton(volumeStyle);

        stage.addActor(backButton);
        stage.addActor(volumeSlider);
        stage.addActor(exitButton);
        stage.addActor(volume);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        float volume = volumeSlider.getValue();
        if(volume == 0){
            volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_off.png")));
        }
        else if(volume <= 0.5){
            volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_half.png")));
        }
        else{
            volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_on.png")));
        }
        prefs.putFloat("volume", volume);
        prefs.flush();
        game.setVolume(volume);
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
        stage.dispose();
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
        volumeSlider.setDisabled(true);
        backButton.setDisabled(true);
    }
}

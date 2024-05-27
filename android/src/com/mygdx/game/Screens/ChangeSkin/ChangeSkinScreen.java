package com.mygdx.game.Screens.ChangeSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.Main;


public class ChangeSkinScreen implements Screen {

    private final Main game;
    private Stage stage;
    public ChangeSkinScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        CreateChangeSkinScreen createChangeSkinScreen = new CreateChangeSkinScreen(game);

        Button backButton = createChangeSkinScreen.createBackButton();
        Image mortImage = createChangeSkinScreen.createMortImage();
        Label mortLabel = createChangeSkinScreen.createMortLabel();
        Image douxImage = createChangeSkinScreen.createDouxImage();
        Label douxLabel = createChangeSkinScreen.createDouxLabel();
        Label douxCostLabel = createChangeSkinScreen.createDouxCostLabel();
        Image vitaImage = createChangeSkinScreen.createVitaImage();
        Label vitaLabel = createChangeSkinScreen.createVitaLabel();
        Label vitaCostLabel = createChangeSkinScreen.createVitaCostLabel();
        Image tardImage = createChangeSkinScreen.createTardImage();
        Label tardLabel = createChangeSkinScreen.createTardLabel();
        Label tardCostLabel = createChangeSkinScreen.createTardCostLabel();

        Label coinsLabel = createChangeSkinScreen.createCoinsLabel();
        Button coinsButton = createChangeSkinScreen.createCoinsButton();

        stage.addActor(backButton);
        stage.addActor(mortImage);
        stage.addActor(mortLabel);
        stage.addActor(douxImage);
        stage.addActor(douxLabel);
        stage.addActor(vitaImage);
        stage.addActor(vitaLabel);
        stage.addActor(tardImage);
        stage.addActor(tardLabel);
        if(douxCostLabel != null)
            stage.addActor(douxCostLabel);
        if(vitaCostLabel != null)
            stage.addActor(vitaCostLabel);
        if(tardCostLabel != null)
            stage.addActor(tardCostLabel);

        stage.addActor(coinsLabel);
        stage.addActor(coinsButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (stage != null) {
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }


}

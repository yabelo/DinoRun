package com.mygdx.game.Screens.SignUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Splash.SplashScreen;
import com.mygdx.game.Utils.Toast;

public class SignUpScreen implements Screen {

    private final Main game;
    private Stage stage;
    public static Toast toast;
    public SignUpScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        CreateSignUpScreen createSignUpScreen = new CreateSignUpScreen(game);

        final TextField usernameField = createSignUpScreen.createUsernameField();
        TextButton submitButton = createSignUpScreen.createSubmitButton();
        Label usernameUnavailableLabel = createSignUpScreen.createUsernameUnavailableLabel();
        Label creatingUserLabel = createSignUpScreen.createCreatingUserLabel();

        stage.addActor(usernameField);
        stage.addActor(submitButton);
        stage.addActor(usernameUnavailableLabel);
        stage.addActor(creatingUserLabel);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(game.isAccountCreated()){
            game.setScreen(new SplashScreen(game));
        }

        if (stage != null) {
            stage.act();
            stage.draw();
        }

        if(toast != null)
            toast.render(delta);
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
        if (stage != null)
            stage.dispose();
    }
}

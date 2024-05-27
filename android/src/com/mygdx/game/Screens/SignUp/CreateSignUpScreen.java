package com.mygdx.game.Screens.SignUp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.mygdx.game.Main;
import com.mygdx.game.Utils.DataBaseAdapter;
import com.mygdx.game.Utils.Toast;

import java.util.HashMap;
import java.util.Map;

public class CreateSignUpScreen {
    private final Main game;
    private TextField usernameField;
    private Label usernameUnavailableLabel;
    private Label creatingUserLabel;

    public CreateSignUpScreen(Main game) {
        this.game = game;
    }

    public TextField createUsernameField() {
        BitmapFont customFont = createCustomFont(35);

        NinePatch background = new NinePatch(createColoredSquareTexture(200, Color.BLACK), 8, 8, 8, 8);
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(background);

        NinePatch cursor = new NinePatch(createColoredSquareTexture(5, Color.WHITE), 1, 1, 1, 1);
        NinePatchDrawable cursorDrawable = new NinePatchDrawable(cursor);

        usernameField = new TextField("", new TextField.TextFieldStyle(customFont, Color.WHITE, cursorDrawable, null, backgroundDrawable));

        usernameField.setMessageText("username");
        usernameField.setWidth(500);
        usernameField.setHeight(50);
        usernameField.setMaxLength(15);

        usernameField.setPosition(Gdx.graphics.getWidth() / 2f - 250, Gdx.graphics.getHeight() / 2f + usernameField.getHeight());

        usernameField.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
                usernameUnavailableLabel.setVisible(false);
            }
        });

        return usernameField;
    }

    public TextButton createSubmitButton() {
        BitmapFont customFont = createCustomFont(30);

        NinePatch background = new NinePatch(createColoredSquareTexture(200, Color.BLACK), 8, 8, 8, 8);
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(background);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = customFont;
        textButtonStyle.up = backgroundDrawable;
        textButtonStyle.down = backgroundDrawable;

        TextButton submitButton = new TextButton("Submit", textButtonStyle);
        submitButton.setWidth(500);
        submitButton.setHeight(50);

        submitButton.setPosition(Gdx.graphics.getWidth() / 2f - 250, Gdx.graphics.getHeight() / 2f - submitButton.getHeight() - 10);

        submitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!isUsernameGood(usernameField.getText())) {
                    SignUpScreen.toast = game.createToastFactory(Color.BLACK, Color.RED, 1.2f).create("Failed to create your account!", Toast.Length.SHORT);
                    return;
                }

                String email = usernameField.getText() + "@example.com";
                String password = "example"; // You may want to use a secure way to handle passwords in production

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            creatingUserLabel.setVisible(true);
                            FirebaseUser firebaseUser = authResult.getUser();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("UserID", firebaseUser.getUid());
                            userData.put("Coins", 0);
                            userData.put("Meters", 0);

                            Map<String, Boolean> userSkins = new HashMap<>();
                            userSkins.put("mort", true);
                            userSkins.put("doux", false);
                            userSkins.put("vita", false);
                            userSkins.put("tard", false);
                            userData.put("Skins", userSkins);

                            FirebaseDatabase.getInstance(DataBaseAdapter.databaseUrl)
                                    .getReference("/users/" + usernameField.getText()).setValue(userData)
                                    .addOnSuccessListener(unused -> {
                                        game.getPrefs().putString("UserID", firebaseUser.getUid());
                                        game.getPrefs().putString("UserName", usernameField.getText());
                                        game.getPrefs().putInteger("UserCoins", 0);
                                        game.getPrefs().putInteger("UserMeters", 0);
                                        game.getPrefs().putString("UserCurrentSkin", "mort");
                                        game.getPrefs().putFloat("volume", 0.5f);
                                        game.getPrefs().flush();
                                        game.setAccountCreated(true);
                                    });
                        })
                        .addOnFailureListener(e -> {
                            usernameUnavailableLabel.setVisible(true);
                            String errorMessage = e.getMessage(); // Get the error message
                            Gdx.app.log("CreateSignUpScreen", "Failed to create user: " + errorMessage); // Log the error message
                            SignUpScreen.toast = game.createToastFactory(Color.BLACK, Color.RED, 1.2f).create("Failed to create your account! Error: " + errorMessage, Toast.Length.SHORT); // Show the error message in the toast
                        });

            }
        });

        return submitButton;
    }

    public Label createCreatingUserLabel() {

        Label.LabelStyle creatingUserLabelStyle = new Label.LabelStyle();
        creatingUserLabelStyle.font = createCustomFont(35);
        creatingUserLabelStyle.fontColor = Color.BLACK;

        creatingUserLabel = new Label("Creating user!!", creatingUserLabelStyle);

        creatingUserLabel.setWidth(500);
        creatingUserLabel.setHeight(50);
        creatingUserLabel.setAlignment(Align.center);

        creatingUserLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - 250,
                usernameField.getY() - 50);

        creatingUserLabel.setVisible(false);

        return creatingUserLabel;
    }

    public Label createUsernameUnavailableLabel() {

        Label.LabelStyle usernameUnavailableLabelStyle = new Label.LabelStyle();
        usernameUnavailableLabelStyle.font = createCustomFont(35);
        usernameUnavailableLabelStyle.fontColor = Color.BLACK;

        usernameUnavailableLabel = new Label("Username is already being in use", usernameUnavailableLabelStyle);

        usernameUnavailableLabel.setWidth(500);
        usernameUnavailableLabel.setHeight(50);
        usernameUnavailableLabel.setAlignment(Align.center);

        usernameUnavailableLabel.setPosition(
                Gdx.graphics.getWidth() / 2f - 250,
                usernameField.getY() - 50);

        usernameUnavailableLabel.setVisible(false);

        return usernameUnavailableLabel;
    }

    public static BitmapFont createCustomFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Roboto-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public Texture createColoredSquareTexture(int size, Color color) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, size, size);
        Texture texture = new Texture(new PixmapTextureData(pixmap, pixmap.getFormat(), false, false));
        pixmap.dispose();
        return texture;
    }

    public boolean isUsernameGood(String username) {
        return username.length() >= 3 && username.length() <= 15;
    }

}



package com.mygdx.game.Screens.Splash;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mygdx.game.Account;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Main.MainScreen;
import com.mygdx.game.Utils.DataBaseAdapter;

import java.util.Map;


public class SplashScreen implements Screen {
    private Animation<TextureRegion> splashAnimation;
    private float stateTime = 0;
    private final Main game;
    private boolean isLoadedUserData = false;
    private Image characterImage;
    private Stage stage;

    public SplashScreen(Main game) {
        this.game = game;
        updateAccountData();
        show();
    }

    public void updateAccountData() {
        final String accountName = Account.getName();
        if (accountName == null || accountName.isEmpty()) {
            // Handle the case when the accountName is not available
            isLoadedUserData = true;
            return;
        }

        FirebaseDatabase.getInstance(DataBaseAdapter.databaseUrl)
                .getReference("users/" + accountName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Convert dataSnapshot to Map or handle the data accordingly
                            Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                            if (data.containsKey("UserID")) {
                                String userId = (String) data.get("UserID");
                                game.getPrefs().putString("UserID", userId);
                                Account.setId(userId);
                            }
                            if (data.containsKey("Coins")) {
                                long coins = (long) data.get("Coins");
                                game.getPrefs().putInteger("UserCoins", (int) coins);
                                Account.setCoins((int) coins);
                            }
                            if (data.containsKey("Meters")) {
                                long meters = (long) data.get("Meters");
                                game.getPrefs().putInteger("UserMeters", (int) meters);
                                Account.setMeters((int) meters);
                            }
                            if (data.containsKey("Skins")) {
                                Map<String, Boolean> userSkins = (Map<String, Boolean>) data.get("Skins");
                                game.getPrefs().put(userSkins);
                                Gson gson = new Gson();
                                String skinsMapToString = gson.toJson(userSkins);
                                game.getPrefs().putString("UserSkins", skinsMapToString);
                                Account.setSkins(userSkins);
                            }
                        } else {
                            System.out.println("Error loading data from Firebase");
                        }

                        game.setVolume(game.getPrefs().getFloat("volume"));
                        Account.setName(game.getPrefs().getString("UserName"));
                        Account.setCurrentSkin(game.getPrefs().getString("UserCurrentSkin"));
                        game.getPrefs().flush();
                        if (Account.getName() != null && !Account.getName().isEmpty()) {
                            isLoadedUserData = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Error loading data, " + error.getMessage());
                    }

                });
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture spriteSheet = Main.getCurrentRunTexture();
        TextureRegion[][] frames = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 6, spriteSheet.getHeight());

        splashAnimation = new Animation<>(0.1f, frames[0]);

        characterImage = new Image(new Sprite(splashAnimation.getKeyFrame(stateTime, true)));
        characterImage.setScale(20f);
        characterImage.setPosition((game.WIDTH - characterImage.getWidth() * characterImage.getScaleX()) / 2,
                (game.HEIGHT - characterImage.getHeight() * characterImage.getScaleY()) / 2);


        Image image = new Image(new Texture("assets/Images/white_circle.png"));
        image.setWidth(characterImage.getWidth() * characterImage.getScaleX() + 300);
        image.setHeight(characterImage.getHeight() * characterImage.getScaleY() + 300);
        image.setPosition(
                (game.WIDTH - image.getWidth()) / 2,
                (game.HEIGHT - image.getHeight()) / 2
        );

        stage.addActor(image);
        stage.addActor(characterImage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        // && stateTime < 1.5f
        if (!isLoadedUserData) {

            if (stage != null) {
                stage.act();
                stage.draw();
            }

            TextureRegion currentFrame = splashAnimation.getKeyFrame(stateTime, true);
            characterImage.setDrawable(new TextureRegionDrawable(currentFrame));
        } else {
            game.setScreen(new MainScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height);
    }


    @Override
    public void pause() {
        // No special pause behavior is needed.
    }

    @Override
    public void resume() {
        // No special resume behavior is needed.
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

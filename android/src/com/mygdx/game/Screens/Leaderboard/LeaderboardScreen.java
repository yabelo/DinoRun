package com.mygdx.game.Screens.Leaderboard;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Splash.SplashScreen;
import com.mygdx.game.Utils.DataBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreen implements Screen {

    public enum LeaderboardMode {COINS, METERS}
    private final Main game;
    private List<LeaderboardPlayer> leaderboardList;
    private Stage stage;
    private Label userLabel;
    private Label pointsLabel;
    private LeaderboardMode leaderboardMode = LeaderboardMode.METERS;
    private TextButton coinsButton;
    private TextButton metersButton;
    private TextButton.TextButtonStyle boldButtonStyle;
    private TextButton.TextButtonStyle normalButtonStyle;
    public LeaderboardScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        leaderboardList = new ArrayList<>();

        userLabel = createUserLabel();
        pointsLabel = createPointsLabel();
        Button backButton = createBackButton();

        boldButtonStyle = createButtonStyle(true);
        normalButtonStyle = createButtonStyle(false);

        // Create buttons with initial styles
        coinsButton = createCoinsButton();
        metersButton = createMetersButton();

        // Initially set the bold style to the meters button
        metersButton.setStyle(boldButtonStyle);
        coinsButton.setStyle(normalButtonStyle);

        stage.addActor(userLabel);
        stage.addActor(pointsLabel);
        stage.addActor(backButton);
        stage.addActor(coinsButton);
        stage.addActor(metersButton);

        fetchUserDataFromFirebase();
        
        Gdx.input.setInputProcessor(stage);

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
        stage.getViewport().update(width, height, true);
    }

    private void fetchUserDataFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(DataBaseAdapter.databaseUrl);

        database.getReference("users/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leaderboardList.clear();

                userLabel.setText("");
                pointsLabel.setText("");

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.child("UserID").getValue(String.class);
                    String username = userSnapshot.getKey();
                    Integer coins = userSnapshot.child("Coins").getValue(Integer.class);
                    Integer meters = userSnapshot.child("Meters").getValue(Integer.class);

                    // Check for null values before using them
                    if (coins != null && meters != null) {
                        LeaderboardPlayer player = new LeaderboardPlayer(userId, username, coins, meters);
                        if (leaderboardList.size() < 10)
                            leaderboardList.add(player);
                    }
                }

                if(leaderboardMode.equals(LeaderboardMode.METERS))
                    leaderboardList.sort((player1, player2) -> Integer.compare(player2.getMeters(), player1.getMeters()));
                else
                    leaderboardList.sort((player1, player2) -> Integer.compare(player2.getCoins(), player1.getCoins()));

                for (LeaderboardPlayer p : leaderboardList) {
                    userLabel.setText(userLabel.getText() != null ? userLabel.getText() + "\n\n" + p.getUsername() : p.getUsername());
                    if(leaderboardMode.equals(LeaderboardMode.METERS))
                        pointsLabel.setText(pointsLabel.getText() + "\n\n" + p.getMeters());
                    else
                        pointsLabel.setText(pointsLabel.getText() + "\n\n" + p.getCoins());

                }

                stage.addActor(userLabel);
                stage.addActor(pointsLabel);

                updateButtonStyles();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Gdx.app.error("LeaderboardFragment", "Error getting usernames from FirebaseDatabase", databaseError.toException());
            }
        });
    }

    private TextButton createCoinsButton() {
        TextButton coinsButton = new TextButton("Coins", normalButtonStyle);
        coinsButton.setSize(100, 100);
        coinsButton.setPosition(Gdx.graphics.getWidth() / 2f - coinsButton.getWidth() / 2f - 120, Gdx.graphics.getHeight() - coinsButton.getHeight() - 10);

        coinsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboardMode = LeaderboardMode.COINS;
                leaderboardList.clear();
                fetchUserDataFromFirebase();
            }
        });

        return coinsButton;
    }

    private TextButton createMetersButton() {
        TextButton metersButton = new TextButton("Meters", boldButtonStyle);
        metersButton.setSize(100, 100);
        metersButton.setPosition(Gdx.graphics.getWidth() / 2f - metersButton.getWidth() / 2f + 120, Gdx.graphics.getHeight() - metersButton.getHeight() - 10);

        metersButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaderboardMode = LeaderboardMode.METERS;
                leaderboardList.clear();
                fetchUserDataFromFirebase();
            }
        });

        return metersButton;
    }


    private Label createUserLabel() {
        Label.LabelStyle labelStyle;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = createCustomFont(40);
        labelStyle.fontColor = Color.WHITE;

        userLabel = new Label("", labelStyle);
        userLabel.setWidth(200);
        userLabel.setHeight(Gdx.graphics.getHeight() - 100);
        userLabel.setPosition(Gdx.graphics.getWidth() / 2f - 400, 100);

        return userLabel;
    }


    private Label createPointsLabel() {
        Label.LabelStyle labelStyle;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = createCustomFont(40);
        labelStyle.fontColor = Color.WHITE;

        pointsLabel = new Label("", labelStyle);

        pointsLabel.setWidth(200);
        pointsLabel.setHeight(Gdx.graphics.getHeight() - 100);

        pointsLabel.setPosition(Gdx.graphics.getWidth() / 2f + 150, 100);

        return pointsLabel;
    }

    private Button createBackButton() {
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/back_button.png")));

        Button backButton = new Button(backButtonStyle);
        backButton.setSize(100, 100);
        backButton.setPosition(10, game.HEIGHT - backButton.getHeight() - 10);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SplashScreen(game));
                System.out.println("clicked");
            }
        });

        return backButton;
    }

    public static BitmapFont createCustomFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Roboto-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private TextButton.TextButtonStyle createButtonStyle(boolean bold) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = createCustomFont(bold ? 36 + 10 : 36);
        buttonStyle.fontColor = Color.WHITE;
        return buttonStyle;
    }

    private void updateButtonStyles() {
        if (leaderboardMode == LeaderboardMode.METERS) {
            metersButton.setStyle(boldButtonStyle);
            coinsButton.setStyle(normalButtonStyle);
        } else {
            metersButton.setStyle(normalButtonStyle);
            coinsButton.setStyle(boldButtonStyle);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

package com.mygdx.game.Screens.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import com.mygdx.game.Account;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.ChangeSkin.ChangeSkinScreen;
import com.mygdx.game.Screens.Leaderboard.LeaderboardScreen;
import com.mygdx.game.Screens.Play.PlayScreen;
import com.mygdx.game.Screens.Settings.SettingsScreen;

public class CreateMainScreen {

    private final Main game;
    private Button playButton = null;
    private Button settingsButton = null;
    private Button leaderboardButton = null;
    private Label coinsLabel = null;
    private Image greenBackground = null;

    public CreateMainScreen(Main game) {
        this.game = game;
    }

    public Button createPlayButton() {
        final Button.ButtonStyle playButtonStyle = new Button.ButtonStyle();
        playButtonStyle.up = new TextureRegionDrawable(createColoredSquareTexture(200, Color.YELLOW));
        playButton = new Button(playButtonStyle);
        playButton.setSize(500, 200);
        playButton.setPosition(game.WIDTH - playButton.getWidth(), game.HEIGHT / 2f - playButton.getHeight() - 200);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

        return playButton;
    }

    public Label createPlayLabel() {
        Label.LabelStyle playLabelStyle = new Label.LabelStyle();
        playLabelStyle.font = createCustomFont(72);
        playLabelStyle.fontColor = Color.BLACK;

        Label playLabel = new Label("PLAY!", playLabelStyle);
        playLabel.setAlignment(Align.center);
        playLabel.setPosition(
                playButton.getX() + playButton.getWidth() / 2 - playLabel.getWidth() / 2,
                playButton.getY() + playButton.getHeight() / 2 - playLabel.getHeight() / 2
        );

        playLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game));
            }
        });

        return playLabel;
    }

    public Image createGreenBackgroundImage(Image characterImage) {
        Texture roundedCornerTexture = createColoredSquareTexture(200, Color.GREEN);

        greenBackground = new Image(roundedCornerTexture);
        greenBackground.setWidth(characterImage.getWidth() * characterImage.getScaleX());
        greenBackground.setHeight(50);
        greenBackground.setPosition(characterImage.getX(),
                characterImage.getY() - greenBackground.getHeight());

        greenBackground.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChangeSkinScreen(game));
            }
        });

        return greenBackground;
    }

    public Label createChangeSkinLabel() {
        Label.LabelStyle changeSkinLabelStyle = new Label.LabelStyle();
        changeSkinLabelStyle.font = createCustomFont(36);
        changeSkinLabelStyle.fontColor = Color.BLACK;

        Label changeSkinLabel = new Label("Change skin", changeSkinLabelStyle);
        changeSkinLabel.setAlignment(Align.center);
        changeSkinLabel.setPosition(
                greenBackground.getX() + greenBackground.getWidth() / 2 - changeSkinLabel.getWidth() / 2,
                greenBackground.getY() + greenBackground.getHeight() / 2 - changeSkinLabel.getHeight() / 2
        );

        changeSkinLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ChangeSkinScreen(game));
            }
        });

        return changeSkinLabel;
    }

    public Button createSettingsButton() {
        Button.ButtonStyle settingsButtonStyle = new Button.ButtonStyle();
        settingsButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/settings_button.png")));

        settingsButton = new Button(settingsButtonStyle);
        settingsButton.setSize(100, 100);
        settingsButton.setPosition(game.WIDTH - settingsButton.getWidth() - 10, game.HEIGHT - settingsButton.getHeight() - 10);

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        return settingsButton;
    }

    public Label createPlayerNameLabel() {
        Label.LabelStyle playerNameLabelStyle = new Label.LabelStyle();
        playerNameLabelStyle.font = createCustomFont(72);
        playerNameLabelStyle.fontColor = Color.BLACK;

        String username = "";
        username = game.getPrefs().getString("UserName");
        Account.setName(username);


        Label playerNameLabel = new Label(username, playerNameLabelStyle);
        playerNameLabel.setPosition(
                10,
                game.HEIGHT - playerNameLabel.getHeight() - 10
        );

        playerNameLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });


        return playerNameLabel;
    }

    public Button createCoinsButton() {
        Button.ButtonStyle coinsButtonStyle = new Button.ButtonStyle();
        coinsButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/coin_button.png")));

        Button coinsButton = new Button(coinsButtonStyle);
        coinsButton.setSize(100, 100);
        coinsButton.setPosition(
                coinsLabel.getX() + coinsLabel.getWidth() + 10,
                game.HEIGHT - coinsButton.getHeight() - 10);

        return coinsButton;
    }

    public Label createCoinsLabel() {
        Label.LabelStyle coinsLabelStyle = new Label.LabelStyle();
        coinsLabelStyle.font = createCustomFont(72);
        coinsLabelStyle.fontColor = Color.BLACK;

        coinsLabel = new Label(Account.getCoins() + "", coinsLabelStyle);
        coinsLabel.setPosition(
                settingsButton.getX() - coinsLabel.getWidth() - 400,
                game.HEIGHT - coinsLabel.getHeight() - 10
        );

        return coinsLabel;
    }

    public Button createLeaderboardButton() {
        Button.ButtonStyle leaderboardButtonStyle = new Button.ButtonStyle();
        leaderboardButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/trophy_button.png")));

        leaderboardButton = new Button(leaderboardButtonStyle);
        leaderboardButton.setSize(100, 100);
        leaderboardButton.setPosition(game.WIDTH - leaderboardButton.getWidth() - settingsButton.getWidth() - 100, game.HEIGHT - leaderboardButton.getHeight() - 10);

        leaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
                game.setScreen(new LeaderboardScreen(game));

            }
        });

        return leaderboardButton;
    }

    public static BitmapFont createCustomFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/04B_30__.TTF"));
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
}

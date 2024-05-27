package com.mygdx.game.Screens.Play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Account;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Main.MainScreen;
import com.mygdx.game.Screens.Splash.SplashScreen;
import com.mygdx.game.Sprites.Bird;
import com.mygdx.game.Sprites.Coin;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Utils.MapAdapter;

import java.util.ArrayList;
import java.util.Random;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;

public class PlayScreen implements Screen {

    private final Main game;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;
    private MapAdapter mapAdapter;
    private final float transitionTime = 5f;
    private float timeLeft = transitionTime;
    private BitmapFont font;
    private Music backgroundMusic;
    private Music collectCoinsMusic;
    private ArrayList<Coin> coins;
    private TiledMapTileLayer groundLayer;
    private ArrayList<Bird> birds;
    private ImageButton forwardButton;
    private ImageButton backwardButton;
    private ImageButton jumpButton;
    private ImageButton pauseButton;
    private Stage stage;
    private boolean pauseDialogIsShown = false;
    private int playerMeters = 0;
    private int playerCoins = Account.getCoins();
    private Table heartsTable;
    private float damageCooldown = 0;
    private final float DAMAGE_COOLDOWN_TIME = 1.5f;

    public PlayScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        map = new TmxMapLoader().load("tilemap/untitled.tmx");
        mapAdapter = new MapAdapter(map);

        renderer = new OrthogonalTiledMapRenderer(map, 1);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        Texture idleSpriteSheet = Main.getCurrentIdleTexture();

        groundLayer = (TiledMapTileLayer) map.getLayers().get("ground");

        player = new Player(idleSpriteSheet, groundLayer, mapAdapter);
        player.setPosition(2 * player.getGroundLayer().getTileWidth(), 20 * player.getGroundLayer().getTileHeight());

        coins = generateCoins();
        birds = generateBirds();

        font = MainScreen.createCustomFont(72);

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/countdown-music.ogg"));
        backgroundMusic.setLooping(false);
        backgroundMusic.setOnCompletionListener(music -> playBackgroundMusic());
        backgroundMusic.setVolume(game.getVolume());
        backgroundMusic.play();

        collectCoinsMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/collect-coins.mp3"));
        collectCoinsMusic.setVolume(game.getVolume());

        forwardButton = createButton("assets/Images/forward_button.png", 300, 50);
        backwardButton = createButton("assets/Images/backwards_button.png", 100, 50);
        jumpButton = createButton("assets/Images/jump_button.png", game.WIDTH - 300, 50);
        pauseButton = createButton("assets/Images/pause_button.png", game.WIDTH - 300, game.HEIGHT - 175);

        heartsTable = createHearts();

        stage = new Stage();
        stage.addActor(forwardButton);
        stage.addActor(backwardButton);
        stage.addActor(jumpButton);
        stage.addActor(pauseButton);
        stage.addActor(heartsTable);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        update(delta);
        updateCamera();

        renderer.setView(camera);
        renderer.render();

        stage.act();
        stage.draw();

        if (player.isDead() || player.getHearts() == 0) {
            handlePlayerDeath();
            return;
        }

        renderer.getBatch().begin();
        player.draw(renderer.getBatch());
        drawPlayerInfo();
        drawCoinsAndBirds();

        if (!player.isTransitionComplete()) {
            setTransition(delta);
        }

        renderer.getBatch().end();
    }

    private void update(float delta) {

        if (pauseDialogIsShown) return;

        if (jumpButton.isPressed() && player.isCanJump() && !player.isJumping()) player.jump();
        if (pauseButton.isPressed()) showPauseDialog();

        if (!player.isTransitionComplete()) return;

        player.setCurrentState(Player.State.RUNNING);
        if (forwardButton.isPressed()) {
            player.setRunningDirection(Player.RunningDirection.RIGHT);
        } else if (backwardButton.isPressed()) {
            player.setRunningDirection(Player.RunningDirection.LEFT);
        } else {
            player.setSpeed(0);
            player.setCurrentState(Player.State.STANDING);
        }

        checkCollisions();
        if (damageCooldown > 0) {
            damageCooldown -= delta;
        }
    }

    private void updateCamera() {
        if (player.getX() > camera.viewportWidth / 2 && camera.position.x + camera.viewportWidth / 2 < mapAdapter.getMapWidthInPixels())
            camera.position.set(player.getX(), camera.viewportHeight / 2, 0);
        camera.update();
    }

    private void setTransition(float delta) {

        timeLeft -= delta;
        String timeLeftText = (int) timeLeft + "";

        BitmapFont.BitmapFontData fontData = font.getData();
        float textWidth = fontData.scaleX * fontData.spaceXadvance * timeLeftText.length();

        float x = (game.WIDTH - textWidth) / 2;
        switch ((int) timeLeft) {
            case 5:
                font.setColor(Color.RED);
            case 4:
                font.setColor(Color.RED);
            case 3:
                font.setColor(Color.RED);
                break;
            case 2:
                font.setColor(Color.YELLOW);
                break;

            case 1:
                font.setColor(Color.GREEN);
                break;


        }

        if (timeLeft < 1) {
            player.transitionToRunningState();
            return;
        }

        font.draw(renderer.getBatch(), timeLeftText, x, game.HEIGHT - 200);
    }

    // Draw game information (meters, coins)
    private void drawPlayerInfo() {
        // Draw meters and coins collected
        float textX = camera.position.x - camera.viewportWidth / 2 + 10;
        float textY = camera.position.y + camera.viewportHeight / 2 - 10;

        font.setColor(Color.WHITE);
        playerMeters = player.getTileX() - 2;
        font.draw(renderer.getBatch(), playerMeters + "m", textX, textY);

        playerCoins = Account.getCoins();

        font.setColor(Color.GOLD);
        font.draw(renderer.getBatch(), playerCoins + " coins", textX, textY - 100);
    }

    private Table createHearts() {
        Texture heartTexture = new Texture(Gdx.files.internal("assets/Images/heart.png"));
        Table heartsTable = new Table();

        for (int i = 0; i < player.getHearts(); i++) {
            Image heartImage = new Image(heartTexture);
            heartImage.setSize(150, 150);
            heartsTable.add(heartImage).pad(2);
        }
        heartsTable.setSize(player.getHearts() * 150, 150);
        heartsTable.setPosition(Gdx.graphics.getWidth() / 2, game.HEIGHT - 150);
        return heartsTable;
    }

    private void playBackgroundMusic() {
        backgroundMusic.dispose();
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/background-music.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(game.getVolume());
        backgroundMusic.play();
    }

    // Draw coins and birds
    private void drawCoinsAndBirds() {
        // Draw coins
        for (Coin coin : coins) {
            if (isOnAir((int) coin.getX(), (int) coin.getY()))
                coin.draw(renderer.getBatch());
        }

        // Draw birds
        for (Bird bird : birds) {
            bird.draw(renderer.getBatch());
        }
    }

    private ArrayList<Coin> generateCoins() {
        ArrayList<Coin> c = new ArrayList<>();
        Texture spriteSheet = new Texture("assets/Images/coin.png");
        Random random = new Random();

        int mapWidthInTiles = mapAdapter.getMapWidthInTiles();
        int section = mapWidthInTiles / 100;
        int coinsPer100Meters = 2;
        int sectionWidth = 100;

        for (int i = 0; i < section; i++) {
            int startX = i * sectionWidth;
            for (int j = 0; j < coinsPer100Meters; j++) {
                int x = (random.nextInt(sectionWidth) + startX) * 32;
                int y = random.nextInt(Gdx.graphics.getHeight() - 100) + 175;
                c.add(new Coin(spriteSheet, x, y));
            }
        }

        return c;
    }

    private ArrayList<Bird> generateBirds() {
        ArrayList<Bird> birds = new ArrayList<>();
        Texture spriteSheet = new Texture("spritesheet/bird/Flying.png");
        Random random = new Random();

        int mapWidthInTiles = mapAdapter.getMapWidthInTiles();
        int section = mapWidthInTiles / 100;
        int coinsPer100Meters = 2;
        int sectionWidth = 100;

        for (int i = 0; i < section; i++) {
            int startX = i * sectionWidth;
            for (int j = 0; j < coinsPer100Meters; j++) {
                int x = (random.nextInt(sectionWidth) + startX) * mapAdapter.getTileWidth();
                int y = random.nextInt(Gdx.graphics.getHeight() - 100 - 50) + 50;
                birds.add(new Bird(spriteSheet, x, y));
            }
        }
        return birds;
    }

    private void checkCollisions() {
        checkCollisionWithCoins();
        if (damageCooldown <= 0) {
            checkCollisionWithBirds();
            checkCollisionWithSpikes();
        }
    }

    private void checkCollisionWithCoins() {
        for (Coin c : coins) {
            if (player.getX() <= c.getX() + c.getWidth() && player.getX() >= c.getX() - c.getWidth() &&
                    player.getY() <= c.getY() + c.getHeight() && player.getY() >= c.getY() - c.getHeight()) {
                if (!c.isBeenTaken()) {
                    c.setBeenTaken();

                    collectCoinsMusic.play();
                }
            }
        }
    }

    private void checkCollisionWithBirds() {
        for (Bird b : birds) {
            if (player.getX() <= b.getX() + b.getWidth() && player.getX() >= b.getX() - b.getWidth() &&
                    player.getY() <= b.getY() + b.getHeight() && player.getY() >= b.getY() - b.getHeight()) {
                checkIfDiedOrRemoveHeart();
            }
        }
    }

    public void checkCollisionWithSpikes() {
        int spikeTile = mapAdapter.getSpikesTileValue(player.getTileX() + 1, player.getTileY() + 1);
        if (spikeTile != 0) checkIfDiedOrRemoveHeart();
    }

    private boolean isOnAir(int x, int y) {
        int groundTileValue = mapAdapter.getGroundTileValue((int) (x / groundLayer.getTileWidth()), (int) (y / groundLayer.getTileHeight()));
        return groundTileValue == 0;
    }

    private ImageButton createButton(String imageFile, float x, float y) {
        Texture buttonTexture = new Texture(Gdx.files.internal(imageFile));
        TextureRegion buttonRegion = new TextureRegion(buttonTexture);
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(buttonRegion);

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.imageUp = buttonDrawable;

        ImageButton button = new ImageButton(buttonStyle);
        button.setPosition(x, y);

        button.setSize(175, 175);

        return button;
    }

    private void showPauseDialog() {
        if (pauseDialogIsShown) return;
        pauseDialogIsShown = true;
        final GDXDialogs dManager = GDXDialogsSystem.install();
        final GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);
        bDialog.setTitle("Pause Dialog");
        bDialog.setMessage("You sure you want to exit?");
        bDialog.setClickListener(button -> {
            if (Account.getMeters() < playerMeters)
                Account.setMeters(playerMeters);
            if (button == 0) {
                player.setDied();
                game.setScreen(new SplashScreen(game));
            } else if (button == 2) {
                game.setScreen(new PlayScreen(game));
            }
            pauseDialogIsShown = false;
        });

        bDialog.addButton("Yes");
        bDialog.addButton("Resume");
        bDialog.addButton("Retry");

        bDialog.build().show();
    }

    private void checkIfDiedOrRemoveHeart() {
        if (damageCooldown <= 0) {
            if (player.getHearts() > 0) {
                player.removeHeart();
                damageCooldown = DAMAGE_COOLDOWN_TIME;
                heartsTable = createHearts();
                for (Actor actor : stage.getActors()) {
                    if (actor.getX() == Gdx.graphics.getWidth() / 2) actor.remove();
                }
                stage.addActor(heartsTable);
            } else {
                player.setDied();
            }
        }
    }


    private void handlePlayerDeath() {
        if (Account.getMeters() < playerMeters)
            Account.setMeters(playerMeters);

        if (!pauseDialogIsShown) {
            GDXDialogs dManager = GDXDialogsSystem.install();
            GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);
            bDialog.setTitle("Game Over");
            bDialog.setMessage("You died! Retry?");
            bDialog.addButton("Retry");
            bDialog.addButton("Quit");

            bDialog.setClickListener(button -> {
                if (button == 0) {
                    game.setScreen(new PlayScreen(game));
                } else if (button == 1) {
                    game.setScreen(new MainScreen(game));
                }
                pauseDialogIsShown = false;
            });

            bDialog.build().show();
            pauseDialogIsShown = true;
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
        map.dispose();
        renderer.dispose();
        backgroundMusic.dispose();
    }
}

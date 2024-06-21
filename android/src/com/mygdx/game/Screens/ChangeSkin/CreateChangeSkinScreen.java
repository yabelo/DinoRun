package com.mygdx.game.Screens.ChangeSkin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Account;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Splash.SplashScreen;
import com.mygdx.game.Sprites.Skin;

import java.util.HashMap;
import java.util.Map;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;

public class CreateChangeSkinScreen {

    private final Main game;
    private final Skin mort = new Skin("mort", new Image(new Texture("spritesheet/mort/Thumbnail.png")), 0);
    private final Skin doux = new Skin("doux", new Image(new Texture("spritesheet/doux/Thumbnail.png")), 250);
    private final Skin vita = new Skin("vita", new Image(new Texture("spritesheet/vita/Thumbnail.png")), 500);
    private final Skin tard = new Skin("tard", new Image(new Texture("spritesheet/tard/Thumbnail.png")), 1000);

    private Image mortImage;
    private final Integer mortX = 200;
    private Image douxImage;
    private final Integer douxX = 700;
    private Image vitaImage;
    private final Integer vitaX = 1200;
    private Image tardImage;
    private final Integer tardX = 1700;
    private final GDXDialogs dManager = GDXDialogsSystem.install();
    private Music purchaseMusic;
    private Label coinsLabel = null;


    public CreateChangeSkinScreen(Main game) {
        this.game = game;
        purchaseMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/Music/purchase.m4a"));
        purchaseMusic.setVolume(game.getVolume());
    }

    public Label createMortLabel(){
        Label.LabelStyle mortLabelStyle = new Label.LabelStyle();
        mortLabelStyle.font = createCustomFont(72);
        mortLabelStyle.fontColor = Color.BLACK;

        Label mortLabel = new Label(mort.getName() + "", mortLabelStyle);
        mortLabel.setWidth(50);
        mortLabel.setPosition(
                mortX,
                mortImage.getY() + mortImage.getHeight() * mortImage.getScaleY() + 50
        );

        return mortLabel;
    }

    public Image createMortImage(){
        mortImage = mort.getImage();
        mortImage.scaleBy(20);
        mortImage.setPosition(
                mortX - (mortImage.getWidth() * 5),
                (game.HEIGHT - mortImage.getHeight() * mortImage.getScaleY()) / 2
        );

        mortImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeSkinAndReturn("mort");
            }
        });

        return mortImage;
    }

    public Label createDouxLabel(){
        Label.LabelStyle douxLabelStyle = new Label.LabelStyle();
        douxLabelStyle.font = createCustomFont(72);
        douxLabelStyle.fontColor = Color.BLACK;

        Label douxLabel = new Label( doux.getName() + "", douxLabelStyle);
        douxLabel.setWidth(50);
        douxLabel.setPosition(
                douxX,
                douxImage.getY() + douxImage.getHeight() * douxImage.getScaleY() + 50
        );

        return douxLabel;
    }

    public Image createDouxImage(){
        douxImage = doux.getImage();
        douxImage.scaleBy(20);
        douxImage.setPosition(
                douxX - (douxImage.getWidth() * 5),
                (game.HEIGHT - douxImage.getHeight() * douxImage.getScaleY()) / 2
        );

        if(!Account.hasSkin("doux"))
            douxImage.setColor(Color.DARK_GRAY);

        douxImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!Account.hasSkin("doux")){
                    if(Account.getCoins() >= doux.getCost()){
                        showPurchaseDialog(doux);
                    }
                }
                else{
                    changeSkinAndReturn("doux");
                }
            }
        });

        return douxImage;
    }

    public Label createDouxCostLabel(){
        if(Account.hasSkin("doux"))
            return null;
        Label.LabelStyle douxCostLabelStyle = new Label.LabelStyle();
        douxCostLabelStyle.font = createCustomFont(50);
        douxCostLabelStyle.fontColor = Color.BLACK;

        Label douxCostLabel = new Label( doux.getCost() + "", douxCostLabelStyle);
        douxCostLabel.setPosition(
                douxX + douxCostLabel.getWidth() / 4,
                douxImage.getY() - 50
        );

        return douxCostLabel;
    }

    public Label createVitaLabel(){
        Label.LabelStyle vitaLabelStyle = new Label.LabelStyle();
        vitaLabelStyle.font = createCustomFont(72);
        vitaLabelStyle.fontColor = Color.BLACK;

        Label vitaLabel = new Label( vita.getName() + "", vitaLabelStyle);
        vitaLabel.setWidth(50);
        vitaLabel.setPosition(
                vitaX,
                vitaImage.getY() + vitaImage.getHeight() * vitaImage.getScaleY() + 50
        );

        return vitaLabel;
    }

    public Image createVitaImage(){
        vitaImage = vita.getImage();
        vitaImage.scaleBy(20);
        vitaImage.setPosition(
                vitaX - (vitaImage.getWidth() * 5),
                (game.HEIGHT - vitaImage.getHeight() * vitaImage.getScaleY()) / 2
        );

        if(!Account.hasSkin("vita"))
            vitaImage.setColor(Color.DARK_GRAY);

        vitaImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!Account.hasSkin("vita")){
                    if(Account.getCoins() >= vita.getCost()){
                        showPurchaseDialog(vita);
                    }
                }
                else{
                    changeSkinAndReturn("vita");
                }
            }
        });

        return vitaImage;
    }

    public Label createVitaCostLabel(){
        if(Account.hasSkin("vita"))
            return null;
        Label.LabelStyle vitaCostLabelStyle = new Label.LabelStyle();
        vitaCostLabelStyle.font = createCustomFont(50);
        vitaCostLabelStyle.fontColor = Color.BLACK;

        Label vitaCostLabel = new Label( vita.getCost() + "", vitaCostLabelStyle);
        vitaCostLabel.setPosition(
                vitaX + vitaCostLabel.getWidth() / 4,
                vitaImage.getY() - 50
        );

        return vitaCostLabel;
    }

    public Label createTardLabel(){
        Label.LabelStyle tardLabelStyle = new Label.LabelStyle();
        tardLabelStyle.font = createCustomFont(72);
        tardLabelStyle.fontColor = Color.BLACK;

        Label tardLabel = new Label( tard.getName() + "", tardLabelStyle);
        tardLabel.setWidth(50);
        tardLabel.setPosition(
                tardX,
                tardImage.getY() + tardImage.getHeight() * tardImage.getScaleY() + 50
        );

        return tardLabel;
    }

    public Image createTardImage(){
        tardImage = tard.getImage();
        tardImage.scaleBy(20);
        tardImage.setPosition(
                tardX - (tardImage.getWidth() * 5),
                (game.HEIGHT - tardImage.getHeight() * tardImage.getScaleY()) / 2
        );

        if(!Account.hasSkin("tard"))
            tardImage.setColor(Color.DARK_GRAY);

        tardImage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!Account.hasSkin("tard")){
                    if(Account.getCoins() >= tard.getCost()){
                        showPurchaseDialog(tard);

                    }
                }
                else{
                    changeSkinAndReturn("tard");
                }
            }
        });

        return tardImage;
    }

    public Label createTardCostLabel(){
        if(Account.hasSkin("tard"))
            return null;
        Label.LabelStyle tardCostLabelStyle = new Label.LabelStyle();
        tardCostLabelStyle.font = createCustomFont(50);
        tardCostLabelStyle.fontColor = Color.BLACK;

        Label tardCostLabel = new Label( tard.getCost() + "", tardCostLabelStyle);
        tardCostLabel.setPosition(
                tardX + tardCostLabel.getWidth() / 4,
                tardImage.getY() - 50
        );

        return tardCostLabel;
    }

    public Button createBackButton(){
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/back_button.png")));

        Button backButton = new Button(backButtonStyle);
        backButton.setSize(100, 100);
        backButton.setPosition(10, game.HEIGHT - backButton.getHeight() - 10);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SplashScreen(game));
            }
        });

        return backButton;
    }

    private void updateUserSkins(String skinToAdd){
        Map<String, Boolean> userSkins = new HashMap<>();
        if(Account.hasSkin("mort"))
            userSkins.put("mort", true);
        else
            userSkins.put("mort", false);

        if(Account.hasSkin("doux"))
            userSkins.put("doux", true);
        else
            userSkins.put("doux", false);

        if(Account.hasSkin("vita"))
            userSkins.put("vita", true);
        else
            userSkins.put("vita", false);

        if(Account.hasSkin("tard"))
            userSkins.put("tard", true);
        else
            userSkins.put("tard", false);

        userSkins.put(skinToAdd, true);

        Account.setSkins(userSkins);
    }

    private void changeSkinAndReturn(String skin){
        game.getPrefs().putString("UserCurrentSkin", skin);
        game.getPrefs().flush();
        game.setScreen(new SplashScreen(game));
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
                1684,
                game.HEIGHT - coinsLabel.getHeight() - 10
        );

        return coinsLabel;
    }

    public static BitmapFont createCustomFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/04B_30__.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    private void showPurchaseDialog(final Skin skin) {
        final GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);
        bDialog.setTitle("Purchase a skin");
        bDialog.setMessage("You sure you want to buy " + skin.getName().toUpperCase() + " for " + skin.getCost() + " coins?");
        bDialog.setClickListener(button -> {
            if (button == 0) {
                purchaseMusic.play();
                updateUserSkins(skin.getName());
                Account.removeCoins(skin.getCost());
                changeSkinAndReturn(skin.getName());
            }
        });

        bDialog.addButton("Yes");
        bDialog.addButton("No");

        bDialog.build().show();
    }
}

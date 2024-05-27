package com.mygdx.game.Screens.Settings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Main;
import com.mygdx.game.Screens.Main.MainScreen;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;

public class CreateSettingsScreen {

    private final Main game;
    private Slider volumeSlider;
    private Button exitButton;
    private final GDXDialogs dManager = GDXDialogsSystem.install();

    public CreateSettingsScreen(Main game) {
        this.game = game;
    }

    public Button createExitButton() {
        Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/exit_button.png")));

        exitButton = new Button(exitButtonStyle);
        exitButton.setSize(100, 100);
        exitButton.setPosition(game.WIDTH - exitButton.getWidth() - 20, game.HEIGHT - exitButton.getHeight() - 10);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showExitDialog();
            }
        });

        return exitButton;
    }

    private void showExitDialog() {
        final GDXButtonDialog bDialog = dManager.newDialog(GDXButtonDialog.class);
        bDialog.setTitle("Exit Dialog");
        bDialog.setMessage("Are you sure you want to exit?");
        bDialog.setClickListener(button -> {
            if (button == 0) {
                System.exit(0);
            }
        });

        bDialog.addButton("Yes");
        bDialog.addButton("No");

        bDialog.build().show();
    }

    public Button createBackButton() {
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/back_button.png")));

        Button backButton = new Button(backButtonStyle);
        backButton.setSize(100, 100);
        backButton.setPosition(10, game.HEIGHT - backButton.getHeight() - 10);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainScreen(game));
            }
        });

        return backButton;
    }

    public Slider createVolumeSlider() {
        Texture sliderBackgroundTexture = new Texture("assets/Images/slider_background.png");
        Texture sliderKnobTexture = new Texture("assets/Images/slider_knob.png");

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new NinePatchDrawable(new NinePatch(sliderBackgroundTexture, 6, 6, 6, 6));
        sliderStyle.knob = new NinePatchDrawable(new NinePatch(sliderKnobTexture, 6, 6, 6, 6));

        sliderStyle.knob.setMinWidth(100);
        sliderStyle.knob.setMinHeight(100);

        volumeSlider = new Slider(0f, 1f, 0.01f, false, sliderStyle);
        volumeSlider.setValue(game.getVolume());

        volumeSlider.setWidth(sliderBackgroundTexture.getWidth());
        volumeSlider.setHeight(sliderBackgroundTexture.getHeight());

        volumeSlider.setPosition(
                game.WIDTH / 2f - volumeSlider.getWidth() / 2,
                game.HEIGHT / 2f - volumeSlider.getHeight() / 2
        );

        return volumeSlider;
    }

    public ImageButton createVolumeImageButton(final ImageButton.ImageButtonStyle volumeStyle) {

        final ImageButton volume = new ImageButton(volumeStyle);
        volume.setSize(100, 100);
        volume.setPosition(volumeSlider.getX() - volume.getWidth() - volume.getWidth() / 2, volumeSlider.getY() + volume.getHeight() * 2);

        volume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!volume.isChecked()) {
                    volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_on.png")));
                    volumeSlider.setValue(1f);
                } else {
                    volumeStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture("assets/Images/volume_off.png")));
                    volumeSlider.setValue(0f);
                }
            }
        });
        return volume;
    }
}

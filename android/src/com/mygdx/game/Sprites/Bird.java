package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bird extends Sprite {

    private final Vector2 velocity = new Vector2();
    private final Animation<TextureRegion> flyingAnimation;
    private float stateTime;


    public Bird(Texture spriteSheet, int x, int y){
        super(spriteSheet);

        setSize(getWidth() / 20, getHeight() / 5);
        setFlip(true, false);
        setX(x);
        setY(y);

        TextureRegion[][] spriteSheetAnimation = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight());
        flyingAnimation = new Animation<>(0.1f, spriteSheetAnimation[0]);

        stateTime = 0f;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = flyingAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());

    }

    private void update(float delta){
        velocity.x = -200;
        setX(getX() + velocity.x * delta);
    }
}

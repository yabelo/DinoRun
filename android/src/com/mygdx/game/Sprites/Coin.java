package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import com.mygdx.game.Account;

public class Coin extends Sprite {

    private boolean isBeenTaken;
    public Coin(Texture spriteSheet, int x, int y){
        super(spriteSheet);

        setX(x);
        setY(y);
        setSize(getWidth() / 10, getHeight() / 10);

    }

    @Override
    public void draw(Batch batch) {

        if(!isBeenTaken)
            batch.draw(getTexture(), getX(), getY(), getWidth(), getHeight());
    }

    public boolean isBeenTaken() {
        return isBeenTaken;
    }

    public void setBeenTaken() {
        isBeenTaken = true;
        Account.increaseCoins();
        setTexture(null);
    }
}

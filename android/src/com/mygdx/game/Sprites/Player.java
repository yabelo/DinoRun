package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Main;
import com.mygdx.game.Utils.MapAdapter;

public class Player extends Sprite {


    public enum State {STANDING, RUNNING, DEAD}
    private State currentState;
    public enum RunningDirection {RIGHT, LEFT}
    private RunningDirection runningDirection;
    private Vector2 velocity = new Vector2();
    private float speed = 350;
    private float gravity = 16;
    private float jumpDuration = 0.2f;
    private TiledMapTileLayer groundLayer;
    private final MapAdapter mapAdapter;
    private final int tileWidth;
    private final int tileHeight;
    private boolean jumping = false;
    private boolean canJump = true;
    private float jumpTimer = 0.0f;
    private boolean transitionComplete = false;
    private final Animation<TextureRegion> standAnimation;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime;
    private int hearts = 3;


    public Player(Texture spriteSheet, TiledMapTileLayer groundLayer, MapAdapter mapAdapter) {
        super(spriteSheet);
        this.groundLayer = groundLayer;
        this.mapAdapter = mapAdapter;
        tileWidth = groundLayer.getTileWidth();
        tileHeight = groundLayer.getTileHeight();

        setSize(tileWidth * 3, tileHeight * 3);

        TextureRegion[][] framesStand = TextureRegion.split(spriteSheet, 53 / 3, 19);
        standAnimation = new Animation<>(0.1f, framesStand[0]);

        stateTime = 0f;
        currentState = State.STANDING;
        runningDirection = RunningDirection.RIGHT;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        stateTime += Gdx.graphics.getDeltaTime();

        switch (currentState) {
            case STANDING:
                TextureRegion currentFrame = standAnimation.getKeyFrame(stateTime, true);
                batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
                break;
            case RUNNING:
                TextureRegion currentFrameRun = walkAnimation.getKeyFrame(stateTime, true);
                batch.draw(currentFrameRun, getX(), getY(), getWidth(), getHeight());
                break;
            case DEAD:
                break;
        }
    }

    public void jump() {
        jumping = true;
        canJump = false;
    }

    private float calculateJumpVelocity() {
        // Adjust the jump velocity based on the desired jump height and gravity
        float jumpHeight = 100; // Adjust this value as needed
        return (float) Math.sqrt(2 * gravity * jumpHeight);
    }

    private void isJumping(float delta) {
        if (jumping) {
            velocity.y = (float) (calculateJumpVelocity() / delta / 12.5);
            jumpTimer += delta;
            if (jumpTimer >= jumpDuration) {
                jumping = false;
            }
        }
    }

    private void applyGravity() {
        velocity.y -= gravity;
    }

    public void update(float delta) {
        applyGravity();
        isJumping(delta);

        velocity.x = speed;

        float oldX = getX(), oldY = getY();
        int collisionX;
        if(getRunningDirection().equals(RunningDirection.RIGHT))
            collisionX = collidesRight(oldX, oldY);
        else
            collisionX = collidesLeft(oldX, oldY);
        int collisionY;

        if (collisionX != 0) {
            velocity.x = 0;
        }

        if (velocity.y < 0) {
            collisionY = collidesBottom(oldX, oldY);
            if (collisionY != 0) {
                velocity.y = 0;
                jumpTimer = 0.0f;
                canJump = true;
            } else {
                canJump = false;
            }
        } else if (velocity.y > 0) {
            collisionY = collidesTop(oldX, oldY);
            if (collisionY != 0) {
                setY(oldY);
                velocity.y = 0;
            }
        }

        if (currentState == State.RUNNING){
            if(getTileX() <= 0) velocity.x = 0;
            setX(getX() + velocity.x * delta);
        }

        setY(getY() + velocity.y * delta);

        if (getY() < 0) {
            setDied();
        }
    }

    public int collidesTop(float oldX, float oldY) {
        int leftTile = (int) (oldX / tileWidth);
        int rightTile = (int) ((oldX + getWidth()) / tileWidth) - 1;
        int topTile = (int) ((oldY + getHeight()) / tileHeight) - 1; // Adjusted for player's increased height

        for (int x = leftTile; x <= rightTile; x++) {
            int tileValue = mapAdapter.getGroundTileValue(x, topTile + 1);
            if (tileValue != 0) {
                return tileValue;
            }
        }

        return 0;
    }

    public int collidesRight(float oldX, float oldY) {
        int rightTile = (int) ((oldX + getWidth()) / tileWidth);
        int bottomTile = (int) (oldY / tileHeight);
        int topTile = (int) ((oldY + getHeight()) / tileHeight) - 1; // Adjusted for player's increased height

        for (int y = bottomTile; y <= topTile; y++) {
            int tileValue = mapAdapter.getGroundTileValue(rightTile, y + 1);
            if (tileValue != 0) {
                return tileValue;
            }
        }

        return 0;
    }

    public int collidesLeft(float oldX, float oldY) {
        int leftTile = (int) (oldX / tileWidth) - 1;
        int bottomTile = (int) (oldY / tileHeight);
        int topTile = (int) ((oldY + getHeight()) / tileHeight) - 1;

        for (int y = bottomTile; y <= topTile; y++) {
            int tileValue = mapAdapter.getGroundTileValue(leftTile, y + 1);
            if (tileValue != 0) {
                return tileValue;
            }
        }

        return 0;
    }

    public int collidesBottom(float oldX, float oldY) {
        int leftTile = (int) (oldX / tileWidth);
        int rightTile = (int) ((oldX + getWidth()) / tileWidth);
        int bottomTile = (int) (oldY / tileHeight);
        int topTile = (int) ((oldY + getHeight()) / tileHeight) - 1; // Adjusted for player's increased height

        for (int x = leftTile; x < rightTile; x++) {
            int tileValue = mapAdapter.getGroundTileValue(x, bottomTile);
            if (tileValue != 0) {
                return tileValue;
            }
        }

        return 0;
    }

    public void setRunningDirection(RunningDirection runningDirection){
        this.runningDirection = runningDirection;
        if(runningDirection.equals(RunningDirection.RIGHT)){
            setSpeed(350);
        }
        else{
            setSpeed(-350);
        }
    }

    public RunningDirection getRunningDirection() {
        return runningDirection;
    }

    public int getTileX() {
        return (int) (getX() / tileWidth);
    }

    public int getTileY() {
        return (int) (getY() / tileHeight);
    }

    public void transitionToRunningState() {
        Texture runSpriteSheet = Main.getCurrentRunTexture();
        TextureRegion[][] framesRun = TextureRegion.split(runSpriteSheet, 104 / 6, 19);
        walkAnimation = new Animation<>(0.1f, framesRun[0]);
        currentState = State.RUNNING;
        transitionComplete = true;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public void setGroundLayer(TiledMapTileLayer groundLayer) {
        this.groundLayer = groundLayer;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean isJumping() {
        return jumping;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public float getTransitionTime() {
        // Time before transitioning to the running state
        float transitionTime = 3f;
        return transitionTime;
    }

    public boolean isTransitionComplete() {
        return transitionComplete;
    }

    public void setDied() {
        setCurrentState(State.DEAD);
    }

    public boolean isDead() {
        return currentState == State.DEAD;
    }

    public int getHearts() {
        return hearts;
    }

    public void removeHeart() {
        hearts--;
    }
}
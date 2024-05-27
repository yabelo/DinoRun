package com.mygdx.game.Sprites;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Skin {

    private String name;
    private Image image;
    private Integer cost;

    public Skin(String name, Image image, Integer cost) {
        this.name = name;
        this.image = image;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}

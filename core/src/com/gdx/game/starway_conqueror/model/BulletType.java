package com.gdx.game.starway_conqueror.model;

public enum BulletType {

    FIREBALL(0), GREENRAY(1);

    private int index;

    public int getIndex() {
        return index;
    }

    BulletType(int index) {
        this.index = index;
    }

}

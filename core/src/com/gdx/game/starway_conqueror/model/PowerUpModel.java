package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PowerUpModel {

    public enum Type {

        MONEY10(0), MONEY25(1), MONEY50(2), MEDKIT(3);

        private int number;

        public int getNumber() {
            return number;
        }

        Type(int number) {
            this.number = number;
        }

    }

    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float time;
    private float maxTime;
    private Type type;

    public Type getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public PowerUpModel() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        this.time = 0.0f;
        this.maxTime = 7.0f;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if(time > maxTime) {
            deactivate();
        }
    }

    public void activate(float x, float y, Type type) {
        this.position.set(x, y);
        this.velocity.set(MathUtils.random(-50.0f, 50.0f), MathUtils.random(-50.0f, 50.0f));
        this.type = type;
        this.time = 0.0f;
        this.maxTime = 7.0f;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void use(PlayerModel hero) {
        switch (type) {
            case MONEY10:
                hero.addMoney(10);
                hero.addScore(100);
                break;
            case MONEY25:
                hero.addMoney(25);
                hero.addScore(250);
                break;
            case MONEY50:
                hero.addMoney(50);
                hero.addScore(500);
                break;
            case MEDKIT:
                hero.fullRepair();
                break;
        }
    }

}

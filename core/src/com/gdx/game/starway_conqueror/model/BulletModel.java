package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.component.Poolable;
import com.gdx.game.starway_conqueror.application.ApplicationController;

public class BulletModel implements Poolable {
    private boolean isPlayerBullet;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private BulletType type;

    public boolean isPlayerBullet() {
        return isPlayerBullet;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public BulletType getType() {
        return type;
    }

    public BulletModel() {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.active = false;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate(BulletType type, boolean isPlayersBullet, float x, float y, float vx, float vy) {
        this.type = type;
        this.isPlayerBullet = isPlayersBullet;
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

        if (position.x > ApplicationController.SCREEN_WIDTH
            || position.x < 0
            || position.y > ApplicationController.SCREEN_HEIGHT
            || position.y < 0
        ) {
            deactivate();
        }
    }
}

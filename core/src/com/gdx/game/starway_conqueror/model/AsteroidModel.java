package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.component.Poolable;

public class AsteroidModel extends AbstractSpaceObjectModel implements Poolable {
    private float scale;
    private float angle;
    private float angularSpeed;

    private float textureRegionWidth;
    private float textureRegionHeight;

    public AsteroidModel(TextureRegion texture) {
        this.texture = texture;

        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        scale = 0;
        angle = 0;
        angularSpeed = 0;
        hpMax = 0;
        hp = 0;
        textureRegionWidth = (float) texture.getRegionWidth();
        textureRegionHeight = (float) texture.getRegionHeight();
        hitArea = new Circle(position.x, position.y,  (textureRegionWidth / 2 - 16) * scale);
        damageReaction = 0.0f;
        active = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (damageReaction > 0.01f) {
            batch.setColor(1.0f, 1.0f - damageReaction, 1.0f - damageReaction, 1.0f);
        }

        batch.draw(
            texture,
            position.x - textureRegionWidth / 2,
            position.y - textureRegionHeight / 2,
            textureRegionWidth / 2,
            textureRegionHeight / 2,
            textureRegionWidth,
            textureRegionHeight,
            scale,
            scale,
            angle
        );

        if (damageReaction > 0.01f) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += angularSpeed * dt;
        if (position.x < -100.0f) {
            deactivate();
        }
        hitArea.setPosition(position);
        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;
    }

    @Override
    public void onDestroy() {
        deactivate();
    }

    public void activate(float x, float y, float vx, float vy, int hpMax, float r) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.active = true;
        this.scale = r;
        this.hitArea.radius = (textureRegionWidth / 2 - 16) * this.scale;
        this.damageReaction = 0.0f;
    }
}

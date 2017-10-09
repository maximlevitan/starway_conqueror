package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.component.Poolable;
import com.gdx.game.starway_conqueror.component.Route;
import com.gdx.game.starway_conqueror.view.GameScreen;

public class BotModel extends ShipModel implements Poolable {
    private TextureRegion[] frames;
    private int maxFrames;
    private float time;
    private float timePerFrame;
    private Route route;

    private float textureRegionWidth;
    private float textureRegionHeight;
    private float scale;

    public BotModel(GameScreen game, TextureRegion texture) {
        this.game = game;
        this.texture = texture;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.enginePower = 0;
        this.currentFire = 0.0f;
        this.fireRate = 0;
        this.hpMax = 0;
        this.hp = this.hpMax;
        this.textureRegionWidth = (float) texture.getRegionWidth();
        this.textureRegionHeight = (float) texture.getRegionHeight();
        this.scale = 0.3f;
        this.hitArea = new Circle(position, (textureRegionWidth / 2 - 16) * scale);
        this.weaponDirection = new Vector2(-1.0f, 0.0f);
        this.isPlayer = false;
        this.maxFrames = 1;
        this.timePerFrame = 0.1f;
        this.time = MathUtils.random(0.0f, maxFrames * timePerFrame);
        this.frames = new TextureRegion[maxFrames];
        for (int i = 0; i < maxFrames; i++) {
            frames[i] = new TextureRegion(texture, i * (int) textureRegionWidth, 0, (int) textureRegionWidth, (int) textureRegionHeight);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (damageReaction > 0.01f) {
            batch.setColor(1.0f, 1.0f - damageReaction, 1.0f - damageReaction, 1.0f);
        }
        int k = (int) (time / timePerFrame);
        if (k > maxFrames - 1) k = maxFrames - 1;

        batch.draw(
            frames[k],
            position.x - textureRegionWidth / 2,
            position.y - textureRegionHeight / 2,
            textureRegionWidth / 2,
            textureRegionHeight / 2,
            textureRegionWidth,
            textureRegionHeight,
            0.3f,
            0.3f,
            0
        );

        if (damageReaction > 0.01f) {
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public void update(float dt) {
        time += dt;
        if (time > maxFrames * timePerFrame) {
            time = 0.0f;
        }
        pressFire(dt);
        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;
        if (position.x < -100) {
            deactivate();
        }
        //velocity.set(-120, 0);
        velocity.set(route.getMyVelocity((int)position.x));
        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        velocity.scl(0.95f);
    }

    @Override
    public void onDestroy() {
        deactivate();
    }

    public void activate(Route route) {
        position.set(route.getInitialPosition());
        fireRate = 1.0f;
        hitArea.setRadius((textureRegionWidth / 2 - 16) * scale);
        hpMax = 15;
        active = true;
        hp = hpMax;
        this.route = route;
    }
}

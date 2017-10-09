package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gdx.game.starway_conqueror.application.ApplicationController;
import com.gdx.game.starway_conqueror.view.GameScreen;

public class PlayerModel extends ShipModel {
    private TextureRegion redHpRegion;
    private TextureRegion greenHpRegion;
    private int lives;

    private JoystickModel joystickModel;

    private int score;
    private int money;

    private Sound fireSound;

    // private float time;

    private StringBuilder hudStringHelper;

    private float textureRegionWidth;
    private float textureRegionHeight;
    private float scale;

    public int getLives() {
        return lives;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public PlayerModel(GameScreen game, TextureRegion texture, TextureRegion textureHP, TextureRegion textureJoystick, Sound fireSound, Vector2 position, Vector2 velocity, float engine) {
        this.game = game;
        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.enginePower = engine;
        this.currentFire = 0.0f;
        this.fireRate = 0.1f;
        this.hpMax = 40;
        this.hp = this.hpMax;
        this.lives = 3;
        this.redHpRegion = new TextureRegion(textureHP, 0, 32, 224, 32);
        this.greenHpRegion = new TextureRegion(textureHP, 0, 0, 224, 32);
        this.textureRegionWidth = (float) texture.getRegionWidth();
        this.textureRegionHeight = (float) texture.getRegionHeight();
        this.scale = 0.3f;
        this.hitArea = new Circle(position, (textureRegionWidth / 2 - 16) * scale);
        this.hudStringHelper = new StringBuilder(50);
        this.score = 0;
        this.money = 0;
        this.weaponDirection = new Vector2(1.0f, 0.0f);
        this.isPlayer = true;
        this.joystickModel = new JoystickModel(this, textureJoystick);
        this.fireSound = fireSound;
        // this.time = 0.0f;
    }

    @Override
    public void render(SpriteBatch batch) {
        float textureHeight = (float) texture.getRegionHeight();
        float textureWidth = (float) texture.getRegionWidth();

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
            velocity.y / 30.0f
        );
    }

    public void renderHUD(SpriteBatch batch, BitmapFont fnt, float x, float y) {
        // batch.draw(redHpRegion, x, y, 224, 32);
        // batch.draw(greenHpRegion, x, y, 224 * ((float)hp / (float)hpMax), 32);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(redHpRegion, x + (int) (Math.random() * damageReaction * 10), y + (int) (Math.random() * damageReaction * 10));
        batch.draw(greenHpRegion, x + (int) (Math.random() * damageReaction * 10), y + (int) (Math.random() * damageReaction * 10), (int) ((float) hp / hpMax * 224), 32);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1, 1, 0, damageReaction);
        batch.draw(redHpRegion, x + (int) (Math.random() * damageReaction * 25), y + (int) (Math.random() * damageReaction * 25));
        batch.draw(greenHpRegion, x + (int) (Math.random() * damageReaction * 25), y + (int) (Math.random() * damageReaction * 25), (int) ((float) hp / hpMax * 224), 32);
        batch.setColor(1, 1, 1, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hudStringHelper.setLength(0);
        hudStringHelper.append("x").append(lives);
        fnt.draw(batch, hudStringHelper, x + 224, y + 22);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Score: ").append(score);
        fnt.draw(batch, hudStringHelper, x + 4, y - 4);
        joystickModel.render(batch);
    }

    public void fullRepair() {
        this.hp = this.hpMax;
    }

    @Override
    public void update(float dt) {
        joystickModel.update(dt);
        // time += dt;
        // for (int i = 0; i < 10; i++) {
        //     game.getParticleEmitter().setup(position.x + 32 * (float) Math.cos(time * 5 + i * 0.628f), position.y + 32 * (float) Math.sin(time * 5 + i * 0.628f), MathUtils.random(-10, 10), MathUtils.random(-10, 10), 0.2f, 0.8f, 1.1f, 0, 0, 1, 1, 1, 1, 1, 1);
        // }

        if (joystickModel.getPower() > 0.02f) {
            velocity.x += enginePower * dt * joystickModel.getNormal().x * joystickModel.getPower();
            velocity.y += enginePower * dt * joystickModel.getNormal().y * joystickModel.getPower();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x += enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x -= enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y -= enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            pressFire(dt);
        }
        damageReaction -= dt * 2.0f;
        if (damageReaction < 0.0f) damageReaction = 0.0f;

        float scaledHalfShipWidth = textureRegionWidth / 2 * scale;
        float scaledHalfShipHeight = textureRegionHeight / 2 * scale;

        if (position.x < scaledHalfShipWidth) {
            position.x = scaledHalfShipWidth;
            if (velocity.x < 0) velocity.x = 0;
        }
        if (position.x > ApplicationController.SCREEN_WIDTH - scaledHalfShipWidth) {
            position.x = ApplicationController.SCREEN_WIDTH - scaledHalfShipWidth;
            if (velocity.x > 0) velocity.x = 0;
        }
        if (position.y < scaledHalfShipHeight) {
            position.y = scaledHalfShipHeight;
            if (velocity.y < 0) velocity.y = 0;
        }
        if (position.y > ApplicationController.SCREEN_HEIGHT - scaledHalfShipHeight) {
            position.y = ApplicationController.SCREEN_HEIGHT - scaledHalfShipHeight;
            if (velocity.y > 0) velocity.y = 0;
        }

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);
        velocity.scl(0.95f);
        if (velocity.x > 20.0f) {
            float size = velocity.len() / 120.0f * 1.5f;
            float delta = scaledHalfShipWidth;
            game.getParticleEmitter().setup(position.x - delta, position.y, -MathUtils.random(5.0f, 20.0f), MathUtils.random(-4.0f, 4.0f), 0.5f, size, 0.6f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        }
    }

    @Override
    public void onDestroy() {
        hp = hpMax;
        lives--;
        damageReaction = 0.0f;
    }

    @Override
    public void fire() {
        super.fire();
        fireSound.play();
    }

    public void dispose() {
        fireSound.dispose();
    }
}

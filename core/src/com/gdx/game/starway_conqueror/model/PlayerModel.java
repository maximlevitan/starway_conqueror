package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.StringBuilder;
import com.gdx.game.starway_conqueror.application.ApplicationController;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
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

    public PlayerModel(GameScreen game, Vector2 position, Vector2 velocity, float engine) {
        this.game = game;
        this.position = position;
        this.velocity = velocity;
        this.enginePower = engine;

        ResourceManager resourceManager = ResourceManager.getInstance();

        texture = resourceManager.getAtlasRegion("my2.pack", "ship256");
        currentFire = 0.0f;
        fireRate = 0.1f;
        hpMax = 40;
        hp = this.hpMax;
        lives = 3;
        textureRegionWidth = (float) texture.getRegionWidth();
        textureRegionHeight = (float) texture.getRegionHeight();
        scale = 0.3f;
        hitArea = new Circle(position, (textureRegionWidth / 2 - 16) * scale);
        hudStringHelper = new StringBuilder(50);
        score = 0;
        money = 0;
        weaponDirection = new Vector2(1.0f, 0.0f);
        isPlayer = true;
        // time = 0.0f;

        fireSound = resourceManager.getSound("laser.wav");

        redHpRegion = new TextureRegion(
            resourceManager.getAtlasRegion("my2.pack", "hpBar"),
            0,
            32,
            224,
            32
        );

        greenHpRegion = new TextureRegion(
            resourceManager.getAtlasRegion("my2.pack", "hpBar"),
            0,
            0,
            224,
            32
        );

        joystickModel = new JoystickModel(
            this,
            resourceManager.getAtlasRegion("my2.pack", "joystick")
        );
    }

    @Override
    public void render(SpriteBatch batch) {
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

    public void renderHUD(SpriteBatch batch, float x, float y) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(
            redHpRegion,
            x + (int) (Math.random() * damageReaction * 10),
            y + (int) (Math.random() * damageReaction * 10)
        );
        batch.draw(
            greenHpRegion,
            x + (int) (Math.random() * damageReaction * 10),
            y + (int) (Math.random() * damageReaction * 10),
            (int) ((float) hp / hpMax * 224),
            32
        );
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1, 1, 0, damageReaction);
        batch.draw(
            redHpRegion,
            x + (int) (Math.random() * damageReaction * 25),
            y + (int) (Math.random() * damageReaction * 25)
        );
        batch.draw(
            greenHpRegion,
            x + (int) (Math.random() * damageReaction * 25),
            y + (int) (Math.random() * damageReaction * 25),
            (int) ((float) hp / hpMax * 224),
            32
        );
        batch.setColor(1, 1, 1, 1);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        hudStringHelper.setLength(0);
        hudStringHelper.append("x").append(lives);
        ResourceManager.getInstance().getFont("font2.fnt")
            .draw(batch, hudStringHelper, x + 224, y + 22);
        hudStringHelper.setLength(0);
        hudStringHelper.append("Score: ").append(score);
        ResourceManager.getInstance().getFont("font2.fnt")
            .draw(batch, hudStringHelper, x + 4, y - 4);
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
        //     game.getParticleEmitter().setup(
        //         position.x + 32 * (float) Math.cos(time * 5 + i * 0.628f),
        //         position.y + 32 * (float) Math.sin(time * 5 + i * 0.628f),
        //         MathUtils.random(-10, 10),
        //         MathUtils.random(-10, 10),
        //         0.2f,
        //         0.8f,
        //         1.1f,
        //         0,
        //         0,
        //         1,
        //         1,
        //         1,
        //         1,
        //         1,
        //         1
        //     );
        // }

        if (joystickModel.getPower() > 0.02f) {
            velocity.x += enginePower * dt * joystickModel.getNorma().x * joystickModel.getPower();
            velocity.y += enginePower * dt * joystickModel.getNorma().y * joystickModel.getPower();
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

            game.getParticleEmitter().setup(
                position.x - delta,
                position.y,
                -MathUtils.random(5.0f, 20.0f),
                MathUtils.random(-4.0f, 4.0f),
                0.5f,
                size,
                0.6f,
                1.0f,
                1.0f,
                1.0f,
                1.0f,
                0.0f,
                0.0f,
                1.0f,
                0.0f
            );
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

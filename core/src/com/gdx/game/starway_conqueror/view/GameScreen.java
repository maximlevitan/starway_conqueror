package com.gdx.game.starway_conqueror.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.AsteroidModel;
import com.gdx.game.starway_conqueror.emitter.AsteroidEmitter;
import com.gdx.game.starway_conqueror.model.BackgroundModel;
import com.gdx.game.starway_conqueror.emitter.BoomEmitter;
import com.gdx.game.starway_conqueror.model.BotModel;
import com.gdx.game.starway_conqueror.emitter.BotEmitter;
import com.gdx.game.starway_conqueror.model.BulletModel;
import com.gdx.game.starway_conqueror.emitter.BulletEmitter;
import com.gdx.game.starway_conqueror.model.LevelInfoModel;
import com.gdx.game.starway_conqueror.emitter.ParticleEmitter;
import com.gdx.game.starway_conqueror.model.PlayerModel;
import com.gdx.game.starway_conqueror.model.PowerUpModel;
import com.gdx.game.starway_conqueror.emitter.PowerUpsEmitter;
import com.gdx.game.starway_conqueror.application.ApplicationController;

import java.util.List;

public class GameScreen implements Screen {

    private ApplicationController game;

    private SpriteBatch batch;
    private ResourceManager resourceManager;
    private BackgroundModel backgroundModel;
    private PlayerModel playerModel;
    private AsteroidEmitter asteroidEmitter;
    private BulletEmitter bulletEmitter;
    private PowerUpsEmitter powerUpsEmitter;
    private ParticleEmitter particleEmitter;
    private BoomEmitter boomEmitter;
    private BotEmitter botEmitter;
    private Music music;
    private BitmapFont font;
    private int level;
    private int maxLevels;
    private float timePerLevel;
    private float currentLevelTime;

    List<LevelInfoModel> levels;

    private final Vector2 collisionHelper = new Vector2(0, 0);

    public LevelInfoModel getCurrentLevelInfo() {
        return levels.get(level - 1);
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public int getLevel() {
        return level;
    }

    public void loadFullGameInfo() {
        levels = resourceManager.loadInternalFileData("leveldata.csv", LevelInfoModel.class);

        maxLevels = levels.size();
    }

    public GameScreen(ApplicationController game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.resourceManager = ResourceManager.getInstance();
    }

    @Override
    public void show() {
        ResourceManager.getInstance().loadScreenAssetsByType(ScreenType.GAME);

        loadFullGameInfo();
        level = 1;
        currentLevelTime = 0.0f;
        timePerLevel = 60.0f;

        float asteroidGenerationTime = getCurrentLevelInfo().getAsteroidGenerationTime();
        int startAsteroidCount = 5;

        float botGenerationTime = getCurrentLevelInfo().getBotGenerationTime();
        int startBotCount = 2;

        backgroundModel = new BackgroundModel();
        playerModel = new PlayerModel(this, new Vector2(100, 328), new Vector2(0.0f, 0.0f), 800.0f);
        asteroidEmitter = new AsteroidEmitter(this, startAsteroidCount, asteroidGenerationTime);
        bulletEmitter = new BulletEmitter(100);
        powerUpsEmitter = new PowerUpsEmitter();
        particleEmitter = new ParticleEmitter();
        boomEmitter = new BoomEmitter();
        botEmitter = new BotEmitter(this, startBotCount, botGenerationTime);

        font = resourceManager.getFont("font2.fnt");
        music = resourceManager.assetManager.get("music.mp3", Music.class);
        music.setLooping(true);

        resourceManager.getMusic("music.mp3").play();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Matrix4 m4 = new Matrix4();
        // m4.setToTranslationAndScaling(-1f, -1f, 0, 2.0f / 1280.0f, 2.0f / 720.0f, 0.0f);
        batch.setProjectionMatrix(game.getCamera().combined);
        // batch.setProjectionMatrix(m4);

        batch.begin();
        backgroundModel.render(batch);
        playerModel.render(batch);
        asteroidEmitter.render(batch);
        botEmitter.render(batch);
        bulletEmitter.render(batch);
        powerUpsEmitter.render(batch);
        boomEmitter.render(batch);
        particleEmitter.render(batch);
        playerModel.renderHUD(batch, 20, 668);
        font.draw(batch, "LEVEL: " + level, 600, 680);
        batch.end();
    }

    public void updateLevel(float dt) {
        currentLevelTime += dt;

        if (currentLevelTime > timePerLevel) {
            currentLevelTime = 0.0f;
            level++;

            if (level > maxLevels) level = maxLevels;

            asteroidEmitter.setGenerationTime(getCurrentLevelInfo().getAsteroidGenerationTime());
            botEmitter.setGenerationTime(getCurrentLevelInfo().getBotGenerationTime());
        }
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(game.getMenuScreen());
        }

        updateLevel(dt);

        backgroundModel.update(dt, playerModel.getVelocity());
        playerModel.update(dt);
        asteroidEmitter.update(dt);
        botEmitter.update(dt);
        bulletEmitter.update(dt);
        powerUpsEmitter.update(dt);
        particleEmitter.update(dt);

        checkCollision();

        boomEmitter.update(dt);

        asteroidEmitter.checkPool();
        botEmitter.checkPool();
        bulletEmitter.checkPool();
        particleEmitter.checkPool();
    }

    public void checkCollision() {
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            BulletModel bulletModel = bulletEmitter.getActiveList().get(i);

            if (bulletModel.isPlayerBullet()) {
                for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                    AsteroidModel asteroidModel = asteroidEmitter.getActiveList().get(j);

                    if (asteroidModel.getHitArea().contains(bulletModel.getPosition())) {
                        if (asteroidModel.takeDamage(1)) {
                            playerModel.addScore(asteroidModel.getHpMax() * 10);
                            powerUpsEmitter.makePower(
                                asteroidModel.getPosition().x,
                                asteroidModel.getPosition().y
                            );
                            boomEmitter.setup(asteroidModel.getPosition());
                        }

                        bulletModel.deactivate();
                        break;
                    }
                }

                for (int j = 0; j < botEmitter.getActiveList().size(); j++) {
                    BotModel botModel = botEmitter.getActiveList().get(j);

                    if (botModel.getHitArea().contains(bulletModel.getPosition())) {
                        if (botModel.takeDamage(1)) {
                            playerModel.addScore(botModel.getHpMax() * 100);
                            powerUpsEmitter.makePower(
                                botModel.getPosition().x,
                                botModel.getPosition().y
                            );
                            boomEmitter.setup(botModel.getPosition());
                        }

                        bulletModel.deactivate();
                        break;
                    }
                }
            } else {
                if (playerModel.getHitArea().contains(bulletModel.getPosition())) {
                    playerModel.takeDamage(5);
                    bulletModel.deactivate();
                    break;
                }
            }
        }

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            AsteroidModel asteroidModel = asteroidEmitter.getActiveList().get(i);

            if (playerModel.getHitArea().overlaps(asteroidModel.getHitArea())) {
                float len = playerModel.getPosition().dst(asteroidModel.getPosition());

                // Астероид иногда выталкивает корабль за экран, поэтому глубину пересечения лучше брать по модулю
                float interLen = Math.abs((playerModel.getHitArea().radius + asteroidModel.getHitArea().radius) - len);

                collisionHelper.set(asteroidModel.getPosition()).sub(playerModel.getPosition()).nor();

                float radiusRatio = playerModel.getHitArea().radius / asteroidModel.getHitArea().radius;

                // Чтобы не бросала вверх-вниз на краю эрана при столкновении с астеройдом
                if (playerModel.getPosition().x + collisionHelper.x * -interLen > playerModel.getHitArea().radius) {
                    playerModel.getPosition().mulAdd(collisionHelper, -interLen);
                    playerModel.getVelocity().mulAdd(collisionHelper, -interLen * 1 / radiusRatio * 70);
                }

                asteroidModel.getPosition().mulAdd(collisionHelper, interLen);
                asteroidModel.getVelocity().mulAdd(collisionHelper, interLen * radiusRatio * 70);

                playerModel.takeDamage(5);

                if (asteroidModel.takeDamage(1)) {
                    powerUpsEmitter.makePower(
                        asteroidModel.getPosition().x,
                        asteroidModel.getPosition().y
                    );

                    boomEmitter.setup(asteroidModel.getPosition());
                }
            }
        }

        for (int i = 0; i < botEmitter.getActiveList().size(); i++) {
            BotModel botModel = botEmitter.getActiveList().get(i);

            if (playerModel.getHitArea().overlaps(botModel.getHitArea())) {
                float len = playerModel.getPosition().dst(botModel.getPosition());

                float interLen = Math.abs((playerModel.getHitArea().radius + botModel.getHitArea().radius) - len);

                collisionHelper.set(botModel.getPosition()).sub(playerModel.getPosition()).nor();

                float radiusRatio = playerModel.getHitArea().radius / botModel.getHitArea().radius;

                if (playerModel.getPosition().x + collisionHelper.x * -interLen > playerModel.getHitArea().radius) {
                    playerModel.getPosition().mulAdd(collisionHelper, -interLen);
                    playerModel.getVelocity().mulAdd(collisionHelper, -interLen * 1 / radiusRatio * 20);
                }

                botModel.getPosition().mulAdd(collisionHelper, interLen);
                botModel.getVelocity().mulAdd(collisionHelper, interLen * radiusRatio * 20);

                playerModel.takeDamage(5);

                if (botModel.takeDamage(5)) {
                    powerUpsEmitter.makePower(
                        botModel.getPosition().x,
                        botModel.getPosition().y
                    );

                    boomEmitter.setup(botModel.getPosition());
                }
            }
        }

        for (int i = 0; i < powerUpsEmitter.getPowerUps().length; i++) {
            PowerUpModel powerUpModel = powerUpsEmitter.getPowerUps()[i];

            if (powerUpModel.isActive()) {
                if (playerModel.getHitArea().contains(powerUpModel.getPosition())) {
                    powerUpModel.use(playerModel);
                    powerUpModel.deactivate();
                }
            }
        }

        if (playerModel.getLives() < 0) {
            game.setScreen(game.getMenuScreen());
        }
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        ResourceManager.getInstance().clear();
    }

    @Override
    public void dispose() {
        ResourceManager.getInstance().clear();
    }

}

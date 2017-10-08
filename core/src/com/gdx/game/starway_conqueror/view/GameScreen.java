package com.gdx.game.starway_conqueror.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private ApplicationController game;

    private SpriteBatch batch;
    private BitmapFont fnt;
    private BackgroundModel backgroundModel;
    private PlayerModel playerModel;
    private AsteroidEmitter asteroidEmitter;
    private BulletEmitter bulletEmitter;
    private PowerUpsEmitter powerUpsEmitter;
    private ParticleEmitter particleEmitter;
    private BoomEmitter boomEmitter;
    private BotEmitter botEmitter;
    private TextureAtlas atlas;
    private Music music;
    private int level;
    private int maxLevels;
    private float timePerLevel;
    private float currentLevelTime;

    List<LevelInfoModel> levels;

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
        levels = new ArrayList<LevelInfoModel>();
        BufferedReader br = null;
        try {
            br = Gdx.files.internal("leveldata.csv").reader(8192);
            br.readLine();
            String str;
            while ((str = br.readLine()) != null) {
                String[] arr = str.split("\\t");
                LevelInfoModel levelInfoModel = new LevelInfoModel(Integer.parseInt(arr[0]), Float.parseFloat(arr[1]),
                        Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Float.parseFloat(arr[4])
                        , Float.parseFloat(arr[5]));
                levels.add(levelInfoModel);
            }
            maxLevels = levels.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameScreen(ApplicationController game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        ResourceManager.getInstance().loadAssets(ScreenType.GAME);
        atlas = ResourceManager.getInstance().mainAtlas;
        backgroundModel = new BackgroundModel(atlas.findRegion("star16"));
        fnt = ResourceManager.getInstance().assetManager.get("font2.fnt", BitmapFont.class);
        playerModel = new PlayerModel(this, atlas.findRegion("ship64"), atlas.findRegion("hpBar"), atlas.findRegion("joystick"), ResourceManager.getInstance().assetManager.get("laser.wav", Sound.class), new Vector2(100, 328), new Vector2(0.0f, 0.0f), 800.0f);
        asteroidEmitter = new AsteroidEmitter(this, atlas.findRegion("asteroid64"), 20, 0.4f);
        bulletEmitter = new BulletEmitter(atlas.findRegion("bullets36"), 100);
        powerUpsEmitter = new PowerUpsEmitter(atlas.findRegion("powerUps"));
        particleEmitter = new ParticleEmitter(atlas.findRegion("star16"));
        boomEmitter = new BoomEmitter(atlas.findRegion("explosion64"), ResourceManager.getInstance().assetManager.get("CollapseNorm.wav", Sound.class));
        botEmitter = new BotEmitter(this, atlas.findRegion("ufo"), 10, 1.0f);
        music = ResourceManager.getInstance().assetManager.get("music.mp3", Music.class);
        music.setLooping(true);
        loadFullGameInfo();
        level = 1;
        currentLevelTime = 0.0f;
        timePerLevel = 60.0f;
        // music.play();
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
        playerModel.renderHUD(batch, fnt, 20, 668);
        fnt.draw(batch, "LEVEL: " + level, 600, 680);
        batch.end();
    }

    public void updateLevel(float dt) {
        currentLevelTime += dt;
        if (currentLevelTime > timePerLevel) {
            currentLevelTime = 0.0f;
            level++;
            if (level > maxLevels) level = maxLevels;
            asteroidEmitter.setGenerationTime(getCurrentLevelInfo().getAsteroidGenerationTime());
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

    private final Vector2 collisionHelper = new Vector2(0, 0);

    public void checkCollision() {
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            BulletModel b = bulletEmitter.getActiveList().get(i);
            if (b.isPlayersBullet()) {
                for (int j = 0; j < asteroidEmitter.getActiveList().size(); j++) {
                    AsteroidModel a = asteroidEmitter.getActiveList().get(j);
                    if (a.getHitArea().contains(b.getPosition())) {
                        if (a.takeDamage(1)) {
                            playerModel.addScore(a.getHpMax() * 10);
                            powerUpsEmitter.makePower(a.getPosition().x, a.getPosition().y);
                            boomEmitter.setup(a.getPosition());
                        }
                        b.deactivate();
                        break;
                    }
                }
                for (int j = 0; j < botEmitter.getActiveList().size(); j++) {
                    BotModel botModel = botEmitter.getActiveList().get(j);
                    if (botModel.getHitArea().contains(b.getPosition())) {
                        if (botModel.takeDamage(1)) {
                            playerModel.addScore(botModel.getHpMax() * 100);
                            powerUpsEmitter.makePower(botModel.getPosition().x, botModel.getPosition().y);
                            boomEmitter.setup(botModel.getPosition());
                        }
                        b.deactivate();
                        break;
                    }
                }
            } else {
                if (playerModel.getHitArea().contains(b.getPosition())) {
                    playerModel.takeDamage(5);
                    b.deactivate();
                    break;
                }
            }
        }

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            AsteroidModel a = asteroidEmitter.getActiveList().get(i);
            if (playerModel.getHitArea().overlaps(a.getHitArea())) {
                float len = playerModel.getPosition().dst(a.getPosition());
                float interLen = (playerModel.getHitArea().radius + a.getHitArea().radius) - len;
                collisionHelper.set(a.getPosition()).sub(playerModel.getPosition()).nor();
                a.getPosition().mulAdd(collisionHelper, interLen);
                playerModel.getPosition().mulAdd(collisionHelper, -interLen);
                a.getVelocity().mulAdd(collisionHelper, interLen * 4);
                playerModel.getVelocity().mulAdd(collisionHelper, -interLen * 4);
                a.takeDamage(1);
                playerModel.takeDamage(1);
            }
        }

        for (int i = 0; i < powerUpsEmitter.getPowerUps().length; i++) {
            PowerUpModel p = powerUpsEmitter.getPowerUps()[i];
            if (p.isActive()) {
                if (playerModel.getHitArea().contains(p.getPosition())) {
                    p.use(playerModel);
                    p.deactivate();
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

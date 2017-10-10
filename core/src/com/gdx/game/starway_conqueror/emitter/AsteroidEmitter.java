package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.AsteroidModel;
import com.gdx.game.starway_conqueror.model.LevelInfoModel;
import com.gdx.game.starway_conqueror.component.ObjectPool;
import com.gdx.game.starway_conqueror.view.GameScreen;

public class AsteroidEmitter extends ObjectPool<AsteroidModel> {

    private GameScreen gameScreen;
    private TextureRegion asteroidTexture;
    private float generationTime;
    private float innerTimer;

    public void setGenerationTime(float generationTime) {
        this.generationTime = generationTime;
    }

    @Override
    protected AsteroidModel newObject() {
        return new AsteroidModel(asteroidTexture);
    }

    public AsteroidEmitter(GameScreen gameScreen, int size, float generationTime) {
        super();
        this.gameScreen = gameScreen;
        this.asteroidTexture = ResourceManager.getInstance().getAtlasRegion("my2.pack", "asteroid256");
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
        this.generationTime = generationTime;
        this.innerTimer = 0.0f;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        innerTimer += dt;
        if (innerTimer > generationTime) {
            innerTimer -= generationTime;
            setup();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup() {
        LevelInfoModel info = gameScreen.getCurrentLevelInfo();
        AsteroidModel a = getActiveElement();
        float x = MathUtils.random(1400.0f, 2200.0f);
        float y = MathUtils.random(0, 720.0f);
        float vx = -MathUtils.random(info.getAsteroidSpeedMin(), info.getAsteroidSpeedMax());
        float vy = 0;
        int hpMax = MathUtils.random(info.getAsteroidHpMin(), info.getAsteroidHpMax());
        int delta = info.getAsteroidHpMax() - info.getAsteroidHpMin();
        int delta2 = hpMax - info.getAsteroidHpMin();
        float r = 0.2f + 0.4f * ((float)delta2 / (float)delta);
        a.activate(x, y, vx, vy, hpMax, r);
    }

}

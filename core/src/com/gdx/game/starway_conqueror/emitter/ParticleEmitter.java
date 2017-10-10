package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.game.starway_conqueror.component.ObjectPool;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.ParticleModel;

public class ParticleEmitter extends ObjectPool<ParticleModel> {

    private TextureRegion oneParticle;

    public ParticleEmitter() {
        this.oneParticle = ResourceManager.getInstance().getAtlasRegion("my2.pack", "star16");
    }

    @Override
    protected ParticleModel newObject() {
        return new ParticleModel();
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i < activeList.size(); i++) {
            ParticleModel o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);

            if(Math.random() < 0.04) {
                scale *= 2.0f;
            }

            batch.setColor(
                lerp(o.getR1(), o.getR2(), t),
                lerp(o.getG1(), o.getG2(), t),
                lerp(o.getB1(), o.getB2(), t),
                lerp(o.getA1(), o.getA2(), t)
            );

            batch.draw(
                oneParticle,
                o.getPosition().x - 8,
                o.getPosition().y - 8,
                8,
                8,
                16,
                16,
                scale,
                scale,
                0
            );
        }

        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        for (int i = 0; i < activeList.size(); i++) {
            ParticleModel o = activeList.get(i);
            float t = o.getTime() / o.getTimeMax();
            float scale = lerp(o.getSize1(), o.getSize2(), t);

            if(Math.random() < 0.04) {
                scale *= 2.0f;
            }

            batch.setColor(
                lerp(o.getR1(), o.getR2(), t),
                lerp(o.getG1(), o.getG2(), t),
                lerp(o.getB1(), o.getB2(), t),
                lerp(o.getA1(), o.getA2(), t)
            );

            batch.draw(
                oneParticle,
                o.getPosition().x - 8,
                o.getPosition().y - 8,
                8,
                8,
                16,
                16,
                scale,
                scale,
                0
            );
        }

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup(
        float x,
        float y,
        float vx,
        float vy,
        float timeMax,
        float size1,
        float size2,
        float r1,
        float g1,
        float b1,
        float a1,
        float r2,
        float g2,
        float b2,
        float a2
    ) {
        ParticleModel item = getActiveElement();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }

}

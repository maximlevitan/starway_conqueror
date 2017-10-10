package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.BulletModel;
import com.gdx.game.starway_conqueror.model.BulletType;
import com.gdx.game.starway_conqueror.component.ObjectPool;

public class BulletEmitter extends ObjectPool<BulletModel> {

    private TextureRegion[] bulletsTexture;

    @Override
    protected BulletModel newObject() {
        return new BulletModel();
    }

    public BulletEmitter(int size) {
        super(size);

        this.bulletsTexture = new TextureRegion[2];
        for (int i = 0; i < 2; i++) {
            this.bulletsTexture[i] = new TextureRegion(
                ResourceManager.getInstance().getAtlasRegion("my2.pack", "bullets36"),
                i * 36,
                0,
                36,
                36
            );
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            batch.draw(
                bulletsTexture[activeList.get(i).getType().getIndex()],
                activeList.get(i).getPosition().x - 24,
                activeList.get(i).getPosition().y - 24,
                24,
                24,
                48,
                48,
                0.7f,
                0.7f,
                0.0f
            );
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup(BulletType type, boolean isPlayerBullet, float x, float y, float vx, float vy) {
        BulletModel b = getActiveElement();
        b.activate(type, isPlayerBullet, x, y, vx, vy);
    }

}

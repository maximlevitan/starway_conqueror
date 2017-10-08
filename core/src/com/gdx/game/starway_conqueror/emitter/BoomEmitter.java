package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.model.BoomModel;

public class BoomEmitter {
    private BoomModel[] booms;
    private Sound boomSound;

    public BoomEmitter(TextureRegion texture, Sound boomSound) {
        booms = new BoomModel[50];
        TextureRegion[][] regions = texture.split(64, 64);
        TextureRegion[] result = new TextureRegion[regions[0].length * regions.length];
        this.boomSound = boomSound;
        for (int i = 0, n = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++, n++) {
                result[n] = regions[i][j];
            }
        }
        for (int i = 0; i < booms.length; i++) {
            booms[i] = new BoomModel(result);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < booms.length; i++) {
            if (booms[i].isActive()) {
                booms[i].update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < booms.length; i++) {
            if (booms[i].isActive()) {
                booms[i].render(batch);
            }
        }
    }

    public void setup(Vector2 position) {
        for (int i = 0; i < booms.length; i++) {
            if (!booms[i].isActive()) {
                booms[i].activate(position);
                boomSound.play();
                break;
            }
        }
    }

    public void dispose() {
        boomSound.dispose();
    }
}

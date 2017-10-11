package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.PowerUpModel;

public class PowerUpsEmitter {

    private PowerUpModel[] powerUps;
    private TextureRegion[][] textureRegion;

    public PowerUpModel[] getPowerUps() {
        return powerUps;
    }

    public PowerUpsEmitter() {
        this.textureRegion = ResourceManager.getInstance()
            .getAtlasRegion("my2.pack", "powerUps").split(32, 32);

        this.powerUps = new PowerUpModel[50];
        for (int i = 0; i < powerUps.length; i++) {
            powerUps[i] = new PowerUpModel();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < powerUps.length; i++) {
            if (powerUps[i].isActive()) {
                batch.draw(
                    textureRegion[0][powerUps[i].getType().getNumber()],
                    powerUps[i].getPosition().x - 16,
                    powerUps[i].getPosition().y - 16
                );
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < powerUps.length; i++) {
            if (powerUps[i].isActive()) {
                powerUps[i].update(dt);
            }
        }
    }

    public void makePower(float x, float y) {
        if (Math.random() < 0.3) {
            for (int i = 0; i < powerUps.length; i++) {
                if (!powerUps[i].isActive()) {
                    PowerUpModel.Type t = PowerUpModel.Type.values()[(int) (Math.random() * 4)];
                    powerUps[i].activate(x, y, t);
                    break;
                }
            }
        }
    }

}

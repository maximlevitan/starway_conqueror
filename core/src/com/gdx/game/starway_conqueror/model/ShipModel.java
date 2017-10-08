package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.math.Vector2;

public abstract class ShipModel extends AbstractSpaceObjectModel {
    protected float enginePower;

    protected float currentFire;
    protected float fireRate;

    protected Vector2 weaponDirection;
    protected boolean isPlayer;

    public void pressFire(float dt) {
        currentFire += dt;
        if (currentFire > fireRate) {
            currentFire -= fireRate;
            fire();
        }
    }

    public void fire() {
        BulletType bt = BulletType.FIREBALL;
        if (!isPlayer) bt = BulletType.GREENRAY;
        game.getBulletEmitter().setup(bt, isPlayer, position.x + 24.0f, position.y + 0.0f, weaponDirection.x * 640, weaponDirection.y * 640);
    }
}

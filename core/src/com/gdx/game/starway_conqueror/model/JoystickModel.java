package com.gdx.game.starway_conqueror.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.processor.MyInputProcessor;

public class JoystickModel {

    private PlayerModel playerModel;
    private TextureRegion back;
    private TextureRegion stick;
    private TextureRegion fire;
    private Rectangle rectangle;
    private Rectangle fireBtnRect;
    private float joyCenterX, joyCenterY;
    private int lastId;
    private Vector2 vs;
    private Vector2 norma;
    private float backScale = 0.7f;

    public float getPower() {
        return vs.len() / 75.0f * 1.4f;
    }

    public Vector2 getNorma() {
        return norma;
    }

    public JoystickModel(PlayerModel playerModel, TextureRegion texture) {
        this.playerModel = playerModel;
        this.back = new TextureRegion(texture, 0, 0, 200, 200);
        this.stick = new TextureRegion(texture, 0, 200, 50, 50);
        this.rectangle = new Rectangle(
            50 * backScale, 50 * backScale, 200 * backScale, 200 * backScale
        );

        this.fireBtnRect = new Rectangle(1080, 100, 100, 100);
        this.joyCenterX = rectangle.x + rectangle.width / 2;
        this.joyCenterY = rectangle.y + rectangle.height / 2;
        this.vs = new Vector2(0, 0);
        this.norma = new Vector2(0, 0);
        this.lastId = -1;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 0.5f);
        batch.draw(back, rectangle.x, rectangle.y, 100, 100, 200, 200, backScale, backScale, 0);
        batch.setColor(1, 1, 1, 0.7f);
        batch.draw(stick, joyCenterX + vs.x + 5, joyCenterY + vs.y + 5);
        batch.setColor(1, 1, 1, 0.7f);
        batch.draw(stick, fireBtnRect.x, fireBtnRect.y, 100, 100);
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        MyInputProcessor mip = (MyInputProcessor) Gdx.input.getInputProcessor();

        if (lastId == -1) {
            lastId = mip.isTouchedInArea(rectangle);
        }

        if (lastId > -1) {
            float touchX = mip.getX(lastId);
            float touchY = mip.getY(lastId);
            vs.x = touchX - joyCenterX;
            vs.y = touchY - joyCenterY;

            if (vs.len() > 75 * backScale) {
                vs.nor().scl(75 * backScale);
            }
        }

        if (lastId > -1 && !mip.isTouched(lastId)) {
            lastId = -1;
            vs.x = 0;
            vs.y = 0;
        }

        norma.set(vs);
        norma.nor();

        if (mip.isTouchedInArea(fireBtnRect) != -1) {
            playerModel.pressFire(dt);
        }
    }

}

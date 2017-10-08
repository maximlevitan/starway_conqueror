package com.gdx.game.starway_conqueror.emitter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.model.BotModel;
import com.gdx.game.starway_conqueror.component.ObjectPool;
import com.gdx.game.starway_conqueror.component.Route;
import com.gdx.game.starway_conqueror.view.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class BotEmitter extends ObjectPool<BotModel> {
    private GameScreen game;
    private TextureRegion botTexture;
    private float generationTime;
    private float innerTimer;
    private List<Route> routes;

    @Override
    protected BotModel newObject() {
        return new BotModel(game, botTexture);
    }

    public BotEmitter(GameScreen game, TextureRegion botTexture, int size, float generationTime) {
        super();
        this.game = game;
        this.botTexture = botTexture;
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
        this.generationTime = generationTime;
        this.innerTimer = 0.0f;
        routes = new ArrayList<Route>();
        Route r1 = new Route(new Vector2(1400, 640));
        r1.addPoint(1500, new Vector2(-320.0f, 0.0f)).addPoint(600, new Vector2(-320.0f, -120.0f));
        Route r2 = new Route(new Vector2(1400, 80));
        r2.addPoint(1500, new Vector2(-320.0f, 0.0f)).addPoint(600, new Vector2(-320.0f, 120.0f));
        Route r3 = new Route(new Vector2(1400, 400));
        r3.addPoint(1500, new Vector2(-400.0f, 0.0f)).addPoint(600, new Vector2(-400.0f, 180.0f));
        Route r4 = new Route(new Vector2(1400, 320));
        r4.addPoint(1500, new Vector2(-400.0f, 0.0f)).addPoint(600, new Vector2(-400.0f, 180.0f));

        routes.add(r1);
        routes.add(r2);
        routes.add(r3);
        routes.add(r4);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        innerTimer += dt;
        if(innerTimer > generationTime) {
            innerTimer -= generationTime;
            setup();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
    }

    public void setup() {
        BotModel b = getActiveElement();
        b.activate(routes.get((int)(Math.random() * routes.size())));
    }
}

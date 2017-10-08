package com.gdx.game.starway_conqueror.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.manager.ResourceManager;
import com.gdx.game.starway_conqueror.model.BackgroundModel;
import com.gdx.game.starway_conqueror.processor.MyInputProcessor;
import com.gdx.game.starway_conqueror.application.ApplicationController;

public class MenuScreen implements Screen {
    private ApplicationController game;

    private SpriteBatch batch;
    private BackgroundModel backgroundModel;
    private Vector2 emptyVelocity = new Vector2(0, 0);
    private TextureRegion texStart;
    private TextureRegion texExit;
    private Rectangle rectStart;
    private Rectangle rectExit;

    private MyInputProcessor mip;

    public MenuScreen(ApplicationController game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        ResourceManager.getInstance().loadAssets(ScreenType.MENU);
        TextureAtlas atlas = ResourceManager.getInstance().mainAtlas;
        backgroundModel = new BackgroundModel(atlas.findRegion("star16"));
        texExit = atlas.findRegion("btExit");
        texStart = atlas.findRegion("btPlay");
        rectStart = new Rectangle(256, 232, texStart.getRegionWidth(), texStart.getRegionHeight());
        rectExit = new Rectangle(1280 - 512, 232, texExit.getRegionWidth(), texExit.getRegionHeight());
        mip = (MyInputProcessor) Gdx.input.getInputProcessor();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(((ApplicationController) game).getCamera().combined);
        batch.begin();
        backgroundModel.render(batch);
        batch.draw(texStart, rectStart.x, rectStart.y);
        batch.draw(texExit, rectExit.x, rectExit.y);
        batch.end();
    }

    public void update(float dt) {
        backgroundModel.update(dt, emptyVelocity);
        if (mip.isTouchedInArea(rectStart) != -1) {
            game.setScreen(game.getGameScreen());
        }
        if (mip.isTouchedInArea(rectExit) != -1) {
            Gdx.app.exit();
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

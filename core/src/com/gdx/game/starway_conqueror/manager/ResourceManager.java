package com.gdx.game.starway_conqueror.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.gdx.game.starway_conqueror.view.ScreenType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ResourceManager {
    private static ResourceManager instance;

    public AssetManager assetManager;

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }

        return instance;
    }

    private TextureAtlas mainAtlas;

    private ResourceManager() {
        assetManager = new AssetManager();
    }

    public void loadScreenAssetsByType(ScreenType type) {
        switch (type) {
            case MENU:
                addAtlas("my2.pack").addTexture("bg.png").waitLoading();
                mainAtlas = getAtlas("my2.pack");
                break;
            case GAME:
                addAtlas("my2.pack").addTexture("bg.png").addFont("font2.fnt");

                assetManager.load("music.mp3", Music.class);
                assetManager.load("laser.wav", Sound.class);
                assetManager.load("CollapseNorm.wav", Sound.class);

                waitLoading();
                mainAtlas = getAtlas("my2.pack");
                break;
        }
    }

    public TextureAtlas getMainAtlas() {
        return mainAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ResourceManager addAtlas(String filename) {
        assetManager.load(filename, TextureAtlas.class);

        return this;
    }

    public ResourceManager addTexture(String filename) {
        assetManager.load(filename, Texture.class);

        return this;
    }

    public ResourceManager addFont(String filename) {
        assetManager.load(filename, BitmapFont.class);

        return this;
    }

    public ResourceManager addSound(String filename) {
        assetManager.load(filename, Sound.class);

        return this;
    }

    public ResourceManager addMusic(String filename) {
        assetManager.load(filename, Sound.class);

        return this;
    }

    public TextureAtlas getAtlas(String filename) {
        return assetManager.get(filename, TextureAtlas.class);
    }

    public TextureAtlas.AtlasRegion getAtlasRegion(String filename, String regionName) {
        return assetManager.get(filename, TextureAtlas.class).findRegion(regionName);
    }

    public Texture getTexture(String filename) {
        return assetManager.get(filename, Texture.class);
    }

    public BitmapFont getFont(String filename) {
        return assetManager.get(filename, BitmapFont.class);
    }

    public Sound getSound(String filename) {
        return assetManager.get(filename, Sound.class);
    }

    public Music getMusic(String filename) {
        return assetManager.get(filename, Music.class);
    }

    public <T> List<T> loadInternalFileData(String filename, Class<? extends T> type) {
        List<T> container = new ArrayList<T>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Gdx.files.internal(filename).file()));
            br.readLine();
            String str;
            while ((str = br.readLine()) != null) {
                container.add(type.getConstructor(String.class).newInstance(str));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return container;
    }

    public void waitLoading() {
        assetManager.finishLoading();
    }

    public void clear() {
        assetManager.clear();
    }
}

package com.gdx.game.starway_conqueror.model;

public class LevelInfoModel {
    private int level;
    private float asteroidGenerationTime;
    private int asteroidHpMin;
    private int asteroidHpMax;
    private float asteroidSpeedMin;
    private float asteroidSpeedMax;

    public int getLevel() {
        return level;
    }

    public float getAsteroidGenerationTime() {
        return asteroidGenerationTime;
    }

    public int getAsteroidHpMin() {
        return asteroidHpMin;
    }

    public int getAsteroidHpMax() {
        return asteroidHpMax;
    }

    public float getAsteroidSpeedMin() {
        return asteroidSpeedMin;
    }

    public float getAsteroidSpeedMax() {
        return asteroidSpeedMax;
    }

    public LevelInfoModel(int level, float asteroidGenerationTime, int asteroidHpMin, int asteroidHpMax, float asteroidSpeedMin, float asteroidSpeedMax) {
        this.level = level;
        this.asteroidGenerationTime = asteroidGenerationTime;
        this.asteroidHpMin = asteroidHpMin;
        this.asteroidHpMax = asteroidHpMax;
        this.asteroidSpeedMin = asteroidSpeedMin;
        this.asteroidSpeedMax = asteroidSpeedMax;
    }
}

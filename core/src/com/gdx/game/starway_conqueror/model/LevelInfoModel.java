package com.gdx.game.starway_conqueror.model;

public class LevelInfoModel {

    private int level;
    private float asteroidGenerationTime;
    private float botGenerationTime;
    private int asteroidHpMin;
    private int asteroidHpMax;
    private float asteroidSpeedMin;
    private float asteroidSpeedMax;

    public LevelInfoModel(String data) {
        String[] arrData = data.split("\\t");

        this.level = Integer.parseInt(arrData[0]);
        this.asteroidGenerationTime = Float.parseFloat(arrData[1]);
        this.asteroidHpMin = Integer.parseInt(arrData[2]);
        this.asteroidHpMax = Integer.parseInt(arrData[3]);
        this.asteroidSpeedMin = Float.parseFloat(arrData[4]);
        this.asteroidSpeedMax = Float.parseFloat(arrData[5]);
        this.botGenerationTime = Float.parseFloat(arrData[6]);
    }

    public int getLevel() {
        return level;
    }

    public float getAsteroidGenerationTime() {
        return asteroidGenerationTime;
    }

    public float getBotGenerationTime() {
        return botGenerationTime;
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

}

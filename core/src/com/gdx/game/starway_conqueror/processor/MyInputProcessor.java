package com.gdx.game.starway_conqueror.processor;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.gdx.game.starway_conqueror.application.ApplicationController;

import java.util.HashMap;
import java.util.Map;

public class MyInputProcessor implements InputProcessor {
    class TouchInfo {
        int x;
        int y;
        boolean touched;

        void set(int x, int y, boolean touched) {
            this.x = x;
            this.y = y;
            this.touched = touched;
        }
    }

    private ApplicationController game;
    private HashMap<Integer, TouchInfo> map = new HashMap<Integer, TouchInfo>();
    private Vector2 temp = new Vector2(0, 0);

    public MyInputProcessor(ApplicationController game) {
        this.game = game;
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    public void clear() {
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.getViewport().unproject(temp.set(screenX, screenY));
        map.get(pointer).set((int)temp.x, (int)temp.y, true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        map.get(pointer).x = 0;
        map.get(pointer).y = 0;
        map.get(pointer).touched = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        game.getViewport().unproject(temp.set(screenX, screenY));
        map.get(pointer).set((int)temp.x, (int)temp.y, true);
        return false;
    }

    public boolean isTouched(int pointer) {
        return map.get(pointer).touched;
    }

    public int getX(int pointer) {
        return map.get(pointer).x;
    }

    public int getY(int pointer) {
        return map.get(pointer).y;
    }

    public int isTouchedInArea(Rectangle rect) {
        return isTouchedInArea((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
    }

    public int isTouchedInArea(int x, int y, int w, int h) {
        for (Map.Entry<Integer, TouchInfo> o : map.entrySet()) {
            if (o.getValue().touched) {
                int id = o.getKey();
                TouchInfo t = o.getValue();
                if (t.x > x && t.x < x + w && t.y > y && t.y < y + h) {
                    return id;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

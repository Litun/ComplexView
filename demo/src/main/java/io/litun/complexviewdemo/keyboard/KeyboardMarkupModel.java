package io.litun.complexviewdemo.keyboard;

import java.util.List;

/**
 * Created by Litun on 22.04.2017.
 */

public class KeyboardMarkupModel {
    private float x;
    private float y;
    private float width;
    private float height;
    private List<KeyboardMarkupItem> items;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public List<KeyboardMarkupItem> getItems() {
        return items;
    }
}

package cz.reindl.game.entity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class GameObject {

    protected float x, y;
    protected int width, height;
    protected Bitmap bitmap;
    protected Bitmap bitmap2;

    public GameObject(float x, float y, int width, int height, Bitmap bitmap, Bitmap bitmap2) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.bitmap = bitmap;
        this.bitmap2 = bitmap2;
    }

    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObject(int height, int width, float x, float y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    GameObject() {
    }

    public GameObject(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        this.bitmap = bitmap;
        this.bitmap2 = bitmap2;
        this.x = x;
        this.y = y;
    }

    public GameObject(Bitmap resizeBitmap, int width, int height) {
        this.bitmap = resizeBitmap;
        this.width = width;
        this.height = height;
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        this.bitmap2 = bitmap2;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}

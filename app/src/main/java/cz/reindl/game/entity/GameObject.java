package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class GameObject {

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

    public GameObject() {
    }

    public GameObject(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        this.bitmap = bitmap;
        this.bitmap2 = bitmap2;
        this.x = x;
        this.y = y;
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

    public Rect getRect() {
        return new Rect((int) this.x, (int) this.y, (int) this.x + this.width, (int) this.y + this.height); //Object collision params
    }

}

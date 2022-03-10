package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

import cz.reindl.game.constants.Constants;

public class Bird extends GameObject {

    private ArrayList<Bitmap> birdList = new ArrayList<>(); //List of Bitmap - animation during game
    public final int TICK;
    private int countTick, idCurrentBitmap;
    private float drop, velocity;

    public Bird() {
        this.countTick = 0;
        this.TICK = 8;
        this.idCurrentBitmap = 0;
        this.drop = 0;
        this.velocity = 0.6f;
    }

    public void draw(Canvas canvas) {
        drop();
        death();
        canvas.drawBitmap(this.getBitmap(), this.getX(), this.getY(), null);
    }

    private void drop() {
        this.drop += velocity;
        y += this.drop;
    }

    public void death() {
        if (y >= Constants.SCREEN_HEIGHT) {
            setY(Constants.SCREEN_HEIGHT / 2 - this.getHeight() / 2);
            this.drop = 0;
        } else if (y <= Constants.SCREEN_HEIGHT) {
        }
    }

    public ArrayList<Bitmap> getBirdList() {
        return birdList;
    }

    public void setBirdList(ArrayList<Bitmap> birdList) {
        this.birdList = birdList;
        for (int i = 0; i < birdList.size(); i++) {
            this.birdList.set(i, Bitmap.createScaledBitmap(this.birdList.get(i), this.getWidth(), this.getHeight(), true));
        }
    }

    @Override
    public Bitmap getBitmap() {
        countTick++;
        //Changing img every 8 ticks
        if (this.countTick == this.TICK) {
            for (int i = 0; i < birdList.size(); i++) {
                if (i == birdList.size() - 1) {
                    this.idCurrentBitmap = 0;
                    break;
                } else if (this.idCurrentBitmap == i) {
                    idCurrentBitmap = i + 1;
                    break;
                }
            }
            countTick = 0;
        }
        if (this.drop < 0) {
            Matrix matrix = new Matrix(); //Transforming bird
            matrix.postRotate(-25);
            return Bitmap.createBitmap(birdList.get(idCurrentBitmap), 0, 0, birdList.get(idCurrentBitmap).getWidth(), birdList.get(idCurrentBitmap).getHeight(), matrix, true);
        } else if (drop >= 0) {
            Matrix matrix = new Matrix();
            if (drop <= 70) {
                matrix.postRotate(-25 + (drop * 2));
            } else {
                matrix.postRotate(45);
            }
            return Bitmap.createBitmap(birdList.get(idCurrentBitmap), 0, 0, birdList.get(idCurrentBitmap).getWidth(), birdList.get(idCurrentBitmap).getHeight(), matrix, true);
        }
        return this.birdList.get(idCurrentBitmap);
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }
}

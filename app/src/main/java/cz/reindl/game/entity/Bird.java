package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.view.View;

public class Bird extends GameObject {

    Matrix matrix = new Matrix(); //Transforming bird
    private ArrayList<Bitmap> birdList = new ArrayList<>(); //List of Bitmap - animation during game
    public final int TICK = 8;
    private int countTick;
    private int idCurrentBitmap;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;
    private int highScore;
    private float gravity, velocity;

    public Bird() {
        this.countTick = 0;
        this.idCurrentBitmap = 0;
        this.gravity = 0;
        this.velocity = 0.6f;
        this.score = 0;
    }

    public void draw(Canvas canvas) {
        drop();
        death();
        canvas.drawBitmap(this.getBitmap(), this.getX(), this.getY(), null);
    }

    private void drop() {
        this.gravity += velocity;
        y += this.gravity;
    }

    public void death() {
        if (this.y >= Constants.SCREEN_HEIGHT || this.x == View.barrier.getX()) {
            System.out.println("X barrier" + View.barrier.getX() + " " + "X bird: " + this.x);
            setY(Constants.SCREEN_HEIGHT / 2 - this.getHeight() / 2);
            this.gravity = 0;
            if (this.score > this.highScore) {
                this.score = this.highScore;
            }
            this.score = 0;
        } else if (y <= 0) {
            setY(0);
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
        if (this.countTick == TICK) {
            for (int i = 0; i < birdList.size(); i++) {
                if (i == birdList.size() - 1) {
                    this.idCurrentBitmap = 0;
                } else if (this.idCurrentBitmap == i) {
                    ++i;
                    idCurrentBitmap = i;
                }
            }
            countTick = 0;
        }
        return this.birdList.get(idCurrentBitmap);
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
}

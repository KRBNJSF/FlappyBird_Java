package cz.reindl.game.entity;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;

public class Bird extends GameObject {

    Matrix matrix = new Matrix(); //Transforming bird
    private ArrayList<Bitmap> birdList = new ArrayList<>(); //List of Bitmap - animation during game

    public static boolean skinUnlocked, changeSkin;

    private int tick;

    private float gravity;
    private final float velocity;

    private int score;
    private int highScore;

    public Bird() {
        super();
        this.tick = 0;
        this.gravity = 0;
        this.velocity = 0.6f;
        this.score = 0;
    }

    public void renderBird(Canvas canvas) {
        drop();
        checkY();
        canvas.drawBitmap(this.changeBird(), this.getX(), this.getY(), null);
    }

    private void drop() {
        this.gravity += velocity;
        y += this.gravity;
    }

    public void checkY() {
        if (y <= 0) {
            setGravity(2);
            if (y <= -this.height) {
                setY(0);
            }
        }
    }

    public void setBirdList(ArrayList<Bitmap> birdList) {
        this.birdList = birdList;
        for (int i = 0; i < birdList.size(); i++) {
            this.birdList.set(i, Bitmap.createScaledBitmap(this.birdList.get(i), this.getWidth(), this.getHeight(), true));
        }
    }

    public Bitmap changeBird() {
        if (skinUnlocked && changeSkin) {
            this.birdList.set(0, birdList.get(2));
            this.birdList.set(1, birdList.get(3));
        } else {
            this.birdList.set(0, birdList.get(4));
            this.birdList.set(1, birdList.get(5));
        }
        tick++;
        if (tick > 5) {
            if (tick == 15) {
                tick = 0;
                //Log.d(Constants.TAG = "Bird change", "Bird change");
            }
            return birdList.get(0);
        }
        return birdList.get(1);
    }

    public ArrayList<Bitmap> getBirdList() {
        return birdList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

}

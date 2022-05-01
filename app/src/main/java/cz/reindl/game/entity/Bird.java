package cz.reindl.game.entity;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Bird extends GameObject {

    Matrix matrix = new Matrix(); //Transforming bird
    private List<Bitmap> birdList = new ArrayList<>(); //List of Bitmap - animation during game

    //SKIN
    public static boolean legendarySkin;
    public static int boughtSkin = 0;
    //SKIN USAGE
    public static boolean legendarySkinUsing, boughtSkinUsing;

    //REFRESH TICKS
    private int tick = 0;

    //GRAVITY
    private float fallGravity;
    private final float velocity;

    //STATS
    private int score, coins;
    private int highScore;

    public Bird() {
        super();
        this.fallGravity = 0;
        this.velocity = 0.6f;
        this.score = 0;
    }

    public void renderBird(Canvas canvas) {
        fall();
        checkY();
        canvas.drawBitmap(this.changeBird(), this.getX(), this.getY(), null);
    }

    private void fall() {
        this.fallGravity += velocity;
        y += this.fallGravity;
    }

    public void checkY() {
        if (y <= 0) {
            setFallGravity(3);
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

    //ANIMATION
    public Bitmap changeBird() {
        if (legendarySkin && legendarySkinUsing) {
            this.birdList.set(0, birdList.get(2));
            this.birdList.set(1, birdList.get(3));
        } else if (boughtSkinUsing) {
            this.birdList.set(0, birdList.get(6));
            this.birdList.set(1, birdList.get(7));
        } else {
            this.birdList.set(0, birdList.get(4));
            this.birdList.set(1, birdList.get(5));
        }
        tick++;
        if (tick > 5) {
            if (tick == 15) {
                tick = 0;
            }
            return birdList.get(0);
        }
        return birdList.get(1);
    }

    public List<Bitmap> getBirdList() {
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

    public float getFallGravity() {
        return fallGravity;
    }

    public void setFallGravity(float fallGravity) {
        this.fallGravity = fallGravity;
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}

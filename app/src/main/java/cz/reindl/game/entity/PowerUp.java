package cz.reindl.game.entity;

import static android.view.View.INVISIBLE;
import static cz.reindl.game.MainActivity.powerUpText;
import static cz.reindl.game.MainActivity.reviveButton;
import static cz.reindl.game.MainActivity.skipReviveButton;
import static cz.reindl.game.MainActivity.view;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.values.Values.speedPipe;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.powerUp;
import static cz.reindl.game.view.View.sound;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;

import java.util.Random;

import cz.reindl.game.MainActivity;
import cz.reindl.game.values.Values;
import cz.reindl.game.view.View;

public class PowerUp extends GameObject {

    public PowerUp(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.y = y;
        this.x = x;
    }

    @SuppressLint("SetTextI18n")
    public void renderPowerUp(Canvas canvas) {
        if (this.getRect().intersect(View.bird.getRect()) || this.x + this.width <= 0) {
            view.counter = 0;
            if (this.getRect().intersect(View.bird.getRect())) {
                if (bird.getScore() >= 0) {
                    switch (new Random().nextInt(6)) { //Bound can be smaller by 1 -> Isn't cause of initial (default) case.
                        case 0:
                            bird.setScore(bird.getScore() - 100);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            powerUpText.setText("-100 SCORE");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 1:
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 2500);
                            Values.gapPipe = (int) (SCREEN_HEIGHT / 4);
                            powerUpText.setText("Bigger gap");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 2:
                            bird.setCoins(bird.getCoins() + 5);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            powerUpText.setText("+5 coins");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 3:
                            bird.setScore(bird.getScore() + 200);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            powerUpText.setText("+200 score");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 4:
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 2500);
                            Values.gapPipe = (int) (SCREEN_HEIGHT / 6);
                            powerUpText.setText("Smaller gap");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        default: {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            powerUpText.setText("Nothing");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                        }
                    }
                }
            }
            //sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            this.setX((float) SCREEN_WIDTH);
            this.setY(new Random().nextInt(SCREEN_HEIGHT / 2 - MainActivity.grass.getHeight()) + (float) SCREEN_HEIGHT / 3);
        }

        this.x -= (speedPipe + (float) speedPipe / 2);
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

}

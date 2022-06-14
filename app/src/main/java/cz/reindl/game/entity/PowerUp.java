package cz.reindl.game.entity;

import static android.view.View.INVISIBLE;
import static cz.reindl.game.MainActivity.powerUpText;
import static cz.reindl.game.MainActivity.view;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.speedPipe;
import static cz.reindl.game.view.View.bird;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;

import java.util.Random;

import cz.reindl.game.MainActivity;
import cz.reindl.game.R;
import cz.reindl.game.values.Values;
import cz.reindl.game.view.View;

public class PowerUp extends GameObject {

    private boolean isPowerUp;

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
                    switch (new Random().nextInt(9)/*7*/) { //Bound can be smaller by 1 -> Isn't cause of initial (default) case.
                        case 0:
                            bird.setScore(bird.getScore() - 100);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isPowerUp = false;
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            isPowerUp = true;
                            powerUpText.setText("-100 SCORE");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 1:
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPowerUp = false;
                                            Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
                                            powerUpText.setAnimation(null);
                                            powerUpText.setVisibility(INVISIBLE);
                                        }
                                    }, 250);
                                    powerUpText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.blink));
                                }
                            }, 2250);
                            isPowerUp = true;
                            Values.gapPipe = (int) (SCREEN_HEIGHT / 4);
                            powerUpText.setText("Bigger gap");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 2:
                            bird.setCoins(bird.getCoins() + 5);
                            MainActivity.coinText.setText(String.valueOf(bird.getCoins()));
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isPowerUp = false;
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            isPowerUp = true;
                            powerUpText.setText("+5 coins");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 3:
                            bird.setScore(bird.getScore() + 200);
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isPowerUp = false;
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            isPowerUp = true;
                            powerUpText.setText("+200 score");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 4:
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPowerUp = false;
                                            Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
                                            powerUpText.setAnimation(null);
                                            powerUpText.setVisibility(INVISIBLE);
                                        }
                                    }, 250);
                                    powerUpText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.blink));
                                }
                            }, 2250);
                            isPowerUp = true;
                            Values.gapPipe = (int) (SCREEN_HEIGHT / 6);
                            powerUpText.setText("Smaller gap");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        case 5: {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPowerUp = false;
                                            Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
                                            powerUpText.setAnimation(null);
                                            powerUpText.setVisibility(INVISIBLE);
                                        }
                                    }, 250);
                                    powerUpText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.blink));
                                }
                            }, 500);
                            isPowerUp = true;
                            Values.speedPipe = 5 * SCREEN_WIDTH / 1080;
                            powerUpText.setText("Slowness");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        }
                        case 6: {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPowerUp = false;
                                            view.isDoublePoints = false;
                                            powerUpText.setAnimation(null);
                                            powerUpText.setVisibility(INVISIBLE);
                                        }
                                    }, 250);
                                    powerUpText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.blink));
                                }
                            }, 1250);
                            isPowerUp = true;
                            view.isDoublePoints = true;
                            powerUpText.setText("Double points");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        }
                        case 7: {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            isPowerUp = false;
                                            view.isCollisionImmunity = false;
                                            powerUpText.setAnimation(null);
                                            powerUpText.setVisibility(INVISIBLE);
                                            view.cnt = 3;
                                        }
                                    }, 500);
                                    if (view.isCollisionImmunity)
                                        powerUpText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.blink));
                                }
                            }, 2500);
                            isPowerUp = true;
                            view.isCollisionImmunity = true;
                            powerUpText.setText("Collision Immunity " + view.cnt + "sec");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        }
                        default: {
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isPowerUp = false;
                                    powerUpText.setVisibility(INVISIBLE);
                                }
                            }, 1000);
                            isPowerUp = true;
                            powerUpText.setText("Nothing");
                            powerUpText.setVisibility(android.view.View.VISIBLE);
                            break;
                        }
                    }
                }
            }
            // FIXME: 13.06.2022 New sound effect //sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            this.setX((float) SCREEN_WIDTH);
            this.setY(new Random().nextInt(SCREEN_HEIGHT / 2 - MainActivity.grass.getHeight()) + (float) SCREEN_HEIGHT / 3);
        }

        if (!isPowerUp) {
            this.x -= (speedPipe + (float) speedPipe / 2);
            canvas.drawBitmap(this.bitmap, this.x, this.y, null);
        }
    }

}

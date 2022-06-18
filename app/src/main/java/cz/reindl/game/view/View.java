package cz.reindl.game.view;

import static android.widget.Toast.makeText;
import static java.lang.Thread.sleep;
import static cz.reindl.game.MainActivity.*;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.utils.Utils.resizeBitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.reindl.game.MainActivity;
import cz.reindl.game.R;
import cz.reindl.game.entity.Coin;
import cz.reindl.game.entity.PowerUp;
import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.sound.Sound;

public class View extends android.view.View {

    public static final Sound sound = new Sound();

    public static boolean isActive;
    public static boolean isRunning = true;
    public static boolean isAlive = false;
    public boolean isHardCore = true;
    public boolean isDoublePoints, isBooster, isBoosterDone, isCollisionImmunity, direction, isDragon;

    public static Bird bird;
    public static Coin coin;
    public static PowerUp powerUp;

    public final Runnable runnable;
    public Handler handler;
    public int counter, duckX, duckCounter = 0;
    public int cnt = 3;
    public int barrierThroughCounter = 3;

    EventHandler eventHandler = new EventHandler();
    public final List<Barrier> barriers = new ArrayList();

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Values.TAG = "View - App start up", "Game view");

        //GAME OBJECTS INITIALIZATION
        initBarrier(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe));
        initBird();
        initCoin();
        initPowerUp();

        //Invalidating UI thread - view
        runnable = this::invalidate;
        handler = new Handler();

        //BG THREAD

        //SOUND INITIALIZATION
        sound.flapSound = sound.getSoundPool().load(context, R.raw.flap, 1);
        sound.collideSound = sound.getSoundPool().load(context, R.raw.bang, 2);
        sound.scoreSound = sound.getSoundPool().load(context, R.raw.score_sound, 1);
        sound.highScoreSound = sound.getSoundPool().load(context, R.raw.high_score, 1);
        sound.barrierCollideSound = sound.getSoundPool().load(context, R.raw.collide_fall, 1);
        sound.scytheFlap = sound.getSoundPool().load(context, R.raw.scythe_flap, 1);
        sound.reviveSound = sound.getSoundPool().load(context, R.raw.revive_sound, 1);

        //CODES
        Values.bonusCodes.add("verdysduck");
        Values.bonusCodes.add("pride");
    }

    public void initBarrier(Bitmap bitmap, Bitmap bitmap2) {
        Log.d(Values.TAG = "initBarrier", "Barriers init");
        barrierDistance = 775 * SCREEN_HEIGHT / 1920;

        Barrier barrier = new Barrier(resizeBitmap(bitmap, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(bitmap2, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), SCREEN_WIDTH, 1);
        Barrier barrier2 = new Barrier(resizeBitmap(bitmap, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(bitmap2, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -350);
        Barrier barrier3 = new Barrier(resizeBitmap(bitmap, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(bitmap2, 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance * 2, -200);

        barriers.add(barrier);
        barriers.add(barrier2);
        barriers.add(barrier3);
    }

    private void initCoin() {
        Log.d(Values.TAG = "View - initCoin", "Coin init");
        coin = new Coin(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.coin), 100 * SCREEN_WIDTH / 1080, 90 * SCREEN_HEIGHT / 1920), 600, 600);
    }

    private void initPowerUp() {
        Log.d(Values.TAG = "View - initPowerUp", "Power Up init");
        powerUp = new PowerUp(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.mega_nose1), 100 * SCREEN_WIDTH / 1080, 90 * SCREEN_HEIGHT / 1920), SCREEN_WIDTH, 500);
    }

    public void initBirdList() {
        Log.d(Values.TAG = "View - initBirdList", "Bird list init");
        ArrayList<Bitmap> birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skinup));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skindown));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_bird));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_bird_flap));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.mega_nose1));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.mega_nose2));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.dragon_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.dragon_down));
        bird.setBirdList(birdList);
    }

    private void initBird() {
        Log.d(Values.TAG = "View - initBird", "Bird init");
        bird = new Bird();
        bird.setHeight(105 * SCREEN_HEIGHT / 1920);
        bird.setWidth(105 * SCREEN_WIDTH / 1080);
        bird.setX((float) 100 * SCREEN_WIDTH / 1080);
        bird.setY((float) SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
        initBirdList();
        bird.setWidth(75 * SCREEN_WIDTH / 1080);
    }

    public void test() {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        final int FPS = 30;
        long targetTime = 1000 / FPS;

        while (isRunning) {
            startTime = System.nanoTime();

            timeMillis = (System.nanoTime() - startTime) / 100000;
            waitTime = targetTime - timeMillis;
            try {
                sleep(waitTime);
            } catch (Exception ignored) {
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                double averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("FPS: " + averageFPS);
            }
        }
    }

    //RENDER
    @SuppressLint("SetTextI18n")
    public void draw(Canvas canvas) {
        //LOOP
        /*if (duckButton.getAlpha() == 1f) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (duckButton.getX() + duckButton.getWidth() <= SCREEN_WIDTH && !direction) {
                        duckX += 10;
                    } else if (duckButton.getX() >= 0) {
                        duckButton.setBackgroundResource(R.drawable.duck2);
                        direction = true;
                        duckX -= 10;
                    } else {
                        duckButton.setBackgroundResource(R.drawable.duck);
                        direction = false;
                    }
                    if (duckCounter >= 20) {
                        duckButton.setX(duckX);
                        duckCounter = 0;
                    }
                    //duckButton.setTranslationX((float) SCREEN_WIDTH / 2 - (float) duckButton.getWidth() / 2); // FIXME: 13.06.2022 Just for now
                }
            });
            duckCounter++;
        }*/
        handler.postDelayed(runnable, 1);
        super.draw(canvas);
        if (isRunning) {
            for (int i = 0; i < barriers.size(); i++) {
                barriers.get(i).renderBarrier(canvas);
            }
            if (bird.getY() >= barriers.get(0).getY() + barriers.get(0).getWidth() && barrierThroughCounter > 0/* && isCollisionImmunity*/) {
                // FIXME: 13.06.2022 Test purposes eventHandler.resetGame();
                //barrierThroughCounter--;
            }
            /*if (barrierThroughCounter == 0) {
                isCollisionImmunity = false;
                powerUpText.setVisibility(INVISIBLE);
            }*/
            if (!isBooster) coin.renderCoin(canvas);
            if (!isHardCore) {
                counter++;
                if (counter >= 50 && !isBooster) {
                    powerUp.renderPowerUp(canvas);
                }
                if (isBooster) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isBooster = false;
                            if (isBoosterDone) {
                                isBoosterDone = false;
                                view.barriers.get(0).setX(SCREEN_WIDTH);
                                view.barriers.get(1).setX(view.barriers.get(0).getX() + barrierDistance);
                                view.barriers.get(2).setX(view.barriers.get(1).getX() + barrierDistance);
                                buttonStop.setVisibility(VISIBLE);
                                Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
                                bird.setFallGravity(-15);
                            }
                        }
                    }, 4000);
                    buttonStop.setVisibility(INVISIBLE);
                    isBoosterDone = true;
                    Values.speedPipe = 50 * SCREEN_WIDTH / 1080;
                    if (bird.getY() - bird.getHeight() > (float) SCREEN_HEIGHT / 2) {
                        bird.setFallGravity(-15);
                    }
                }
            }
            if (!isCollisionImmunity) {
                //barrierThroughCounter = 3;
                eventHandler.collision();
            } else if (bird.getY() + bird.getHeight() >= SCREEN_HEIGHT - grass.getHeight()) {
                eventHandler.resetGame();
            } else {
                eventHandler.checkHardCore();
                scoreText.setText(String.valueOf("Score: " + bird.getScore()));
                //powerUpText.setText("Collision Immunity " + view.cnt + "sec");
            }
        } else if (!isAlive) {
            if (bird.getY() - bird.getHeight() > (float) SCREEN_HEIGHT / 2) {
                bird.setFallGravity(-15);
            }
        } else if (bird.getY() /*+ bird.getHeight()*/ <= SCREEN_HEIGHT - grass.getHeight()) {
            for (int i = 0; i < barriers.size(); i++) {
                barriers.get(i).renderBarrier(canvas);
            }
            bird.setFallGravity(bird.getFallGravity() + 0.6f);
        } else {
            if (bird.getCoins() >= 50 && MainActivity.isRevived == 0) {
                eventHandler.checkContinuity();
            } else {
                eventHandler.resetValues();
            }
        }
        bird.renderBird(canvas);
    }

    //FLAP
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRunning && MainActivity.isGameStopped == 0) {
            if (!isAlive && !isBooster) {
                Log.d(Values.TAG = "View - onTouchEvent", "Bird flap");
                bird.setFallGravity(-15);
                if (sound.isSoundLoaded()) {
                    if (!Bird.legendarySkinUsing && !Bird.boughtSkinUsing && !isDragon) {
                        sound.getSoundPool().play(sound.scytheFlap, 0.3f, 0.3f, 1, 0, 1f);
                    } else {
                        sound.getSoundPool().play(sound.flapSound, 1f, 1f, 1, 0, 1f);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

}

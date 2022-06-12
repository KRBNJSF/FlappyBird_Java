package cz.reindl.game.view;

import static android.widget.Toast.makeText;
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
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public static Bird bird;
    public static Coin coin;
    public static PowerUp powerUp;

    public final Runnable runnable;
    public Handler handler;
    public int counter = 0;

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

    //RENDER
    public void draw(Canvas canvas) {
        //LOOP
        handler.postDelayed(runnable, 1);
        super.draw(canvas);
        if (isRunning) {
            for (int i = 0; i < barriers.size(); i++) {
                barriers.get(i).renderBarrier(canvas);
            }
            coin.renderCoin(canvas);
            if (!isHardCore) {
                counter++;
                if (counter >= 50) {
                    powerUp.renderPowerUp(canvas);
                }
            }
            eventHandler.collision();
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
            if (!isAlive) {
                Log.d(Values.TAG = "View - onTouchEvent", "Bird flap");
                bird.setFallGravity(-15);
                if (sound.isSoundLoaded()) {
                    if (!Bird.legendarySkinUsing && !Bird.boughtSkinUsing) {
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

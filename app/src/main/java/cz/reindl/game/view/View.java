package cz.reindl.game.view;

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

import cz.reindl.game.R;
import cz.reindl.game.event.EventCheck;
import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.sound.Sound;

public class View extends android.view.View {

    public static final Sound sound = new Sound();

    public static boolean isActive;
    public static boolean isHardCore = true;
    public static boolean isRunning = true;
    public static boolean isAlive = false;

    public static Bird bird;

    private final Runnable runnable;

    EventCheck eventCheck = new EventCheck();

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Values.TAG = "App start up", "Game view");

        //GAME OBJECTS INITIALIZATION
        initBarrier();
        initBird();

        //MAIN LOOP
        runnable = this::invalidate;

        //BG THREAD
        //scoreThread();

        //SOUND
        sound.flapSound = sound.getSoundPool().load(context, R.raw.flap, 1);
        sound.collideSound = sound.getSoundPool().load(context, R.raw.bang, 1);
        sound.scoreSound = sound.getSoundPool().load(context, R.raw.score_sound, 1);
        sound.highScoreSound = sound.getSoundPool().load(context, R.raw.high_score, 1);
        sound.barrierCollideSound = sound.getSoundPool().load(context, R.raw.collide_fall, 1);
        sound.scytheFlap = sound.getSoundPool().load(context, R.raw.scythe_flap, 1);
    }

    private void scoreThread() {
        Runnable r = () -> {
            boolean flag = true;
            int i = 0;
            while (flag) {
                i++;
                System.out.println("Thread started... Counter ==> " + i);
                try {
                    //checkScore();
                    //collision();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);

        t.start();
        // t.run(); // is going to execute the code in the thread's run method on the current thread
        System.out.println("Program Exited\n");
    }

    private void initBarrier() {
        Log.d(Values.TAG = "initBarrier", "Barriers init");
        barrierDistance = 700 * SCREEN_HEIGHT / 1920;

        //Blue barrier is the first one in ArrayList, Red are the others
        Barrier barrier = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), SCREEN_WIDTH, 0);
        Barrier barrier2 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -350);
        Barrier barrier3 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance * 2, -200);

        eventCheck.barriers.add(barrier);
        eventCheck.barriers.add(barrier2);
        eventCheck.barriers.add(barrier3);
    }

    public void checkSkin() {
        Bird.skinUnlocked = sharedPreferences.getBoolean("skinUnlocked", Bird.skinUnlocked);
        if (sharedPreferences.getInt("highScore", bird.getHighScore()) >= 10000) {
            Bird.skinUnlocked = true;
        }
    }

    private void initBird() {
        Log.d(Values.TAG = "initBird", "Bird init");
        bird = new Bird();
        bird.setHeight(105 * SCREEN_HEIGHT / 1920);
        bird.setWidth(105 * SCREEN_WIDTH / 1080);
        bird.setX((float) 100 * SCREEN_WIDTH / 1080);
        bird.setY((float) SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);

        ArrayList<Bitmap> birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skinup));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skindown));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        bird.setBirdList(birdList);
    }

    //RENDER
    public void draw(Canvas canvas) {
        checkSkin();
        new Handler().postDelayed(runnable, 1);
        super.draw(canvas);
        if (isRunning) {
            for (int i = 0; i < eventCheck.barriers.size(); i++) {
                eventCheck.barriers.get(i).renderBarrier(canvas);
            }
            eventCheck.collision();
        } else if (!isAlive) {
            if (bird.getY() - bird.getHeight() > (float) SCREEN_HEIGHT / 2) {
                bird.setGravity(-15);
            }
        } else if (bird.getY() <= SCREEN_HEIGHT) {
            for (int i = 0; i < eventCheck.barriers.size(); i++) {
                eventCheck.barriers.get(i).renderBarrier(canvas);
            }
            bird.setGravity(15);
        } else {
            eventCheck.resetGame();
        }
        bird.renderBird(canvas);
    }

    //FLAP
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRunning) {
            if (!isAlive) {
                Log.d(Values.TAG = "onTouchEvent", "Bird flap");
                bird.setGravity(-15);
                if (sound.isSoundLoaded()) {
                    if (!Bird.changeSkin) {
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

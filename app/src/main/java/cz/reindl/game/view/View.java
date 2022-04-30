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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import cz.reindl.game.R;
import cz.reindl.game.entity.Coin;
import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.sound.Sound;

public class View extends android.view.View {

    public static final Sound sound = new Sound();

    public static boolean isActive;
    public boolean isHardCore = true;
    public static boolean isRunning = true;
    public static boolean isAlive = false;

    public static Bird bird;
    public static Coin coin;

    private final Runnable runnable;

    EventHandler eventHandler = new EventHandler();

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Values.TAG = "App start up", "Game view");

        //GAME OBJECTS INITIALIZATION
        initBarrier();
        initBird();
        initCoin();
        //checkBirdSkin();

        //MAIN LOOP
        runnable = this::invalidate;

        //BG THREAD
        //scoreThread();

        //SOUND
        sound.flapSound = sound.getSoundPool().load(context, R.raw.flap, 1);
        sound.collideSound = sound.getSoundPool().load(context, R.raw.bang, 2);
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
        barrierDistance = 750 * SCREEN_HEIGHT / 1920;

        //Blue barrier is the first one in ArrayList, Red are the others
        Barrier barrier = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), SCREEN_WIDTH, 1);
        Barrier barrier2 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -350);
        Barrier barrier3 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance * 2, -200);

        eventHandler.barriers.add(barrier);
        eventHandler.barriers.add(barrier2);
        eventHandler.barriers.add(barrier3);
    }

    private void initCoin() {
        Log.d(Values.TAG = "initCoin", "Coin init");
        coin = new Coin(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.coin), 100 * SCREEN_WIDTH / 1080, 90 * SCREEN_HEIGHT / 1920), 600, 600);
    }

    public void initBirdList() {
        ArrayList<Bitmap> birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skinup));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.legendary_skindown));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_bird));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_bird_flap));
        bird.setBirdList(birdList);
    }

    private void initBird() {
        Log.d(Values.TAG = "initBird", "Bird init");
        bird = new Bird();
        bird.setHeight(105 * SCREEN_HEIGHT / 1920);
        bird.setWidth(105 * SCREEN_WIDTH / 1080);
        bird.setX((float) 100 * SCREEN_WIDTH / 1080);
        bird.setY((float) SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
        initBirdList();
    }

    //RENDER
    public void draw(Canvas canvas) {
        new Handler().postDelayed(runnable, 1);
        super.draw(canvas);
        if (isRunning) {
            for (int i = 0; i < eventHandler.barriers.size(); i++) {
                eventHandler.barriers.get(i).renderBarrier(canvas);
            }
            coin.renderCoin(canvas);
            eventHandler.collision();
        } else if (!isAlive) {
            if (bird.getY() - bird.getHeight() > (float) SCREEN_HEIGHT / 2) {
                bird.setGravity(-15);
            }
        } else if (bird.getY() + bird.getHeight() <= SCREEN_HEIGHT - grass.getHeight()) {
            for (int i = 0; i < eventHandler.barriers.size(); i++) {
                eventHandler.barriers.get(i).renderBarrier(canvas);
            }
            bird.setGravity(18);
        } else {
            eventHandler.resetGame();
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

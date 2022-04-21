package cz.reindl.game.view;

import static android.widget.Toast.makeText;
import static cz.reindl.game.MainActivity.*;
import static cz.reindl.game.constants.Constants.SCREEN_HEIGHT;
import static cz.reindl.game.constants.Constants.SCREEN_WIDTH;
import static cz.reindl.game.constants.Constants.barrierDistance;
import static cz.reindl.game.utils.Utils.resizeBitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cz.reindl.game.R;
import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.sound.Sound;
import cz.reindl.game.utils.Utils;

public class View extends android.view.View {

    List<Barrier> barriers = new ArrayList();
    public static Barrier barrier, barrier2, barrier3;
    ArrayList<Bitmap> birdList;
    private Bird bird;

    Sound sound = new Sound();

    private Runnable runnable;


    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Constants.TAG = "App start up", "Game view");

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
    }

    private void scoreThread() {
        Runnable r = new Runnable() {
            public void run() {
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
            }
        };
        Thread t = new Thread(r);

        t.start();
        // t.run(); // is going to execute the code in the thread's run method on the current thread
        System.out.println("Program Exited\n");
    }

    private void initBarrier() {
        Log.d(Constants.TAG = "initBarrier", "Barriers init");
        barrierDistance = 700 * SCREEN_HEIGHT / 1920;

        barriers = new ArrayList();

        barrier = new Barrier(Utils.addBorder(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), 2, "blue"), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), SCREEN_WIDTH, 0);
        barrier2 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -250);
        barrier3 = new Barrier(resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), resizeBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * SCREEN_WIDTH / 1080, SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance * 2, 350);

        barriers.add(barrier);
        barriers.add(barrier2);
        barriers.add(barrier3);
    }

    private void initBird() {
        Log.d(Constants.TAG = "initBird", "Bird init");
        bird = new Bird();
        bird.setHeight(100 * SCREEN_HEIGHT / 1920);
        bird.setWidth(100 * SCREEN_WIDTH / 1080);
        bird.setX(100 * SCREEN_WIDTH / 1080);
        bird.setY(SCREEN_HEIGHT / 2 - bird.getHeight() / 2);

        birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        bird.setBirdList(birdList);
    }

    //COLLISION CHECK
    public void collision() {
        for (int i = 0; i < barriers.size(); i++) {
            if (bird.getX() > (barriers.get(i).getX()) && bird.getY() < barriers.get(i).getY() + (SCREEN_HEIGHT / 2 - Constants.gapPipe) || bird.getY() >= SCREEN_HEIGHT) {
                System.out.println(barriers.get(i).getX());
                resetGame();
            } else if (bird.getY() > SCREEN_HEIGHT / 2 + Constants.gapPipe + barriers.get(i).getY() && bird.getX() > barriers.get(i).getX()) {
                resetGame();
            } else if (bird.getX() > barrier.getX()) {

            }
            // FIXME: 21.04.2022
            //  sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            bird.setScore(bird.getScore() + 1);
            scoreText.setText(String.valueOf("Score: " + bird.getScore()));
        }
    }


    //SCORE CHECK
    public void checkScore() {
        if (bird.getScore() > 1000 && bird.getScore() < 2000) {
            Constants.speedPipe = 5 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 2000 && bird.getScore() < 3000) {
            Constants.speedPipe = 6 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 3000 && bird.getScore() < 4000) {
            Constants.speedPipe = 7 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 4000 && bird.getScore() < 5000) {
            Constants.speedPipe = 8 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 5000) {
            Constants.speedPipe = 9 * SCREEN_WIDTH / 1080;
        }

        /*
        if (bird.getX() > barrier.getX() + barrier.getWidth() && bird.getX() < barrier.getX() + barrier.getWidth() + barrierDistance) {
            bird.setScore(bird.getScore() + 1);
            Log.d(Constants.TAG, "Score: " + bird.getScore());
        }*/

        if (bird.getScore() > bird.getHighScore()) {
            bird.setHighScore(bird.getScore());
            //Log.d(Constants.TAG = "checkScore", "new high score");
        } else {
            bird.setHighScore(sharedPreferences.getInt("highScore", bird.getHighScore()));
        }
        highScoreText.setText(String.valueOf("High Score: " + bird.getHighScore()));
    }

    public void resetGame() {
        if (bird.getScore() > bird.getHighScore()) {
            sound.getSoundPool().play(sound.highScoreSound, 1f, 1f, 1, 0, 1f);
        }
        sound.getSoundPool().play(sound.collideSound, 1f, 1f, 1, 0, 1f);
        editor.putInt("highScore", bird.getHighScore());
        editor.commit();
        /*if (!prepare) {
            Looper.prepare();
            prepare = true;
        }*/
        makeText(highScoreText.getContext(), "Score saved", Toast.LENGTH_SHORT).show();
        Log.d(Constants.TAG = "resetGame", "Game reset");
        bird.setScore(0);
        bird.setY(SCREEN_HEIGHT - this.getHeight() / 2);
        bird.setGravity(0.6f);
        barriers.get(0).setX(SCREEN_WIDTH);
        barriers.get(1).setX(barriers.get(0).getX() + barrierDistance);
        barriers.get(2).setX(barriers.get(1).getX() + barrierDistance);
        Constants.speedPipe = 4 * SCREEN_WIDTH / 1080;
    }

    //RENDER
    public void draw(Canvas canvas) {
        new Handler().postDelayed(runnable, 1);
        super.draw(canvas);
        bird.renderBird(canvas);
        for (int i = 0; i < barriers.size(); i++) {
            barriers.get(i).renderBarrier(canvas);
        }
        checkScore();
        collision();
    }

    //FLAP
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(Constants.TAG = "onTouchEvent", "Bird flap");
        bird.setGravity(-15);
        if (sound.isSoundLoaded()) {
            sound.getSoundPool().play(sound.flapSound, 1f, 1f, 1, 0, 1f);
        }
        return super.onTouchEvent(event);
    }

}

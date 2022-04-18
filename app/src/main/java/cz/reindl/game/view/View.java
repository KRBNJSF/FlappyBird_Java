package cz.reindl.game.view;

import static android.widget.Toast.makeText;
import static cz.reindl.game.MainActivity.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
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

public class View extends android.view.View {

    List<Barrier> barriers = new ArrayList();
    ArrayList<Bitmap> birdList;
    private Bird bird;
    public static Barrier barrier, barrier2, barrier3;
    public static int barrierDistance;
    private Runnable runnable;
    private boolean prepare;

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Constants.TAG = "App start up", "Game view");

        //GAME OBJECTS RENDER
        initBarrier();
        initBird();
        //Updating draw()
        runnable = this::invalidate;
        scoreThread();
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
        // Lets run Thread in background..
        // Sometimes you need to run thread in background for your Timer application..
        t.start(); // starts thread in background..
        // t.run(); // is going to execute the code in the thread's run method on the current thread..
        System.out.println("Main() Program Exited...\n");
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void initBarrier() {
        Log.d(Constants.TAG = "initBarrier", "Barriers init");
        barriers = new ArrayList();
        barrierDistance = 600 * Constants.SCREEN_HEIGHT / 1920;
        barrier = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), 400 * Constants.SCREEN_WIDTH / 1080, 0);
        barrier2 = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -250);
        barrier3 = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), barrier2.getX() + barrierDistance, 350);
        barriers.add(barrier);
        barriers.add(barrier2);
        barriers.add(barrier3);
    }

    private void initBird() {
        Log.d(Constants.TAG = "initBird", "Bird init");
        bird = new Bird(); //Bird attributes for phones
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setY(Constants.SCREEN_HEIGHT / 2 - bird.getHeight() / 2);
        birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        bird.setBirdList(birdList);
    }

    public void collision() {
        for (int i = 0; i < barriers.size(); i++) {
            if (bird.getX() > (barriers.get(i).getX() - 60) && bird.getY() < barriers.get(i).getY() + (Constants.SCREEN_HEIGHT / 2 - Constants.gapPipe) || bird.getY() >= Constants.SCREEN_HEIGHT) {
                System.out.println(barriers.get(i).getX());
                resetGame();
            } else if (bird.getY() > Constants.SCREEN_HEIGHT / 2 + Constants.gapPipe + barriers.get(i).getY() - 70 && bird.getX() > barriers.get(i).getX() - 70) {
                resetGame();
            }
            bird.setScore(bird.getScore() + 1);
            scoreText.setText(String.valueOf("Score: " + bird.getScore()));
        }
    }

    public void checkScore() {
        if (bird.getScore() > 1000 && bird.getScore() < 2000) {
            //Log.d(Constants.TAG = "checkScore", "Sped up game to 7");
            Constants.speedPipe = 5 * Constants.SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 2000 && bird.getScore() < 3000) {
            //Log.d(Constants.TAG = "checkScore", "Sped up game to 8");
            Constants.speedPipe = 6 * Constants.SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 3000 && bird.getScore() < 4000) {
            //Log.d(Constants.TAG = "checkScore", "Sped up game to 9");
            Constants.speedPipe = 7 * Constants.SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 4000 && bird.getScore() < 5000) {
            //Log.d(Constants.TAG = "checkScore", "Sped up game to 10");
            Constants.speedPipe = 8 * Constants.SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 5000) {
            //Log.d(Constants.TAG = "checkScore", "Sped up game to 11");
            Constants.speedPipe = 9 * Constants.SCREEN_WIDTH / 1080;
        }

        if (bird.getScore() > bird.getHighScore()) {
            bird.setHighScore(bird.getScore());
            //Log.d(Constants.TAG = "checkScore", "new high score");
        } else {
            bird.setHighScore(sharedPreferences.getInt("highScore", bird.getHighScore()));
        }
        highScoreText.setText(String.valueOf("High Score: " + bird.getHighScore()));
    }

    public static Bitmap addBorder(Bitmap bitmap, int borderSize) {
        Bitmap borderBitmap = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas canvas = new Canvas(borderBitmap);
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);
        return borderBitmap;
    }

    public void resetGame() {
        editor.putInt("highScore", bird.getHighScore());
        editor.commit();
        /*if (!prepare) {
            Looper.prepare();
            prepare = true;
        }*/
        makeText(highScoreText.getContext(), "Score saved", Toast.LENGTH_SHORT).show();
        Log.d(Constants.TAG = "resetGame", "Game reset");
        bird.setScore(0);
        bird.setY(Constants.SCREEN_HEIGHT - this.getHeight() / 2);
        bird.setGravity(0.6f);
        barriers.get(0).setX(Constants.SCREEN_WIDTH);
        barriers.get(1).setX(barriers.get(0).getX() + barrierDistance);
        barriers.get(2).setX(barriers.get(1).getX() + barrierDistance);
        Constants.speedPipe = 4 * Constants.SCREEN_WIDTH / 1080;
    }

    public void draw(Canvas canvas) {
        new Handler().postDelayed(runnable, 1);
        double frameTime = SystemClock.elapsedRealtime();
        //System.out.println("FPS: " + 1000.0 / frameTime);
        super.draw(canvas);
        bird.renderBird(canvas);
        for (int i = 0; i < barriers.size(); i++) {
            barriers.get(i).renderBarrier(canvas);
        }
        checkScore();
        collision();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(Constants.TAG = "onTouchEvent", "Bird flap");
        bird.setGravity(-15);
        return super.onTouchEvent(event);
    }

}

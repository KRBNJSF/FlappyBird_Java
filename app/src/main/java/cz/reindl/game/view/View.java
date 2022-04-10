package cz.reindl.game.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.BreakIterator;
import java.util.ArrayList;

import cz.reindl.game.R;
import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;

public class View extends android.view.View {

    ArrayList<Barrier> barriers = new ArrayList<Barrier>();
    private Bird bird;
    public static Barrier barrier, barrier2, barrier3;
    public static int score, barrierDistance;
    private android.os.Handler handler;
    private Runnable runnable;
    private BreakIterator scoreText;

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Constants.TAG = "App start up", "Game view");

        //GAME OBJECTS RENDER
        initBarrier();
        initBird();
        //GAME LOOP
        handler = new Handler();
        //Updating draw()
        runnable = this::invalidate;
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
        barrierDistance = 600 * Constants.SCREEN_HEIGHT / 1920;
        barrier = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, 8 * Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), 400 * Constants.SCREEN_WIDTH / 1080, 0);
        barrier2 = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, 8 * Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), barrier.getX() + barrierDistance, -250);
        barrier3 = new Barrier(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200 * Constants.SCREEN_WIDTH / 1080, 8 * Constants.SCREEN_HEIGHT), getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe), 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2), barrier2.getX() + barrierDistance, 350);
    }

    private void initBird() {
        bird = new Bird(); //Bird attributes for phones
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setY(Constants.SCREEN_HEIGHT / 2 - bird.getHeight() / 2);
        ArrayList<Bitmap> birdList = new ArrayList<>();
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_h));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        bird.setBirdList(birdList);
    }

    public void collision() {
        if (bird.getY() < barrier.getY() + (Constants.SCREEN_HEIGHT / 2) - Constants.gapPipe && bird.getX() > barrier.getX() && bird.getX() < barrier.getX()) {
            System.out.println("Birderer: " + bird.getX() + " Piperer: " + barrier.getX());
            resetGame();
        }
    }

    public void resetGame() {
        bird.setY(Constants.SCREEN_HEIGHT / 2 - this.getHeight() / 2);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        bird.draw(canvas);
        barrier.draw(canvas);
        barrier2.draw(canvas);
        barrier3.draw(canvas);
        collision();
        handler.postDelayed(runnable, 10);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bird.setGravity(-13);
        bird.setScore(bird.getScore() + 1);
        scoreText.setText(String.valueOf(bird.getScore()));
        return super.onTouchEvent(event);
    }

}

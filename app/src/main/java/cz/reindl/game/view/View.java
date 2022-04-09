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

import java.util.ArrayList;

import cz.reindl.game.R;
import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;

public class View extends android.view.View {

    public TextView scoreText = (TextView) findViewById(R.id.scoreTextView);
    ArrayList<Barrier> barriers = new ArrayList<Barrier>();
    private Bird bird;
    public static Barrier barrier, barrier2;
    private int score, barrierDistance, idPipe;
    private android.os.Handler handler;
    private Runnable runnable;

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Constants.TAG = "App start up", "view");

        //GAME OBJECTS RENDER
        initBarrier();
        initBird();
        //GAME LOOP
        handler = new Handler();
        //Updating draw()
        runnable = this::invalidate;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight, int x, int y) {
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
                Bitmap.createBitmap(bm, x, y, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private void initBarrier() {
        barrierDistance = 300 * Constants.SCREEN_HEIGHT / 1920;

        barrier2 = new Barrier(Constants.SCREEN_WIDTH / 1080, 0, 200 * Constants.SCREEN_WIDTH / 1080, 200 * Constants.SCREEN_HEIGHT / 2, BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe));

        System.out.println("Ahoj " + barrier2.getHeight());
        barrier = new Barrier(Constants.SCREEN_WIDTH / 1080, barrier2.getHeight(), 200 * Constants.SCREEN_WIDTH / 1080, 200 * Constants.SCREEN_HEIGHT / 2, BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe));
        //barrier.setBitmap(getResizedBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe), 200, 200, 0, 90));//barrier2.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe));

        //barriers.add(new Barrier(Constants.SCREEN_WIDTH / 1080, 0, 200 * Constants.SCREEN_WIDTH / 1080, 200 * Constants.SCREEN_HEIGHT / 2));
        //barriers.get(0).setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe));
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

    public void draw(Canvas canvas) {
        super.draw(canvas);
        bird.draw(canvas);
        barrier.draw(canvas);
        barrier2.draw(canvas);
        //Log.d(Constants.TAG = "Draw", "Render"); //Render logs
        handler.postDelayed(runnable, 10); //Render every 10 mils
        //canvas.drawColor(Color.RED);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        bird.setGravity(-13);
        bird.setScore(bird.getScore() + 1);
        return super.onTouchEvent(event);
    }

}

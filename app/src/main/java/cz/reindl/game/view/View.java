package cz.reindl.game.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    private void initBarrier() {
        barrierDistance = 300 * Constants.SCREEN_HEIGHT / 1920;

        barrier = new Barrier(Constants.SCREEN_WIDTH / 1080, 0, 200 * Constants.SCREEN_WIDTH / 1080, 200 * Constants.SCREEN_HEIGHT / 2);
        barrier.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe));

        barrier2 = new Barrier(Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT, 200 * Constants.SCREEN_WIDTH / 1080, Constants.SCREEN_HEIGHT / 2);
        barrier2.setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.bottom_pipe));

        barriers.add(new Barrier(Constants.SCREEN_WIDTH / 1080, 0, 200 * Constants.SCREEN_WIDTH / 1080, 200 * Constants.SCREEN_HEIGHT / 2));
        barriers.get(0).setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.top_pipe));
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

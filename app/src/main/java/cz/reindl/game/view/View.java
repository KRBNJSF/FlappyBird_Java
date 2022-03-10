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
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.reindl.game.R;
import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Bird;

public class View extends android.view.View {

    private Bird bird;
    private int score;
    private android.os.Handler handler;
    private Runnable runnable;

    public View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(Constants.TAG = "App start up", "view");
        //BIRD RENDER
        bird = new Bird(); //Bird attributes for phones
        bird.setWidth(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setHeight(100 * Constants.SCREEN_HEIGHT / 1920);
        bird.setX(100 * Constants.SCREEN_WIDTH / 1080);
        bird.setY(Constants.SCREEN_HEIGHT / 2 - bird.getHeight() / 2);

        ArrayList<Bitmap> birdList = new ArrayList<>(); //Adding images to Bitmap ArrayList
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_horizontal));
        birdList.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.bird_scythe_up));
        bird.setBirdList(birdList);

        //GAME LOOP
        handler = new Handler();
        //Updating draw()
        runnable = this::invalidate;
    }

    public void draw(Canvas canvas) { //Rendering background using canvas
        super.draw(canvas);
        bird.draw(canvas);
        //Log.d(Constants.TAG = "Draw", "Render"); //Render logs
        handler.postDelayed(runnable, 10); //Render every 10 mils
        //canvas.drawColor(Color.RED);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-13);
            System.out.println("Score: " + ++score);
        }
        return true;
    }

}

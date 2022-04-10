package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.view.View;

public class Barrier extends GameObject {

    private ArrayList<Bitmap> barriers = new ArrayList<>();

    public Barrier(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        super(bitmap, bitmap2, x, y);
        Constants.speedPipe = 6 * Constants.SCREEN_WIDTH / 1080;
    }

    public void draw(Canvas canvas) {
        if (x < -200 * Constants.SCREEN_WIDTH / 1080) {
            this.setX(Constants.SCREEN_WIDTH + View.barrierDistance);
            this.setY(new Random().nextInt(1400) - 700);
        }
        this.x -= Constants.speedPipe;
        canvas.drawBitmap(this.bitmap, this.x, ((Constants.SCREEN_HEIGHT / 2) + (Constants.gapPipe)) + this.y, null);
        canvas.drawBitmap(this.bitmap2, this.x, -(Constants.gapPipe) + this.y, null);
    }

}

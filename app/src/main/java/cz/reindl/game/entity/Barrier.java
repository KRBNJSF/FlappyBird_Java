package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.view.View;

public class Barrier extends GameObject {

    public Barrier(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        super(bitmap, bitmap2, x, y);
        Constants.speedPipe = 4 * Constants.SCREEN_WIDTH / 1080;
    }

    public void renderBarrier(Canvas canvas) {
        if (x < -200 * Constants.SCREEN_WIDTH / 1080) {
            this.setX(Constants.SCREEN_WIDTH + View.barrierDistance);
            this.setY(new Random().nextInt(1000) - 500);
        }
        this.x -= Constants.speedPipe;
        canvas.drawBitmap(View.addBorder(this.bitmap, 3), this.x, ((Constants.SCREEN_HEIGHT / 2) + (Constants.gapPipe)) + this.y, null);
        canvas.drawBitmap(this.bitmap2, this.x, -(Constants.gapPipe) + this.y, null);
    }

}

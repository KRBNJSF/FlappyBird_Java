package cz.reindl.game.entity;

import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.values.Values.speedPipe;
import static cz.reindl.game.utils.Utils.addBorder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import cz.reindl.game.values.Values;

public class Barrier extends GameObject {

    public Barrier(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        super(bitmap, bitmap2, x, y);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        speedPipe = 9 * SCREEN_WIDTH / 1080;
    }

    public void renderBarrier(Canvas canvas) {
        if (x + bitmap.getWidth() < 0) {
            this.setX(SCREEN_WIDTH + barrierDistance + ((float) barrierDistance / 2));
            this.setY(new Random().nextInt(645) - 620);
        }

        this.x -= speedPipe;
        canvas.drawBitmap(this.bitmap2, this.x, this.y, null);
        canvas.drawBitmap(addBorder(this.bitmap, 0, "red"), this.x, (((float) this.getHeight()) + this.getY() + (Values.gapPipe)), null);
    }

}

package cz.reindl.game.entity;

import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.values.Values.gapPipe;
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
        speedPipe = 8 * SCREEN_WIDTH / 1080;
    }

    public void renderBarrier(Canvas canvas) {
        if (x + bitmap.getWidth() < 0) {
            /* if (!(this.yy - this.y == Constants.gapPipe)) {
                this.setY(new Random().nextInt(1000) - 1000);
                this.yy = this.y + gapPipe;
            } */
            this.setX(SCREEN_WIDTH + barrierDistance + 200);
            this.setY(new Random().nextInt(1000) - 1000);
            this.yy = this.y + gapPipe;
            //setRect(this.height).offset(-200, -100);
        }

        this.x -= speedPipe;
        canvas.drawBitmap(this.bitmap2, this.x, this.y, null);
        canvas.drawBitmap(addBorder(this.bitmap, 1, "red"), this.x, (((float) this.getHeight()) + (Values.gapPipe)), null);
    }

}

package cz.reindl.game.entity;

import static cz.reindl.game.constants.Constants.SCREEN_HEIGHT;
import static cz.reindl.game.constants.Constants.SCREEN_WIDTH;
import static cz.reindl.game.constants.Constants.barrierDistance;
import static cz.reindl.game.constants.Constants.speedPipe;
import static cz.reindl.game.utils.Utils.addBorder;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.utils.Utils;
import cz.reindl.game.view.View;

public class Barrier extends GameObject {

    public Barrier(Bitmap bitmap, Bitmap bitmap2, float x, float y) {
        super(bitmap, bitmap2, x, y);
        speedPipe = 4 * SCREEN_WIDTH / 1080;
    }

    public void renderBarrier(Canvas canvas) {
        if (x < (float) -200 * SCREEN_WIDTH / 1080) {
            this.setX(SCREEN_WIDTH + barrierDistance + 300);
            this.setY(new Random().nextInt(1000) - 500);
        }
        this.x -= speedPipe;
        canvas.drawBitmap(this.bitmap2, this.x, -(Constants.gapPipe) + this.y, null);
        canvas.drawBitmap(addBorder(this.bitmap, 1, "red"), this.x, (((float) SCREEN_HEIGHT / 2) + (Constants.gapPipe)) + this.y, null);
    }

}

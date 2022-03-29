package cz.reindl.game.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

import cz.reindl.game.constants.Constants;

public class Barrier extends GameObject {

    private ArrayList<Bitmap> barriers = new ArrayList<>();

    public Barrier(float x, float y, int width, int height) {
        super(x, y, width, height);
        Constants.speedPipe = 6 * Constants.SCREEN_WIDTH / 1080; //Speed of the pipe according to screen width
    }

    public void draw(Canvas canvas) {
        if (x < this.getWidth()) {
            this.setX(Constants.SCREEN_WIDTH);
        }
        this.x -= Constants.speedPipe; //Descending position of the pipe in the X axis
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

}

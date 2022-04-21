package cz.reindl.game.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

public class Utils {

    public static int randomNumber(int value) {
        return new Random().nextInt(value);
    }

    public static Bitmap addBorder(Bitmap bitmap, int borderSize) {
        Bitmap borderBitmap = Bitmap.createBitmap(bitmap.getWidth() + borderSize * 2, bitmap.getHeight() + borderSize * 2, bitmap.getConfig());
        Canvas canvas = new Canvas(borderBitmap);
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(bitmap, borderSize, borderSize, null);
        return borderBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bmp, int width, int height) {
        return Bitmap.createScaledBitmap(bmp, width, height, false);
    }

}

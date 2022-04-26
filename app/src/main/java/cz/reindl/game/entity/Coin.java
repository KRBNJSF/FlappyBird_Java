package cz.reindl.game.entity;

import static cz.reindl.game.utils.Utils.addBorder;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.values.Values.speedPipe;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.sound;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import cz.reindl.game.MainActivity;
import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.view.View;

public class Coin extends GameObject {

    public Coin(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void renderCoin(Canvas canvas) {
        if (this.getRect().intersect(View.bird.getRect())) {
            sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            bird.setCoins(bird.getCoins() + 1);
            MainActivity.coinText.setText(String.valueOf(bird.getCoins()));
            this.setX(new Random().nextInt(SCREEN_WIDTH) + (float) SCREEN_WIDTH / 2);
            this.setY(new Random().nextInt(SCREEN_HEIGHT - MainActivity.grass.getHeight()) + 1);
        }

        this.x -= speedPipe;
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

}

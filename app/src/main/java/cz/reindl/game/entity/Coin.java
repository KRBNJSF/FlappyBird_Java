package cz.reindl.game.entity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static cz.reindl.game.MainActivity.coinGetText;
import static cz.reindl.game.MainActivity.powerUpText;
import static cz.reindl.game.utils.Utils.addBorder;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.values.Values.speedPipe;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.coin;
import static cz.reindl.game.view.View.sound;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;

import java.util.Random;

import cz.reindl.game.MainActivity;
import cz.reindl.game.R;
import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.view.View;

public class Coin extends GameObject {

    private int velocity = 0;

    public Coin(Bitmap bitmap, int x, int y) {
        super(bitmap, x, y);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.y = y;
        this.x = x;
    }

    public void renderCoin(Canvas canvas) {
        velocity += 4;
        coinGetText.setY(bird.getY() - bird.getHeight() - velocity);
        if (this.getRect().intersect(View.bird.getRect())) {
            velocity = 0;
            coinGetText.setVisibility(android.view.View.VISIBLE);
            coinGetText.setX(this.x + bird.getWidth());
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (coinGetText.getVisibility() == VISIBLE) {
                        coinGetText.setAnimation(null);
                        coinGetText.setVisibility(INVISIBLE);
                    }
                    velocity = 0;
                }
            }, 500);
            coinGetText.setAnimation(AnimationUtils.loadAnimation(powerUpText.getContext(), R.anim.disappear));
            coinGetText.setText(String.valueOf("+1"));
            sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            bird.setCoins(bird.getCoins() + 1);
            MainActivity.coinText.setText(String.valueOf(bird.getCoins()));
            this.setX(new Random().nextInt(SCREEN_WIDTH) + (float) SCREEN_WIDTH / 2);
            this.setY(new Random().nextInt(SCREEN_HEIGHT / 2 - MainActivity.grass.getHeight()) + (float) SCREEN_HEIGHT / 3);
        }

        this.x -= speedPipe;
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

}

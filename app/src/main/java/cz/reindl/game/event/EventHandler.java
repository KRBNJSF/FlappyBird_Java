package cz.reindl.game.event;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.makeText;
import static cz.reindl.game.MainActivity.currentMusic;
import static cz.reindl.game.MainActivity.editor;
import static cz.reindl.game.MainActivity.gameOverText;
import static cz.reindl.game.MainActivity.grass;
import static cz.reindl.game.MainActivity.highScoreText;
import static cz.reindl.game.MainActivity.relativeLayout;
import static cz.reindl.game.MainActivity.restartButton;
import static cz.reindl.game.MainActivity.scoreText;
import static cz.reindl.game.MainActivity.sharedPreferences;
import static cz.reindl.game.MainActivity.view;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.isActive;
import static cz.reindl.game.view.View.isAlive;
import static cz.reindl.game.view.View.isRunning;
import static cz.reindl.game.view.View.sound;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.reindl.game.MainActivity;
import cz.reindl.game.R;
import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.values.Values;
import cz.reindl.game.view.View;

public class EventHandler {

    MainActivity mainActivity = new MainActivity();

    public final List<Barrier> barriers = new ArrayList();

    //COLLISION CHECK
    public void collision() {
        checkHighScore();
        for (int i = 0; i < barriers.size(); i++) {
            if (View.coin.getX() + View.coin.getBitmap().getWidth() < 0 || View.coin.getRect().intersect(barriers.get(i).getRect()) || View.coin.getRect().intersect(barriers.get(i).getBottomPipeRect())) {
                View.coin.setX((SCREEN_WIDTH) + (float) SCREEN_WIDTH / 2); //barriers.get(i).getX() - (new Random().nextInt(100) + coin.getWidth())
                View.coin.setY(new Random().nextInt(SCREEN_HEIGHT / 2 - MainActivity.grass.getHeight()) + (float) SCREEN_HEIGHT / 3);
            }
            if (bird.getRect().intersect(barriers.get(i).getRect()) || bird.getY() + bird.getHeight() >= SCREEN_HEIGHT - grass.getHeight() || bird.getRect().intersect(barriers.get(i).getBottomPipeRect())) {
                resetGame();
            }
        }
        if (!view.isHardCore) {
            bird.setScore(bird.getScore() + 3);
        } else {
            bird.setScore(bird.getScore() + 1);
        }
        scoreText.setText(String.valueOf("Score: " + bird.getScore()));
    }

    //HIGH SCORE CHECK
    private void checkHighScore() {
        if (MainActivity.view.isHardCore) {
            gameSpeedUp();
        }

        if (bird.getScore() > bird.getHighScore()) {
            bird.setHighScore(bird.getScore());
            Log.d(Values.TAG = "checkScore", "new high score");
        } else {
            bird.setHighScore(sharedPreferences.getInt("highScore", bird.getHighScore()));
        }
        highScoreText.setText(String.valueOf("High Score: " + bird.getHighScore()));
    }

    @SuppressLint("SetTextI18n")
    public void resetGame() {
        if (!isAlive) {
            sound.getSoundPool().play(sound.collideSound, 0.1f, 0.1f, 1, 0, 1f);
        }

        MainActivity.mediaPlayer.stop();

        isRunning = false;
        isActive = false;
        isAlive = false;

        if (bird.getY() + bird.getHeight() <= SCREEN_HEIGHT - grass.getHeight()) {
            isAlive = true;
            sound.getSoundPool().play(sound.barrierCollideSound, 0.4f, 0.4f, 2, 0, 1f);
            Values.speedPipe = 0;
        } else {
            Log.d(Values.TAG = "resetGame", "Game reset");

            MainActivity.currentMusic = R.raw.theme_music;
            MainActivity.mediaPlayer = MediaPlayer.create(view.getContext(), currentMusic);

            MainActivity.mediaPlayer.start();
            scoreText.setVisibility(INVISIBLE);
            relativeLayout.setVisibility(VISIBLE);
            restartButton.setText("Restart");
            gameOverText.setText("Game Over");
            checkBirdSkin();

            if (bird.getScore() > bird.getHighScore()) {
                sound.getSoundPool().play(sound.highScoreSound, 1f, 1f, 1, 0, 1f);
            }

            editor.putBoolean("skinUnlocked", Bird.legendarySkin);
            editor.putInt("highScore", bird.getHighScore());
            editor.putInt("coinValue", bird.getCoins());
            editor.putInt("skinBought", Bird.boughtSkin);
            editor.commit();
            makeText(highScoreText.getContext(), "Score saved", Toast.LENGTH_SHORT).show();

            bird.setScore(0);
            bird.setY((float) SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
            bird.setFallGravity(0.6f);
            barriers.get(0).setX(SCREEN_WIDTH);
            barriers.get(1).setX(barriers.get(0).getX() + barrierDistance);
            barriers.get(2).setX(barriers.get(1).getX() + barrierDistance);
            Values.gapPipe = 380;
            Values.speedPipe = 9 * SCREEN_WIDTH / 1080;
            if (!MainActivity.view.isHardCore) {
                Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
            }
        }
    }

    private void checkBirdSkin() {
        if (sharedPreferences.getInt("skinBought", Bird.boughtSkin) == 0 && !isRunning && sharedPreferences.getInt("coinValue", bird.getCoins()) >= 1000) {
            Snackbar.make(relativeLayout, "A new skin is available for purchase", Snackbar.LENGTH_SHORT).show();
        }
        if (sharedPreferences.getInt("highScore", bird.getHighScore()) >= 10000 && !sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin) && !isRunning) {
            Snackbar.make(relativeLayout, "New skin unlocked", Snackbar.LENGTH_SHORT).show();
            Bird.legendarySkin = true;
        }
    }

    private void gameSpeedUp() {
        if (bird.getScore() > 1000 && bird.getScore() < 2000) {
            Values.speedPipe = 10 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 2000 && bird.getScore() < 3000) {
            Values.speedPipe = 11 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 3000 && bird.getScore() < 4000) {
            Values.speedPipe = 12 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 4000 && bird.getScore() < 5000) {
            Values.speedPipe = 13 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 5000) {
            Values.speedPipe = 14 * SCREEN_WIDTH / 1080;
        }
    }

}

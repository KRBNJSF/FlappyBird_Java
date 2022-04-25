package cz.reindl.game.event;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.makeText;
import static cz.reindl.game.MainActivity.editor;
import static cz.reindl.game.MainActivity.gameOverText;
import static cz.reindl.game.MainActivity.highScoreText;
import static cz.reindl.game.MainActivity.relativeLayout;
import static cz.reindl.game.MainActivity.restartButton;
import static cz.reindl.game.MainActivity.scoreText;
import static cz.reindl.game.MainActivity.sharedPreferences;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.values.Values.barrierDistance;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.isActive;
import static cz.reindl.game.view.View.isAlive;
import static cz.reindl.game.view.View.isHardCore;
import static cz.reindl.game.view.View.isRunning;
import static cz.reindl.game.view.View.sound;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.reindl.game.entity.Barrier;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.values.Values;

public class EventCheck {

    public final List<Barrier> barriers = new ArrayList();

    //COLLISION CHECK
    public void collision() {
        checkHighScore();
        for (int i = 0; i < barriers.size(); i++) {
           /* if ((bird.getX() + bird.getWidth()) > (barriers.get(i).getX() - barriers.get(i).getWidth()) && bird.getY() < barriers.get(i).getY() + ((float) SCREEN_HEIGHT / 2 - Constants.gapPipe) || bird.getY() >= SCREEN_HEIGHT) { //TOP BARRIER COLLISION
                resetGame();
            } else if ((bird.getY() + bird.getHeight()) > (float) SCREEN_HEIGHT / 2 + Constants.gapPipe + barriers.get(i).getY() && (bird.getX() + bird.getWidth()) > barriers.get(i).getX()) { //BOTTOM BARRIER COLLISION
                resetGame();
            } else if (bird.getX() + bird.getWidth() > barriers.get(i).getX() + barriers.get(i).getWidth()) {
                if (bird.getX() == barriers.get(i).getX()) {
                    if (!bird.getRect().setIntersect(bird.getRect(), barriers.get(i).getRect())) {
                        bird.setScore(bird.getScore() + 1);
                        sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
                    }
                }
            }*/
            if (bird.getRect().intersect(barriers.get(i).getRect()) || bird.getY() >= SCREEN_HEIGHT || bird.getRect().intersect(barriers.get(i).getBottomPipeRect())) {
                resetGame();
            } else if (bird.getX() == barriers.get(i).getX()) {
                sound.getSoundPool().play(sound.scoreSound, 1f, 1f, 1, 0, 1f);
            }
        }
        bird.setScore(bird.getScore() + 1);
        scoreText.setText(String.valueOf("Score: " + bird.getScore()));
    }

    //HIGH SCORE CHECK
    private void checkHighScore() {
        if (isHardCore) {
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

        isRunning = false;
        isActive = false;
        isAlive = false;

        if (bird.getY() <= SCREEN_HEIGHT) {
            isAlive = true;
            sound.getSoundPool().play(sound.barrierCollideSound, 0.4f, 0.4f, 1, 0, 1f);
            Values.speedPipe = 0;
        } else {
            Log.d(Values.TAG = "resetGame", "Game reset");
            scoreText.setVisibility(INVISIBLE);
            relativeLayout.setVisibility(VISIBLE);
            restartButton.setText("Restart");
            gameOverText.setText("Game Over");

            if (bird.getScore() > bird.getHighScore()) {
                sound.getSoundPool().play(sound.highScoreSound, 1f, 1f, 1, 0, 1f);
            }

            editor.putBoolean("skinUnlocked", Bird.skinUnlocked);
            editor.putInt("highScore", bird.getHighScore());
            editor.commit();
            makeText(highScoreText.getContext(), "Score saved", Toast.LENGTH_SHORT).show();

            Bird.skinUnlocked = false;
            bird.setScore(0);
            bird.setY((float) SCREEN_HEIGHT / 2 - (float) bird.getHeight() / 2);
            bird.setGravity(0.6f);
            barriers.get(0).setX(SCREEN_WIDTH);
            barriers.get(1).setX(barriers.get(0).getX() + barrierDistance);
            barriers.get(2).setX(barriers.get(1).getX() + barrierDistance);
            Values.gapPipe = 350;
            Values.speedPipe = 10 * SCREEN_WIDTH / 1080;
            if (!isHardCore) {
                Values.speedPipe = 19 * SCREEN_WIDTH / 1080;
            }
        }
    }

    private void gameSpeedUp() {
        if (bird.getScore() > 1000 && bird.getScore() < 2000) {
            Values.speedPipe = 11 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 2000 && bird.getScore() < 3000) {
            Values.speedPipe = 12 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 3000 && bird.getScore() < 4000) {
            Values.speedPipe = 13 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 4000 && bird.getScore() < 5000) {
            Values.speedPipe = 14 * SCREEN_WIDTH / 1080;
        } else if (bird.getScore() > 5000) {
            Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
        }
    }

}

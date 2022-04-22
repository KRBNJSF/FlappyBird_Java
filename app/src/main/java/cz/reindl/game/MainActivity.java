package cz.reindl.game;

import static cz.reindl.game.constants.Constants.SCREEN_WIDTH;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.sound.Sound;
import cz.reindl.game.view.DeathScreen;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics metrics;
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText, highScoreText, gameOverText;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static Button button, restartButton;
    public static RelativeLayout relativeLayout;
    private MediaPlayer mediaPlayer;
    private View view;
    private int i = 0;

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.gameView);
        relativeLayout = (RelativeLayout) findViewById(R.id.gameOver);

        //Intent intent = new Intent(this, View.class);
        //startActivity(intent);

        scoreText = (TextView) findViewById(R.id.scoreTextView);
        scoreText.setTextAppearance(R.style.whiteText);
        highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setTextAppearance(R.style.whiteText);
        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        button = (Button) findViewById(R.id.buttonMain);
        gameOverText = (TextView) findViewById(R.id.gameOverText);
        restartButton = (Button) findViewById(R.id.buttonRestart);

        button.setOnClickListener(v -> {
            if (i == 0) {
                View.isActive = true;
                Constants.speedPipe = 19 * SCREEN_WIDTH / 1080;
                Bird.skinUnlocked = true;
                i = 1;
                Constants.gapPipe = 600;
            } else {
                View.isActive = false;
                i = 0;
                Bird.skinUnlocked = false;
                Constants.gapPipe = 200;
            }
        });

        restartButton.setOnClickListener(v -> {
            view.isRunning = true;
            relativeLayout.setVisibility(android.view.View.INVISIBLE);
            scoreText.setVisibility(android.view.View.VISIBLE);
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

    }

}
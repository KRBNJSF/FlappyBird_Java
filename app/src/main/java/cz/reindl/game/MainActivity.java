package cz.reindl.game;

import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.view.View.isActive;
import static cz.reindl.game.view.View.isHardCore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics metrics;
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText, highScoreText, gameOverText;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    public static Button button, restartButton, hardCoreButton;
    @SuppressLint("StaticFieldLeak")
    public static ImageButton buttonSkin1, buttonSkin2;
    @SuppressLint("StaticFieldLeak")
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
        Values.SCREEN_WIDTH = metrics.widthPixels;
        Values.SCREEN_HEIGHT = metrics.heightPixels;
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.gameView);
        relativeLayout = (RelativeLayout) findViewById(R.id.gameOver);
        relativeLayout.setVisibility(android.view.View.VISIBLE);
        view.isRunning = false;

        scoreText = (TextView) findViewById(R.id.scoreTextView);
        scoreText.setTextAppearance(R.style.whiteText);
        highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setTextAppearance(R.style.whiteText);
        highScoreText.setVisibility(android.view.View.INVISIBLE);
        scoreText.setVisibility(android.view.View.INVISIBLE);

        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        button = (Button) findViewById(R.id.buttonMain);

        gameOverText = (TextView) findViewById(R.id.gameOverText);
        restartButton = (Button) findViewById(R.id.buttonRestart);
        buttonSkin1 = (ImageButton) findViewById(R.id.buttonSkin1);
        buttonSkin2 = (ImageButton) findViewById(R.id.buttonSkin2);
        hardCoreButton = (Button) findViewById(R.id.buttonHardCore);
        gameOverText.setText("Flappy Bird");
        restartButton.setText("Start");

        buttonSkin1.setOnClickListener(l -> Bird.changeSkin = false);

        hardCoreButton.setOnClickListener(l -> {
            if (!isHardCore) {
                isHardCore = true;
                hardCoreButton.setBackgroundColor(Color.BLACK);
                Values.speedPipe = 10 * SCREEN_WIDTH / 1080;
            } else {
                isHardCore = false;
                Values.speedPipe = 19 * SCREEN_WIDTH / 1080;
                hardCoreButton.setBackgroundColor(Color.RED);
            }
        });

        buttonSkin2.setOnClickListener(l -> {
            if (Bird.skinUnlocked) {
                Bird.changeSkin = true;
            } else {
                System.out.println("It is not unlocked yet");
            }
        });

        button.setOnClickListener(v -> {
            if (i == 0) {
                isActive = true;
                i = 1;
                Values.gapPipe = 600;
            } else {
                isActive = false;
                i = 0;
                Bird.skinUnlocked = false;
                Values.gapPipe = 350;
            }
        });

        restartButton.setOnClickListener(v -> {
            view.isRunning = true;
            relativeLayout.setVisibility(android.view.View.INVISIBLE);
            scoreText.setVisibility(android.view.View.VISIBLE);
            highScoreText.setVisibility(android.view.View.VISIBLE);
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

}
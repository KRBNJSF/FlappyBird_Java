package cz.reindl.game;

import static android.widget.Toast.makeText;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.isActive;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics metrics;
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText, highScoreText, gameOverText, coinText;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    @SuppressLint("StaticFieldLeak")
    public static Button button, restartButton, hardCoreButton;
    @SuppressLint("StaticFieldLeak")
    public static ImageButton buttonSkin1, buttonSkin2, buttonSkin3;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout relativeLayout;
    @SuppressLint("StaticFieldLeak")
    public static ImageView grass;
    private MediaPlayer mediaPlayer;
    public static View view;
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
    @SuppressLint({"SetTextI18n", "WrongConstant", "UseCompatLoadingForDrawables"})
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
        coinText = (TextView) findViewById(R.id.coinText);
        highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setTextAppearance(R.style.whiteText);
        scoreText.setVisibility(android.view.View.INVISIBLE);

        grass = (ImageView) findViewById(R.id.grass);

        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        button = (Button) findViewById(R.id.buttonMain);

        gameOverText = (TextView) findViewById(R.id.gameOverText);
        restartButton = (Button) findViewById(R.id.buttonRestart);
        buttonSkin1 = (ImageButton) findViewById(R.id.buttonSkin1);
        buttonSkin2 = (ImageButton) findViewById(R.id.buttonSkin2);
        buttonSkin3 = (ImageButton) findViewById(R.id.buttonSkin3);
        hardCoreButton = (Button) findViewById(R.id.buttonHardCore);

        gameOverText.setText("Flappy Bird");
        restartButton.setText("Start");
        highScoreText.setText(String.valueOf("High Score: " + sharedPreferences.getInt("highScore", bird.getHighScore())));
        coinText.setText(String.valueOf(sharedPreferences.getInt("coinValue", bird.getCoins())));
        bird.setCoins(sharedPreferences.getInt("coinValue", bird.getCoins()));
        Bird.legendarySkin = sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin);
        Bird.boughtSkin = sharedPreferences.getInt("skinBought", Bird.boughtSkin);

        if (Bird.boughtSkin == 0) {
            buttonSkin3.setBackground(getDrawable(R.drawable.skin_bird_locked));
        }
        if (!Bird.legendarySkin) {
            buttonSkin2.setBackground(getDrawable(R.drawable.legendary_skin_locked));
        }

        buttonSkin1.setOnClickListener(l -> {
            Bird.legendarySkinUsing = false;
            Bird.boughtSkinUsing = false;
            bird.setHeight(105 * SCREEN_HEIGHT / 1920);
            bird.setWidth(105 * SCREEN_WIDTH / 1080);
            bird.setBirdList(bird.getBirdList());
        });

        hardCoreButton.setOnClickListener(l -> {
            if (!view.isHardCore) {
                view.isHardCore = true;
                hardCoreButton.setBackgroundColor(Color.BLACK);
                Values.speedPipe = 9 * SCREEN_WIDTH / 1080;
            } else {
                makeText(this, "HardCore enabled", Toast.LENGTH_SHORT).show();
                view.isHardCore = false;
                Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
                hardCoreButton.setBackgroundColor(Color.RED);
            }
        });

        buttonSkin2.setOnClickListener(l -> {
            if (Bird.legendarySkin) {
                Bird.boughtSkinUsing = false;
                Bird.legendarySkinUsing = true;
                bird.setHeight(100 * SCREEN_HEIGHT / 1920);
                bird.setWidth(110 * SCREEN_WIDTH / 1080);
                bird.setBirdList(bird.getBirdList());
            } else {
                makeText(this, "It is not unlocked yet, " + (10000 - sharedPreferences.getInt("highScore", bird.getHighScore())) + " score remaining", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSkin3.setOnClickListener(l -> {
            if (Bird.boughtSkin == 1) {
                Bird.boughtSkinUsing = true;
                Bird.legendarySkinUsing = false;
                bird.setHeight(80 * SCREEN_HEIGHT / 1920);
                bird.setWidth(105 * SCREEN_WIDTH / 1080);
                bird.setBirdList(bird.getBirdList());
            } else if (Bird.boughtSkin == 0 && bird.getCoins() == 1000) {
                bird.setCoins(bird.getCoins() - 1000);
                coinText.setText(bird.getCoins());
                Bird.boughtSkin = 1;
            } else {
                makeText(this, "It is not unlocked yet, " + (1000 - sharedPreferences.getInt("coinValue", bird.getCoins())) + " coins remaining", Toast.LENGTH_SHORT).show();
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
                Bird.legendarySkin = false;
                Values.gapPipe = 380;
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
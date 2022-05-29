package cz.reindl.game;

import static android.widget.Toast.makeText;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.isActive;
import static cz.reindl.game.view.View.isRunning;
import static cz.reindl.game.view.View.sound;

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
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {

    EventHandler eventHandler = new EventHandler();

    DisplayMetrics metrics;
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText, highScoreText, gameOverText, coinText, lastScoreText, timerText;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @SuppressLint("StaticFieldLeak")
    public static Button devButton, restartButton, hardCoreButton, reviveButton, skipReviveButton, settingsButton, shopButton;
    @SuppressLint("StaticFieldLeak")
    public static ImageButton buttonSkin1, buttonSkin2, buttonSkin3, buttonStop, musicStopButton;

    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout relativeLayout;
    @SuppressLint("StaticFieldLeak")
    public static ImageView grass;
    public static MediaPlayer mediaPlayer;
    public static View view;

    public static int isDevButtonOn, isGameStopped, isRevived = 0;
    public static boolean isMusicStopped;
    public static int currentMusic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "WrongConstant", "UseCompatLoadingForDrawables", "LongLogTag"})
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
        lastScoreText = (TextView) findViewById(R.id.lastScoreText);
        timerText = (TextView) findViewById(R.id.timerText);

        grass = (ImageView) findViewById(R.id.grass);
        grass.setMinimumHeight(SCREEN_HEIGHT / 4);

        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        devButton = (Button) findViewById(R.id.buttonMain);

        gameOverText = (TextView) findViewById(R.id.gameOverText);
        restartButton = (Button) findViewById(R.id.buttonRestart);
        buttonSkin1 = (ImageButton) findViewById(R.id.buttonSkin1);
        buttonSkin2 = (ImageButton) findViewById(R.id.buttonSkin2);
        buttonSkin3 = (ImageButton) findViewById(R.id.buttonSkin3);
        hardCoreButton = (Button) findViewById(R.id.buttonHardCore);
        buttonStop = (ImageButton) findViewById(R.id.buttonStop);
        reviveButton = (Button) findViewById(R.id.reviveButton);
        musicStopButton = (ImageButton) findViewById(R.id.musicStopButton);
        skipReviveButton = (Button) findViewById(R.id.skipReviveButton);
        settingsButton = (Button) findViewById(R.id.settingButton);
        shopButton = (Button) findViewById(R.id.shopButton);

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

        //LISTENERS

        buttonSkin1.setOnClickListener(l -> {
            Bird.legendarySkinUsing = false;
            Bird.boughtSkinUsing = false;
            bird.setHeight(105 * SCREEN_HEIGHT / 1920);
            bird.setWidth(105 * SCREEN_WIDTH / 1080);
            bird.getBirdList().clear();
            view.initBirdList();
        });

        hardCoreButton.setOnClickListener(l -> {
            if (!view.isHardCore) {
                view.isHardCore = true;
                hardCoreButton.setBackgroundColor(Color.BLACK);
                Values.speedPipe = 9 * SCREEN_WIDTH / 1080;
                if (!mediaPlayer.isPlaying() && !isMusicStopped) {
                    mediaPlayer.stop();
                    backgroundMusic(R.raw.background_music);
                }
            } else {
                makeText(this, "Hardcore enabled", Toast.LENGTH_SHORT).show();
                view.isHardCore = false;
                Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
                hardCoreButton.setBackgroundColor(Color.RED);
            }
        });

        buttonSkin2.setOnClickListener(l -> {
            Bird.legendarySkin = sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin);
            if (sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin)) {
                Bird.boughtSkinUsing = false;
                Bird.legendarySkinUsing = true;
                bird.setHeight(100 * SCREEN_HEIGHT / 1920);
                bird.setWidth(110 * SCREEN_WIDTH / 1080);
                buttonSkin2.setBackground(getDrawable(R.drawable.legendary_skinup));
                bird.getBirdList().clear();
                view.initBirdList();
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
                bird.getBirdList().clear();
                view.initBirdList();
            } else if (Bird.boughtSkin == 0 && bird.getCoins() >= 1000) {
                makeText(this, "Successfully bought", Toast.LENGTH_SHORT).show();
                bird.setCoins(bird.getCoins() - 1000);
                coinText.setText(String.valueOf(bird.getCoins()));
                Bird.boughtSkin = 1;
                buttonSkin3.setBackground(getDrawable(R.drawable.default_bird));
                editor.putInt("skinBought", Bird.boughtSkin);
                editor.putInt("coinValue", bird.getCoins());
                editor.commit();
            } else {
                makeText(this, "It is not unlocked yet, " + (1000 - sharedPreferences.getInt("coinValue", bird.getCoins())) + " coins remaining", Toast.LENGTH_SHORT).show();
            }
        });

        buttonStop.setOnClickListener(l -> {
            if (isGameStopped == 0) {
                isGameStopped = 1;
                buttonStop.setBackground(getDrawable(R.drawable.ic_play_button));
                if (!isMusicStopped) {
                    mediaPlayer.pause();
                }
                view.handler.removeCallbacks(view.runnable);
            } else {
                isGameStopped = 0;
                buttonStop.setBackground(getDrawable(R.drawable.ic_pause_button));
                if (!isMusicStopped) {
                    mediaPlayer.start();
                }
                view.handler.postDelayed(view.runnable, 1);
            }
        });

        devButton.setOnClickListener(v -> {
            if (isDevButtonOn == 0) {
                isActive = true;
                isDevButtonOn = 1;
                Values.gapPipe = 600;
            } else {
                isActive = false;
                isDevButtonOn = 0;
                Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
            }
        });

        restartButton.setOnClickListener(v -> {
            if (currentMusic != R.raw.background_music && !isMusicStopped) {
                mediaPlayer.stop();
                Log.i(Values.TAG = "MainActivity - restartButton", "Playing music ID");
                System.out.println("Music ID: " + currentMusic);
                backgroundMusic(R.raw.background_music);
            }
            if (!view.isHardCore && !isMusicStopped) {
                mediaPlayer.stop();
                backgroundMusic(R.raw.hardcore_theme);
            }
            view.isRunning = true;
            musicStopButton.setVisibility(android.view.View.INVISIBLE);
            relativeLayout.setVisibility(android.view.View.INVISIBLE);
            scoreText.setVisibility(android.view.View.VISIBLE);
            highScoreText.setVisibility(android.view.View.VISIBLE);
            buttonStop.setVisibility(android.view.View.VISIBLE);
            lastScoreText.setVisibility(android.view.View.VISIBLE);
        });

        reviveButton.setOnClickListener(l -> {
            sound.getSoundPool().play(sound.reviveSound, 1f, 1f, 1, 0, 1f);
            isRevived = 1;
            // FIXME: 19.05.2022 eventHandler.continueGame();
            reviveButton.setVisibility(android.view.View.INVISIBLE);
            skipReviveButton.setVisibility(android.view.View.INVISIBLE);
            bird.setCoins(bird.getCoins() - 50);
            coinText.setText(String.valueOf(bird.getCoins()));
        });

        musicStopButton.setOnClickListener(l -> {
            if (!isMusicStopped && isGameStopped == 0) {
                musicStopButton.setBackground(getDrawable(R.drawable.ic_music_button));
                isMusicStopped = true;
                mediaPlayer.pause();
            } else {
                musicStopButton.setBackground(getDrawable(R.drawable.ic_music_play_button));
                isMusicStopped = false;
                mediaPlayer.start();
            }
        });

        skipReviveButton.setOnClickListener(l -> {
            // FIXME: 19.05.2022 eventHandler.resetValues();
            skipReviveButton.setVisibility(android.view.View.INVISIBLE);
            reviveButton.setVisibility(android.view.View.INVISIBLE);
        });

        settingsButton.setOnClickListener(l -> {

        });

        shopButton.setOnClickListener(l -> {

        });

        backgroundMusic(R.raw.theme_music);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        isGameStopped = 0;
        buttonStop.setBackground(getDrawable(R.drawable.ic_pause_button));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    public void backgroundMusic(int resid) {
        currentMusic = resid;
        mediaPlayer = MediaPlayer.create(this, currentMusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isGameStopped == 1) {
            timerText.setVisibility(android.view.View.VISIBLE);
            try {
                for (int i = 3; i > 0; i--) {
                    makeText(this, String.valueOf("Start in " + i + " sec"), Toast.LENGTH_SHORT).show();
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        timerText.setVisibility(android.view.View.INVISIBLE);
    }
}
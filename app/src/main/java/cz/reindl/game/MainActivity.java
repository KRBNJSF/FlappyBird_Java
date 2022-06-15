package cz.reindl.game;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.makeText;
import static cz.reindl.game.values.Values.SCREEN_HEIGHT;
import static cz.reindl.game.values.Values.SCREEN_WIDTH;
import static cz.reindl.game.view.View.bird;
import static cz.reindl.game.view.View.isActive;
import static cz.reindl.game.view.View.isAlive;
import static cz.reindl.game.view.View.isRunning;
import static cz.reindl.game.view.View.sound;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import cz.reindl.game.event.EventHandler;
import cz.reindl.game.values.Values;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {

    EventHandler eventHandler = new EventHandler();

    DisplayMetrics metrics;
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText, highScoreText, gameOverText, coinText, lastScoreText, powerUpText, coinGetText;
    @SuppressLint("StaticFieldLeak")
    public static EditText bonusCodeText;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @SuppressLint("StaticFieldLeak")
    public static Button devButton, restartButton, hardCoreButton, reviveButton, skipReviveButton, settingsButton, shopButton, duckButton, boosterButton, buyBoostButton;
    @SuppressLint("StaticFieldLeak")
    public static ImageButton buttonSkin1, buttonSkin2, buttonSkin3, buttonStop, musicStopButton;

    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout menuLayout, mainLayout, shopLayout;
    @SuppressLint("StaticFieldLeak")
    public static ImageView grass;
    public static MediaPlayer mediaPlayer;
    public static View view;

    public static int isDevButtonOn, isGameStopped, isRevived, x, y = 0;
    public static boolean isMusicStopped, isShop, isBonusUsed;
    public static int currentMusic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "WrongConstant", "UseCompatLoadingForDrawables", "LongLogTag", "ClickableViewAccessibility"})
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
        menuLayout = (RelativeLayout) findViewById(R.id.mainMenuLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        shopLayout = (RelativeLayout) findViewById(R.id.shopLayout);
        menuLayout.setVisibility(android.view.View.VISIBLE);
        shopLayout.setVisibility(android.view.View.VISIBLE); // FIXME: 31.05.2022 DELETE!
        view.isRunning = false;

        scoreText = (TextView) findViewById(R.id.scoreTextView);
        scoreText.setTextAppearance(R.style.whiteText);
        coinText = (TextView) findViewById(R.id.coinText);
        highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setTextAppearance(R.style.whiteText);
        scoreText.setVisibility(android.view.View.INVISIBLE);
        lastScoreText = (TextView) findViewById(R.id.lastScoreText);
        powerUpText = (TextView) findViewById(R.id.powerUpText);
        coinGetText = (TextView) findViewById(R.id.coinGetText);

        grass = (ImageView) findViewById(R.id.grass);
        grass.setMinimumHeight(SCREEN_HEIGHT / 4);

        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        devButton = (Button) findViewById(R.id.devButton);

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
        duckButton = (Button) findViewById(R.id.duckButton);
        boosterButton = (Button) findViewById(R.id.boostButton);
        buyBoostButton = (Button) findViewById(R.id.buyBoostButton);
        bonusCodeText = (EditText) findViewById(R.id.bonusCodeText);

        shopButton.setX((float) 0);

        gameOverText.setText("Flappy Bird");
        restartButton.setText("Start");
        highScoreText.setText(String.valueOf("High Score: " + sharedPreferences.getInt("highScore", bird.getHighScore())));
        coinText.setText(String.valueOf(sharedPreferences.getInt("coinValue", bird.getCoins())));
        bird.setCoins(sharedPreferences.getInt("coinValue", bird.getCoins()));
        Bird.legendarySkin = sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin);
        Bird.boughtSkin = sharedPreferences.getInt("skinBought", Bird.boughtSkin);
        Bird.boosterCount = sharedPreferences.getInt("boosterCount", Bird.boosterCount);
        isBonusUsed = sharedPreferences.getBoolean("isBonusUsed", isBonusUsed);
        //view.isDragon = sharedPreferences.getBoolean("isDragon", view.isDragon);
        //Bird.boughtSkinUsing = sharedPreferences.getBoolean("boughtSkinUsing", Bird.boughtSkinUsing);
        boosterButton.setText(String.valueOf(Bird.boosterCount));

        if (!sharedPreferences.getBoolean("isDragon", view.isDragon)) {
            duckButton.setAlpha(0.02f);
        }

        x = 300;
        y = 300;

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
            view.isDragon = false;
            bird.setHeight(105 * SCREEN_HEIGHT / 1920);
            bird.setWidth(105 * SCREEN_WIDTH / 1080);
            if (bird.getBirdList().get(0) != bird.getBirdList().get(4)) {
                bird.getBirdList().clear();
                view.initBirdList();
            }
            bird.setWidth(75 * SCREEN_WIDTH / 1080);
        });

        hardCoreButton.setOnClickListener(l -> {
            if (!view.isHardCore) {
                if (view.isBooster) {
                    view.isBooster = false;
                    Bird.boosterCount++;
                    editor.putInt("boosterCount", Bird.boosterCount);
                    editor.commit();
                    Snackbar.make(menuLayout, "Booster disabled", Snackbar.LENGTH_SHORT).show();
                    boosterButton.setText(String.valueOf(Bird.boosterCount));
                }
                view.isHardCore = true;
                devButton.setBackgroundColor(getColor(R.color.SkyBlue));
                hardCoreButton.setBackgroundColor(Color.BLACK);
                mainLayout.setBackground(getDrawable(R.drawable.background));
                restartButton.setTextColor(Color.WHITE);
                view.barriers.clear();
                view.initBarrier(BitmapFactory.decodeResource(view.getResources(), R.drawable.bottom_pipe), BitmapFactory.decodeResource(view.getResources(), R.drawable.top_pipe));
                Values.speedPipe = 9 * SCREEN_WIDTH / 1080;
                if (!mediaPlayer.isPlaying() && !isMusicStopped) {
                    mediaPlayer.stop();
                    backgroundMusic(R.raw.background_music);
                }
            } else {
                makeText(this, "Hardcore enabled", Toast.LENGTH_SHORT).show();
                devButton.setBackgroundColor(getColor(R.color.darkBlue));
                view.isHardCore = false;
                mainLayout.setBackground(getDrawable(R.drawable.background2));
                restartButton.setTextColor(Color.BLACK);
                hardCoreButton.setBackgroundColor(Color.RED);
                view.barriers.clear();
                view.initBarrier(BitmapFactory.decodeResource(view.getResources(), R.drawable.bottom_pipeblue), BitmapFactory.decodeResource(view.getResources(), R.drawable.top_pipeblue));
                Values.speedPipe = 15 * SCREEN_WIDTH / 1080;
            }
        });

        buttonSkin2.setOnClickListener(l -> {
            Bird.legendarySkin = sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin);
            if (sharedPreferences.getBoolean("skinUnlocked", Bird.legendarySkin)) {
                Bird.boughtSkinUsing = false;
                Bird.legendarySkinUsing = true;
                view.isDragon = false;
                bird.setHeight(100 * SCREEN_HEIGHT / 1920);
                bird.setWidth(110 * SCREEN_WIDTH / 1080);
                buttonSkin2.setBackground(getDrawable(R.drawable.legendary_skinup));
                if (bird.getBirdList().get(0) != bird.getBirdList().get(2)) {
                    bird.getBirdList().clear();
                    view.initBirdList();
                }
            } else {
                makeText(this, "It is not unlocked yet, " + (10000 - sharedPreferences.getInt("highScore", bird.getHighScore())) + " score remaining", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSkin3.setOnClickListener(l -> {
            if (Bird.boughtSkin == 1) {
                Bird.boughtSkinUsing = true;
                Bird.legendarySkinUsing = false;
                view.isDragon = false;
                bird.setHeight(80 * SCREEN_HEIGHT / 1920);
                bird.setWidth(105 * SCREEN_WIDTH / 1080);
                if (bird.getBirdList().get(0) != bird.getBirdList().get(6)) {
                    bird.getBirdList().clear();
                    view.initBirdList();
                }
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
            if (!isRunning && !isAlive && !isBonusUsed) {
                bonusCodeText.setVisibility(VISIBLE);
            }
            if (isDevButtonOn == 0) {
                isActive = true;
                isDevButtonOn = 1;
                //Values.gapPipe = 600;
            } else {
                isActive = false;
                isDevButtonOn = 0;
                //Values.gapPipe = (int) (SCREEN_HEIGHT / 5.4);
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
            duckButton.setVisibility(android.view.View.INVISIBLE);
            view.isRunning = true;
            musicStopButton.setVisibility(android.view.View.INVISIBLE);
            menuLayout.setVisibility(android.view.View.INVISIBLE);
            scoreText.setVisibility(android.view.View.VISIBLE);
            highScoreText.setVisibility(android.view.View.VISIBLE);
            buttonStop.setVisibility(android.view.View.VISIBLE);
            lastScoreText.setVisibility(android.view.View.VISIBLE);
            shopButton.setVisibility(android.view.View.INVISIBLE);
            settingsButton.setVisibility(android.view.View.INVISIBLE);
            boosterButton.setVisibility(android.view.View.INVISIBLE);
            buyBoostButton.setVisibility(android.view.View.INVISIBLE);
            bonusCodeText.setVisibility(INVISIBLE);
            shopLayout.setVisibility(android.view.View.INVISIBLE); // FIXME: 31.05.2022 DELETE!
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
            //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        });

        shopButton.setOnClickListener(l -> {
            //startActivity(new Intent(getApplicationContext(), ShopActivity.class));
            if (!isShop) {
                menuLayout.setVisibility(android.view.View.INVISIBLE);
                view.setVisibility(android.view.View.INVISIBLE);
                shopLayout.setVisibility(android.view.View.VISIBLE);
                isShop = true;
            } else {
                menuLayout.setVisibility(android.view.View.VISIBLE);
                view.setVisibility(android.view.View.VISIBLE);
                shopLayout.setVisibility(android.view.View.INVISIBLE);
                isShop = false;
            }
        });

        duckButton.setOnClickListener(l -> {
            view.isDragon = true;
            editor.putBoolean("isDragon", true);
            editor.commit();
            bird.setHeight(80 * SCREEN_HEIGHT / 1920);
            bird.setWidth(105 * SCREEN_WIDTH / 1080);
            if (bird.getBirdList().get(0) != bird.getBirdList().get(10)) {
                if (duckButton.getAlpha() != 1f) {
                    Snackbar.make(menuLayout, "New skin unlocked", Snackbar.LENGTH_SHORT).show();
                }
                duckButton.setAlpha(1f);
                bird.getBirdList().clear();
                view.initBirdList();
            }
        });

        boosterButton.setOnClickListener(l -> {
            if (view.isBooster) {
                Snackbar.make(menuLayout, "Booster's ready!", Snackbar.LENGTH_SHORT).show();
            } else if (!view.isBooster && !view.isHardCore && Bird.boosterCount > 0) {
                view.isBooster = true;
                Bird.boosterCount--;
                boosterButton.setText(String.valueOf(Bird.boosterCount));
                Snackbar.make(menuLayout, "Booster's ready!", Snackbar.LENGTH_SHORT).show();
            } else if (Bird.boosterCount <= 0) {
                Snackbar.make(menuLayout, "Not enough boosts", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(menuLayout, "Booster only works in Special mode!", Snackbar.LENGTH_SHORT).show();
            }
        });

        buyBoostButton.setOnClickListener(l -> {
            if (bird.getCoins() < 50) {
                Snackbar.make(menuLayout, "Booster costs 50 coins", Snackbar.LENGTH_SHORT).show();
            } else {
                bird.setCoins(bird.getCoins() - 50);
                Bird.boosterCount++;
                editor.putInt("boosterCount", Bird.boosterCount);
                editor.putInt("coinValue", bird.getCoins());
                editor.commit();
                boosterButton.setText(String.valueOf(Bird.boosterCount));
                coinText.setText(String.valueOf(bird.getCoins()));
                coinGetText.setText(String.valueOf(-50));
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (coinGetText.getVisibility() == VISIBLE) {
                            coinGetText.setVisibility(INVISIBLE);
                        }
                    }
                }, 100);

                /*if (!isRunning) {
                    view.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            x = (int) event.getX();
                            y = (int) event.getY();
                        }
                        return true;
                    });
                }*/ // FIXME: 14.06.2022

                coinGetText.setVisibility(VISIBLE);
                coinGetText.setX(x);
                coinGetText.setY(y);
                Snackbar.make(menuLayout, "Booster bought Successfully!", Snackbar.LENGTH_SHORT).show();
            }
        });

        bonusCodeText.setOnClickListener(l -> {
            if (!bonusCodeText.isActivated()) {
                String text = String.valueOf(bonusCodeText.getText());
                if (text.equals("kachna") && !isBonusUsed) {
                    isBonusUsed = true;
                    bonusCodeText.setVisibility(INVISIBLE);
                    bird.setCoins(bird.getCoins() + 500);
                    coinText.setText(String.valueOf(bird.getCoins()));
                    editor.putInt("coinValue", bird.getCoins());
                    editor.putBoolean("isBonusUsed", isBonusUsed);
                    editor.commit();
                    coinGetText.setText(String.valueOf("+500"));
                    coinGetText.setVisibility(VISIBLE);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (coinGetText.getVisibility() == VISIBLE) {
                                coinGetText.setVisibility(INVISIBLE);
                            }
                        }
                    }, 600);
                }
            }
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
            try {
                for (int i = 3; i > 0; i--) {
                    makeText(this, String.valueOf("Start in " + i + " sec"), Toast.LENGTH_SHORT).show();
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
package cz.reindl.game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import cz.reindl.game.constants.Constants;
import cz.reindl.game.entity.Bird;
import cz.reindl.game.view.View;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static TextView scoreText;
    @SuppressLint("StaticFieldLeak")
    public static TextView highScoreText;
    public static SharedPreferences sharedPreferences;
    public static @SuppressLint("CommitPrefEdits")
    SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;

        setContentView(R.layout.activity_main);

        scoreText = (TextView) findViewById(R.id.scoreTextView);
        scoreText.setTextAppearance(R.style.whiteText);
        highScoreText = (TextView) findViewById(R.id.highScore);
        highScoreText.setTextAppearance(R.style.whiteText);
        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
}
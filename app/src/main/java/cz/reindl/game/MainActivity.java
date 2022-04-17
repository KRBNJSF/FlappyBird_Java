package cz.reindl.game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Fullscreen
        DisplayMetrics metrics = new DisplayMetrics(); //Class that describes display information
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_WIDTH = metrics.widthPixels;
        Constants.SCREEN_HEIGHT = metrics.heightPixels;
        setContentView(R.layout.activity_main);
        scoreText = (TextView) findViewById(R.id.scoreTextView);
        highScoreText = (TextView) findViewById(R.id.highScore);
    }
}
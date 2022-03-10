package cz.reindl.game;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import cz.reindl.game.constants.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Fullscreen
        DisplayMetrics metrics = new DisplayMetrics(); //Class that describes display information
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Constants.SCREEN_WIDTH = metrics.widthPixels; //Width of phone
        Constants.SCREEN_HEIGHT = metrics.heightPixels; //Height of phone

        setContentView(R.layout.activity_main);
    }
}
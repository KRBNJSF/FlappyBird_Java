package cz.reindl.game.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

import cz.reindl.game.MainActivity;
import cz.reindl.game.R;

public class ShopActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Button homeButton;
    @SuppressLint("StaticFieldLeak")
    public static RelativeLayout shopLayout;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        homeButton = (Button) findViewById(R.id.homeButton);
        shopLayout = (RelativeLayout) findViewById(R.id.shopLayout);

        homeButton.setOnClickListener(l -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }
}
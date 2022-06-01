package com.example.mopus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HikingMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiking_map);
    }

    public void walkingMode(View view) {
        Button running_button = (Button) findViewById(R.id.running_mode);
        running_button.setBackgroundColor(0xFFFFE7E0);
        Button walking_button = (Button) findViewById(R.id.walking_mode);
        walking_button.setBackgroundColor(0xFFFF9B7C);

    }

    public void runningMode(View view) {
        Button walking_button = (Button) findViewById(R.id.walking_mode);
        walking_button.setBackgroundColor(0xFFFFE7E0);
        Button running_button = (Button) findViewById(R.id.running_mode);
        running_button.setBackgroundColor(0xFFFF9B7C);
    }

    public void clickToMap(View view) {
        Uri gmmIntentUri = Uri.parse("geo:0,0");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
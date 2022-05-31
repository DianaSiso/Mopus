package com.example.mopus;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickToSignUp(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void clickToSignIn(View view) {
        Intent intent = new Intent(this, MenstrualCycle.class);
        startActivity(intent);
    }


}
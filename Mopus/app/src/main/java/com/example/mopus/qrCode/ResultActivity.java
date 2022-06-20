package com.example.mopus.qrCode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mopus.R;

public class ResultActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        text = findViewById(R.id.textViewResult);

        Intent data = getIntent();

        text.setText(data.getStringExtra("key"));
    }
}
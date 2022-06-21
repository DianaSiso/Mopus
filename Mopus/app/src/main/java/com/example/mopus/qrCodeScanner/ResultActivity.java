package com.example.mopus.qrCodeScanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.mopus.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        text = findViewById(R.id.textViewResult);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent data = getIntent();

        JSONObject mainObject;

        try {
            mainObject = new JSONObject(data.getStringExtra("key"));
            String emailItem = (String) mainObject.get( "email" );
            //String priceItem = (String) mainObject.get( "price" );
            //Log.e("TESTING",""+nameItem +priceItem);
            text.setText(emailItem);
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
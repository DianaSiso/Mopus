package com.example.mopus;

import android.graphics.PorterDuff;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DrinkWater extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_water);
        Spinner spinner_water_total=findViewById(R.id.water_total_day);
        ArrayAdapter<CharSequence> adapter_total=ArrayAdapter.createFromResource(this, R.array.water_per_day, android.R.layout.simple_spinner_item);
        adapter_total.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_water_total.setAdapter(adapter_total);

        Spinner spinner_water_cup=findViewById(R.id.water_total_cup);
        ArrayAdapter<CharSequence> adapter_cup=ArrayAdapter.createFromResource(this, R.array.water_per_cup, android.R.layout.simple_spinner_item);
        adapter_cup.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_water_cup.setAdapter(adapter_cup);

        Spinner spinner_wake_up=findViewById(R.id.wake_up_time);
        ArrayAdapter<CharSequence> adapter_wake_up_time=ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter_wake_up_time.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_wake_up.setAdapter(adapter_wake_up_time);

        Spinner spinner_sleep=findViewById(R.id.sleep_time);
        ArrayAdapter<CharSequence> adapter_sleep=ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter_sleep.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_sleep.setAdapter(adapter_sleep);
    }
}
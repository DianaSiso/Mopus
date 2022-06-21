//we can delete this file

package com.example.mopus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopus.model.WaterStats;
import com.example.mopus.model.WaterStatsService;
import com.example.mopus.model.WaterTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DrinkWater extends AppCompatActivity {

    private int progr = 0;
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_water);

        email = getIntent().getStringExtra("email");
        Log.d("Email 1", email);

        Spinner spinner_wake_up= findViewById(R.id.input_wake_up_time);
        ArrayAdapter<CharSequence> adapter_wake_up_time=ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter_wake_up_time.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_wake_up.setAdapter(adapter_wake_up_time);

        Spinner spinner_sleep = findViewById(R.id.input_sleep_time);
        ArrayAdapter<CharSequence> adapter_sleep=ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter_sleep.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_sleep.setAdapter(adapter_sleep);

        UpdateProgressBar();
        Intent intent = new Intent(this, WaterStatsService.class);
        intent.putExtra("email", email);
        startService(intent);
    }

    public void editInfo(View view) {
        LinearLayout first = findViewById(R.id.inputs_water);
        LinearLayout second = findViewById(R.id.inputs_time);
        Button save = findViewById(R.id.button_save_water);
        LinearLayout third = findViewById(R.id.texts_time);
        if (third.getVisibility() ==  View.INVISIBLE) {
            third.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            first.setVisibility(View.VISIBLE);
            second.setVisibility(View.VISIBLE);
        } else {
            third.setVisibility(View.INVISIBLE);
            save.setVisibility(View.INVISIBLE);
            first.setVisibility(View.INVISIBLE);
            second.setVisibility(View.INVISIBLE);
        }
    }

    public void saveInfo(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // new input
                EditText water_total = findViewById(R.id.input_water_total);
                String wt = String.valueOf(water_total.getText());
                int value_water_total = Integer.parseInt(wt);
                EditText water_cup = findViewById(R.id.input_water_cup);
                String wc = String.valueOf(water_cup.getText());
                int value_water_cup = Integer.parseInt(wc);
                Spinner sleep_time = findViewById(R.id.input_sleep_time);
                String st = sleep_time.getSelectedItem().toString();
                int value_sleep_time = Integer.parseInt(st.substring(0,2));
                Spinner wake_up_time = findViewById(R.id.input_wake_up_time);
                String wut = wake_up_time.getSelectedItem().toString();
                int value_wake_up_time = Integer.parseInt(wut.substring(0,2));

                WaterTime water_time = new WaterTime(value_water_total, value_water_cup, value_sleep_time, value_wake_up_time);

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Water", "Update DB");
                        db.collection("water").document(email)
                                .update(water_time.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(DrinkWater.this, "Successful update", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DrinkWater.this, "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.d("Water", "Register DB");
                        db.collection("water")
                                .add(water_time.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(DrinkWater.this, "Successful registration", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DrinkWater.this, "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });

        LinearLayout first = findViewById(R.id.inputs_water);
        LinearLayout second = findViewById(R.id.inputs_time);
        Button save = findViewById(R.id.button_save_water);
        LinearLayout third = findViewById(R.id.texts_time);
        third.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
        first.setVisibility(View.INVISIBLE);
        second.setVisibility(View.INVISIBLE);

        finish();
        startActivity(getIntent());
    }


    public void addWater(View view) {
        ProgressBar progress_bar = findViewById(R.id.progress_bar_water);
        TextView progress_bar_text = findViewById(R.id.progress_bar_water_text);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        Object wad = document.getData().get("water_already_drunk");
                        Object wc = document.getData().get("water_cup");
                        Object wt = document.getData().get("water_total");
                        double water_already_drunk = Double.parseDouble(String.valueOf(wad));
                        double water_cup = Double.parseDouble(String.valueOf(wc));
                        double water_total = Double.parseDouble(String.valueOf(wt));
                        progr = (int) ((water_already_drunk+water_cup)/water_total * 100);
                        if (progr > 100) {
                            progr = 100;
                        }
                        progress_bar.setProgress(progr);
                        progress_bar_text.setText(String.valueOf(progr) + "%");
                        db.collection("water").document(email).update("water_already_drunk", water_already_drunk+water_cup);
                    } else {
                        Log.d("Getting data from DB", "... Error.");
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void UpdateProgressBar() {
        ProgressBar progress_bar = findViewById(R.id.progress_bar_water);
        TextView progress_bar_text = findViewById(R.id.progress_bar_water_text);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        Object wad = document.getData().get("water_already_drunk");
                        Object wc = document.getData().get("water_cup");
                        Object wt = document.getData().get("water_total");
                        double water_already_drunk = Double.parseDouble(String.valueOf(wad));
                        double water_cup = Double.parseDouble(String.valueOf(wc));
                        double water_total = Double.parseDouble(String.valueOf(wt));
                        progr = (int) ((water_already_drunk)/water_total * 100);
                        if (progr > 100) {
                            progr = 100;
                        }
                        progress_bar.setProgress(progr);
                        progress_bar_text.setText(String.valueOf(progr) + "%");
                    } else {
                        Log.d("Getting data from DB", "... Error.");
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });
    }

    private void updateStats() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("stats_water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                WaterStats water_stats = new WaterStats();

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Water_Stats", "Update DB");
                        db.collection("stats_water").document(email)
                                .update(water_stats.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(DrinkWater.this, "Successful update", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DrinkWater.this, "WATER STATS TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.d("Water_Stats", "Register DB");
                        db.collection("stats_water")
                                .add(water_stats.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(DrinkWater.this, "Successful registration", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DrinkWater.this, "WATER STATS TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });

    }
}
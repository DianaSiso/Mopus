package com.example.mopus;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopus.model.WaterTime;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mopus.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private int progr = 0;
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,  R.id.navigation_hiking_map, R.id.navigation_menstrual_cycle, R.id.navigation_profile, R.id.navigation_stats)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void clickToChangePassword(View view) {
        Intent intent = new Intent(view.getContext(), ChangePassword.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void clickToChangeInfo(View view) {
        Log.d("clickID", String.valueOf(view.getId()));
        Integer id = view.getId();
        String from = "";
        switch (id) {
            case R.id.profile_first_name:
                from = "first_name";
                break;
            case R.id.profile_last_name:
                from = "last_name";
                break;
            case R.id.profile_phone_number:
                from = "phone_number";
                break;
            case R.id.profile_email:
                from = "email";
                break;
            case R.id.profile_birth_date:
                from = "birth_date";
                break;
        }
        Intent intent = new Intent(view.getContext(), ChangeInfo.class);
        intent.putExtra("From", from);
        startActivity(intent);
    }

    public void walkingMode(View view) {
        Button running_button = (Button) findViewById(R.id.running_mode);
        running_button.setBackgroundColor(0xFFFFE7E0);
        Button walking_button = (Button) findViewById(R.id.walking_mode);
        walking_button.setBackgroundColor(0xFFFF9B7C);

    }

    
    public void clickToSearchHours(View view) {
        TreeMap<Integer, String> sorted = new TreeMap<Integer, String>();
        BarChart barChart = findViewById(R.id.barchart2);

        Spinner mySpinner_1 = findViewById(R.id.spinner_hours_day);
        Spinner mySpinner_2 = findViewById(R.id.spinner_hours_month);
        String month = mySpinner_2.getSelectedItem().toString();
        String day = mySpinner_1.getSelectedItem().toString();
        final HashMap<Integer, String> values_per_hour = new HashMap<>();

        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        HashMap<String, HashMap<String, Double>> sph = (HashMap<String, HashMap<String, Double>>) document.getData().get("stats_per_hour");
                        Log.d("Value", String.valueOf(sph.keySet())); //temos todos os dias que existem e estão registados na db
                        Log.d("Value 2", String.valueOf(sph));

                        for (String days : sph.keySet()) {
                            if (days.contains(month) && days.contains(day)) { //então é o dia que queremos!
                                Log.d("Value 3", String.valueOf(sph.get(days)));
                                for (String str : sph.get(days).keySet()) {
                                    // Log.d("Value 4", String.valueOf(sph.get(days).get(str)));
                                    // Log.d("Value 4",str);
                                    values_per_hour.put(Integer.parseInt(str), String.valueOf(sph.get(days).get(str)));
                                }
                            }
                        }

                        Log.d("Value 5", String.valueOf(values_per_hour));

                        TreeMap<Integer, String> sorted = new TreeMap<Integer, String> (values_per_hour);

                        // Display the TreeMap which is naturally sorted
                        for (Map.Entry<Integer, String> entry :
                                sorted.entrySet())
                            System.out.println("Key = " + entry.getKey()
                                    + ", Value = "
                                    + entry.getValue());

                        Log.d("Sorted", String.valueOf(sorted));
                        ArrayList<BarEntry> barArrayList = new ArrayList<>();

                        for (int i=0; i< sorted.size();i++) {
                            Log.d("lxlx:", String.valueOf(sorted.keySet().toArray()[i]));
                            Log.d("lxlx:", String.valueOf(sorted.get(sorted.keySet().toArray()[i])));
                            barArrayList.add(new BarEntry(Float.parseFloat(String.valueOf(sorted.keySet().toArray()[i])), Float.parseFloat(sorted.get(sorted.keySet().toArray()[i]))));

                        }

                        BarDataSet barDataSet = new BarDataSet(barArrayList, month + " " + day);
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);
                        barDataSet.setColor(Color.GRAY);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize((0f));
                        barChart.setNoDataText("Click here!");
                        barChart.getDescription().setEnabled(false);

                        Log.d("AAAAAAAA", String.valueOf(values_per_hour));
                        if (values_per_hour.size() > 1) {
                            barChart.setVisibility(View.VISIBLE);
                        } else {
                            barChart.setVisibility(View.INVISIBLE);
                        }

                    }
                } else {
                    Log.d("email", "Failed with: ", task.getException());
                }
            }
        });
    }

    public void clickToSearchDay(View view) {
        TreeMap<Integer, String> sorted = new TreeMap<Integer, String>();
        BarChart barChart = findViewById(R.id.barchart);

        Spinner mySpinner = findViewById(R.id.spinner_months);
        String month = mySpinner.getSelectedItem().toString();
        HashMap<Integer, String> values_per_day = new HashMap<>();

        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        HashMap<String, HashMap<String, Double>> spd = (HashMap<String, HashMap<String, Double>>) document.getData().get("stats_per_day");
                        Log.d("Value", String.valueOf(spd.keySet()));

                        for (String day : spd.keySet()) {
                            if (day.contains(month)) {
                                // Log.d("DAY_DEBUG", day);
                                // Log.d("DAY_NUMBER", day.replace(month + " ", ""));
                                // Log.d("DAY_VALUE", String.valueOf(spd.get(day)));
                                values_per_day.put( Integer.parseInt(day.replace(month + " ", "")), String.valueOf(spd.get(day)));
                            }
                        }

                        TreeMap<Integer, String> sorted = new TreeMap<Integer, String>(values_per_day);

                        // Display the TreeMap which is naturally sorted
                        for (Map.Entry<Integer, String> entry :
                                sorted.entrySet())
                            System.out.println("Key = " + entry.getKey()
                                    + ", Value = "
                                    + entry.getValue());

                        Log.d("Sorted", String.valueOf(sorted));
                        ArrayList<BarEntry> barArrayList = new ArrayList<>();

                        for (int i=0; i< sorted.size();i++) {
                            Log.d("lxlx:", String.valueOf(sorted.keySet().toArray()[i]));
                            Log.d("lxlx:", String.valueOf(sorted.get(sorted.keySet().toArray()[i])));
                            barArrayList.add(new BarEntry(Float.parseFloat(String.valueOf(sorted.keySet().toArray()[i])), Float.parseFloat(sorted.get(sorted.keySet().toArray()[i]))));

                        }

                        BarDataSet barDataSet = new BarDataSet(barArrayList, month);
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);
                        barDataSet.setColor(Color.GRAY);
                        barDataSet.setValueTextColor(Color.BLACK);
                        barDataSet.setValueTextSize((8f));
                        barChart.setNoDataText("Click here!");
                        barChart.getDescription().setEnabled(false);

                        if (values_per_day.size() > 1) {
                            barChart.setVisibility(View.VISIBLE);
                        } else {
                            barChart.setVisibility(View.INVISIBLE);
                        }

                    }
                } else {
                    Log.d("email", "Failed with: ", task.getException());
                }
            }
        });
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
                                        Toast.makeText(getApplicationContext(), "Successful update", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        Log.d("Water", "Register DB");
                        db.collection("water")
                                .add(water_time.toDB())
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Successful registration", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
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

}

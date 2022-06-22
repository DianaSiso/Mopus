package com.example.mopus.qrCodeScanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopus.HomeActivity;
import com.example.mopus.MainActivity;
import com.example.mopus.ProfessionalActivity2;
import com.example.mopus.R;
import com.example.mopus.Register;
import com.example.mopus.model.Scan;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    private String userEmail;
    private String date;
    private String monthScan;
    private String numberDay;
    private boolean isNewScan;

    private TextView userWeight;
    private TextView userHeight;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        userWeight = findViewById(R.id.user_weight);
        userHeight = findViewById(R.id.user_height);

        Spinner spinner_months= findViewById(R.id.spinner_months);
        ArrayAdapter<CharSequence> adapter_months=ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter_months.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_months.setAdapter(adapter_months);

        Spinner spinner_hours_months= findViewById(R.id.spinner_hours_month);
        ArrayAdapter<CharSequence> adapter_hours_months=ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);
        adapter_hours_months.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_hours_months.setAdapter(adapter_hours_months);

        Spinner spinner_hours_days= findViewById(R.id.spinner_hours_day);
        ArrayAdapter<CharSequence> adapter_hours_days=ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter_hours_days.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_hours_days.setAdapter(adapter_hours_days);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        Intent data = getIntent();

        Log.d(TAG, data.getStringExtra("key"));

        JSONObject mainObject;

        try {
            mainObject = new JSONObject(data.getStringExtra( "key" ));
            userEmail = mainObject.getString( "email" );
            date = mainObject.getString( "date" );
            String[] monthAndDay = date.split(" ");
            monthScan = monthAndDay[0];
            numberDay = monthAndDay[1];
            //String hour = (String) mainObject.get( "hour" );

            getStats();
            //text.setText(userEmail + " - " + day + " - " /*+ hour*/);

            isNewScan = mainObject.has("isNewScan");
            Log.d(TAG, "NEW ACTIVITY");

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
            Toast.makeText(this, "Something went wrong \n", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getStats() {
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                userWeight.setText(userWeight.getText() + String.valueOf(document.getData().get("weight")) + " Kg");
                                userHeight.setText(userHeight.getText() + String.valueOf(document.getData().get("height")) + " cm");

                                userWeight.setVisibility(View.VISIBLE);
                                userHeight.setVisibility(View.VISIBLE);

                                if(isNewScan) {
                                    saveNewScan();
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void saveNewScan() {
        Log.d(TAG, "NEW SCAN");
        Scan s = new Scan(String.valueOf(System.currentTimeMillis()), userEmail, date);
        db.collection("professional_scans").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                //.update(s.toDB())
                .set(s.toDB(), SetOptions.merge())
                .addOnCompleteListener(task1 -> {
                    //progressBar.setVisibility(View.GONE);
                    /*if(task1.isSuccessful()) {
                        Toast.makeText(this, "Successful scan", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "USER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }*/
                });
    }

    public void clickToSearchHours(View view) {
        TreeMap<Integer, String> sorted = new TreeMap<Integer, String>();
        BarChart barChart = findViewById(R.id.barchart2);

        Spinner mySpinner_1 = findViewById(R.id.spinner_hours_day);
        Spinner mySpinner_2 = findViewById(R.id.spinner_hours_month);
        String month = mySpinner_2.getSelectedItem().toString();
        String day = mySpinner_1.getSelectedItem().toString();
        final HashMap<Integer, String> values_per_hour = new HashMap<>();

        DocumentReference docIdRef = db.collection("water").document(userEmail);
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

        DocumentReference docIdRef = db.collection("water").document(userEmail);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        HashMap<String, HashMap<String, Double>> spd = (HashMap<String, HashMap<String, Double>>) document.getData().get("stats_per_day");

                        for (String day : spd.keySet()) {
                            String[] days = day.split(" ");
                            Log.d("DAY_DEBUG", days[1] + " - " + numberDay + ": " + ((Integer.parseInt(days[1]) < Integer.parseInt(numberDay) & day.contains(monthScan)) ? "menor" : "maior"));
                            if (day.contains(month)) {
                                if((day.contains(monthScan) && Integer.parseInt(days[1]) < Integer.parseInt(numberDay)) || !day.contains(monthScan)) {
                                    values_per_day.put( Integer.parseInt(day.replace(month + " ", "")), String.valueOf(spd.get(day)));
                                }
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

                        if (values_per_day.size() > 0) {
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
}
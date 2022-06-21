package com.example.mopus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mopus.model.WaterTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Mopus Reminder")
                .setContentText("Drink Water!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());

        Date currentTime = Calendar.getInstance().getTime();
        String[] temp = String.valueOf(currentTime).split(" ");
        String day = temp[1] + " " + temp[2];
        String hour = temp[3].substring(0,2);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();

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
                        HashMap<String, HashMap<String, Double>> sph = (HashMap<String, HashMap<String, Double>>) document.getData().get("stats_per_hour");
                        Log.d("Value_Before", String.valueOf(sph));
                        double water_already_drunk = Double.parseDouble(String.valueOf(wad));
                        WaterTime water_time = new WaterTime(day, hour, water_already_drunk);
                        Log.d("Value_Current", String.valueOf(water_time.getStats_per_hour()));
                        sph.putAll(water_time.getStats_per_hour());
                        Log.d("Value_After", String.valueOf(sph));
                        db.collection("water").document(email)
                                .update(water_time.toDB_per_hour(sph))
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()) {
                                        Toast.makeText(context.getApplicationContext(), "Successful update", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(context.getApplicationContext(), "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                        if (hour.equals("23")) {
                            // we also need update stats_per_day
                            HashMap<String, Double> spd = (HashMap<String, Double>) document.getData().get("stats_per_day");
                            Log.d("Value_Before", String.valueOf(spd));
                            wad = document.getData().get("water_already_drunk");
                            water_already_drunk = Double.parseDouble(String.valueOf(wad));
                            WaterTime water_time_day = new WaterTime(day, water_already_drunk);
                            Log.d("Value_Current", String.valueOf(water_time_day.getStats_per_day()));
                            spd.putAll(water_time_day.getStats_per_day());
                            Log.d("Value_After", String.valueOf(spd));
                            db.collection("water").document(email)
                                    .update(water_time_day.toDB_per_day(spd))
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()) {
                                            Toast.makeText(context.getApplicationContext(), "Successful update", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context.getApplicationContext(), "WATER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                } else {
                    Log.d("email", "Failed with: ", task.getException());
                }
            }
        });
    }
}

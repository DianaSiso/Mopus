package com.example.mopus.model;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mopus.AlarmReceiver;
import com.example.mopus.DrinkWater;
import com.example.mopus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

public class WaterStatsService extends Service {
    private MediaPlayer player;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notify Water";
            String description = "Drink Water";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
       //  Toast.makeText(this, "Drink Water Reminder", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 59);
        Intent intent_ = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent_, 0 );
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long ten_seconds_millis = 1000 * 10;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + ten_seconds_millis, 1*60*60*1000, pendingIntent);
        // doc
        /*String email = intent.getStringExtra("email");
        Log.d("DBGPROGR", email);
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
                        int progr = (int) ((water_already_drunk+water_cup)/water_total * 100);
                        if (progr < 100) {
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + ten_seconds_millis, 1000 * 60 * 60, pendingIntent);
                        }
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });*/


       // if (progress < 100 ) {
           //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + ten_seconds_millis, 1000 * 10, pendingIntent);
        //}
        // creating a media player which
        // will play the audio of Default
        // ringtone in android device
        // player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI );

        // providing the boolean
        // value as true to play
        // the audio on loop
        // player.setLooping( true );

        // starting the process
        // player.start();

        // returns the status
        // of the program



      /*  alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent intent_ = new Intent(this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 1, intent_, 0);

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 10 * 1, alarmIntent);

        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }
*/


        return START_STICKY;
    }



    @Override

    // execution of the service will
    // stop on calling this method
    public void onDestroy() {
        super.onDestroy();

        // stopping the process
        // player.stop();
    }


    public WaterStatsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
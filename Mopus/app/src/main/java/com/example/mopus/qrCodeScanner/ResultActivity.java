package com.example.mopus.qrCodeScanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mopus.HomeActivity;
import com.example.mopus.MainActivity;
import com.example.mopus.ProfessionalActivity2;
import com.example.mopus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "ResultActivity";

    private TextView text;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        text = findViewById(R.id.textViewResult);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent data = getIntent();

        Log.d(TAG, data.getStringExtra("key"));

        JSONObject mainObject;

        try {
            mainObject = new JSONObject(data.getStringExtra( "key" ));
            String emailItem = mainObject.getString( "email" );
            String day = mainObject.getString( "date" );
            //String hour = (String) mainObject.get( "hour" );

            getStats(emailItem, day/*, hour*/);
            text.setText(emailItem + " - " + day + " - " /*+ hour*/);

            if(mainObject.has("isNewScan")) {
                // TODO: create Scan object and save it on the db BUT ONLY IF COMES FROM A NEW SCAN
                Log.d(TAG, "NEW ACTIVITY");
            }
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

    private void getStats(String emailItem, String day/*, String hour*/) {
        db.collection("water")
                .document(emailItem)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void createNewScan() {

    }
}
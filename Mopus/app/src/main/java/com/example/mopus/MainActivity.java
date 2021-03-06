package com.example.mopus;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText emailText, passwordText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = findViewById(R.id.login_email);
        passwordText = findViewById(R.id.change_info_password);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            db.collection("users")
                    .whereEqualTo("id", mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String email = (String) document.getData().get("email");
                                    Map<String, Object> user = document.getData();
                                    Intent intent = new Intent(MainActivity.this, user.get("isProfessional").toString().equals("false") ? HomeActivity.class : ProfessionalActivity2.class);
                                    intent.putExtra("email", email);

                                    try {
                                        intent.putExtra("hasMenstrualCycle", user.get("hasMenstrualCycle").toString().equals("true") ? true : false);
                                    } catch(Exception e) {
                                        intent.putExtra("hasMenstrualCycle", false);
                                    }

                                    startActivity(intent);
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public void clickToSignUp(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        /*emailText.setText("");
        passwordText.setText("");*/
    }

    public void clickToSignIn(View view) {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            db.collection("users")
                                    .whereEqualTo("id", user.getUid())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    Map<String, Object> user = document.getData();
                                                    Log.d(TAG, user.get("isProfessional").toString());
                                                    Intent intent = new Intent(MainActivity.this, user.get("isProfessional").toString().equals("false") ? HomeActivity.class : ProfessionalActivity2.class);
                                                    intent.putExtra("email", email);

                                                    try {
                                                        intent.putExtra("hasMenstrualCycle", user.get("hasMenstrualCycle").toString().equals("true") ? true : false);
                                                    } catch(Exception e) {
                                                        intent.putExtra("hasMenstrualCycle", false);
                                                    }

                                                    startActivity(intent);
                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

       /* emailText.setText("");
        passwordText.setText("");*/

    }

}
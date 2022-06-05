package com.example.mopus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mopus.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText firstName, lastName, date, phone, email, password, confirmPassword;
    private Switch isProfessionalSwitch;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        date = findViewById(R.id.register_birth_date);
        firstName = findViewById(R.id.register_first_name);
        lastName = findViewById(R.id.register_last_name);
        date = findViewById(R.id.register_birth_date);
        phone = findViewById(R.id.register_phone_number);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        confirmPassword = findViewById(R.id.register_password_confirm);
        isProfessionalSwitch = findViewById(R.id.is_professional);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        date.addTextChangedListener(new TextWatcher() {

            private String current = "";
            final String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

    });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null) {
            // TODO: handle the already login user
            /*Intent intent = new Intent(Register.this, HomeActivity.class);
            startActivity(intent);*/
        }
    }

    public void registerUser(View view) {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String birthDate = date.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String confirmPwd = confirmPassword.getText().toString().trim();
        boolean isProfessional = isProfessionalSwitch.isChecked();

        if(fName.isEmpty()) {
            Log.d("Mopus", "first name is empty");
            firstName.setError("name required");
            firstName.requestFocus();
            return;
        }

        if(lName.isEmpty()) {
            lastName.setError("name required");
            lastName.requestFocus();
            return;
        }

        if(birthDate.isEmpty()) {
            date.setError("birth day required");
            date.requestFocus();
            return;
        }

        if(phoneNumber.isEmpty()) {
            phone.setError("phone number required");
            phone.requestFocus();
            return;
        }

        if(userEmail.isEmpty()) {
            email.setError("email required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("enter a valid email");
            email.requestFocus();
            return;
        }

        if(pwd.isEmpty()) {
            password.setError("password required");
            password.requestFocus();
            return;
        }

        if(confirmPwd.isEmpty()) {
            confirmPassword.setError("please confirm password");
            confirmPassword.requestFocus();
            return;
        }

        if(confirmPwd.length() < 8) {
            confirmPassword.setError("password should be at least 8 characters long");
            confirmPassword.requestFocus();
            return;
        }

        if(!pwd.equals(confirmPwd)) {
            confirmPassword.setError("password confirmation is wrong");
            confirmPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(userEmail, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // store additional fields in firebase database
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            User user = new User(userId, fName, lName, userEmail, phoneNumber, birthDate, isProfessional);

                            Log.d("Mopus", "user ID: " + userId);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            db.collection("users")
                                    .add(user.toDB())
                                    .addOnCompleteListener(task1 -> {
                                        progressBar.setVisibility(View.GONE);
                                        if(task1.isSuccessful()) {
                                            Toast.makeText(Register.this, "Successful registration", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Register.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(Register.this, "USER TABLE " + task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(Register.this, "FIREBASE AUTH " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

//code lines 22-72 from: https://techprogrammingideas.blogspot.com/2020/05/android-edit-text-to-show-dd-mm-yyyy.html
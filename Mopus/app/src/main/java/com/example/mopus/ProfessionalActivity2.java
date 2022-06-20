package com.example.mopus;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.example.mopus.qrCode.CameraXLivePreviewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.mopus.databinding.ActivityProfessional2Binding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfessionalActivity2 extends AppCompatActivity {

    private ActivityProfessional2Binding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .build());
        }

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = ActivityProfessional2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_professional, R.id.navigation_dashboard, R.id.navigation_profile_professional)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_professional2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void logoutProfessional(View view) {
        mAuth.signOut();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void clickToChangePassword(View view) {
        Intent intent = new Intent(view.getContext(), ChangePassword.class);
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

    public void clickToScan(View view) {
        startActivity(new Intent(view.getContext(), CameraXLivePreviewActivity.class));
    }

}
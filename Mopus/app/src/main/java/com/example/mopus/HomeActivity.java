package com.example.mopus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

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
        Intent intent = new Intent(this, MainActivity.class);
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
}
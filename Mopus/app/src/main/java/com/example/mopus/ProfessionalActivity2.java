package com.example.mopus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

}
package com.example.mopus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.DrinkWater;
import com.example.mopus.R;
import com.example.mopus.databinding.FragmentHomeBinding;
import com.example.mopus.model.WaterStats;
import com.example.mopus.model.WaterStatsService;
import com.example.mopus.model.WaterTime;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class  HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private int progr = 0;
    private String email = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        email = getActivity().getIntent().getStringExtra("email");
        Log.d("Email 1", email);

        EditText wt_text = binding.inputWaterTotal;
        EditText wc_text = binding.inputWaterCup;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = db.collection("water").document(email);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Getting data from DB", "... Complete.");
                        Object wc = document.getData().get("water_cup");
                        Object wt = document.getData().get("water_total");
                        wt_text.setText(String.valueOf(wt));
                        wc_text.setText(String.valueOf(wc));
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });

        Spinner spinner_wake_up= binding.inputWakeUpTime;
        ArrayAdapter<CharSequence> adapter_wake_up_time=ArrayAdapter.createFromResource(getContext(), R.array.hours, android.R.layout.simple_spinner_item);
        adapter_wake_up_time.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_wake_up.setAdapter(adapter_wake_up_time);

        Spinner spinner_sleep = binding.inputSleepTime;
        ArrayAdapter<CharSequence> adapter_sleep=ArrayAdapter.createFromResource(getContext(), R.array.hours, android.R.layout.simple_spinner_item);
        adapter_sleep.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_sleep.setAdapter(adapter_sleep);

        UpdateProgressBar();
        Intent intent = new Intent(getContext(), WaterStatsService.class);
        intent.putExtra("email", email);
        getActivity().startService(intent);

        return root;
    }
    private void UpdateProgressBar() {
        ProgressBar progress_bar = binding.progressBarWater;
        TextView progress_bar_text = binding.progressBarWaterText;
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
                        progr = (int) ((water_already_drunk)/water_total * 100);
                        if (progr > 100) {
                            progr = 100;
                        }
                        progress_bar.setProgress(progr);
                        progress_bar_text.setText(String.valueOf(progr) + "%");
                    } else {
                        Log.d("Getting data from DB", "... Error.");
                    }
                } else {
                    Log.d(email, "Failed with: ", task.getException());
                }
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.mopus.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.R;
import com.example.mopus.databinding.FragmentHomeBinding;

public class  HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        Spinner spinner_water_total=binding.waterTotalDay;
        ArrayAdapter<CharSequence> adapter_total=ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.water_per_day, android.R.layout.simple_spinner_item);
        adapter_total.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_water_total.setAdapter(adapter_total);

        Spinner spinner_water_cup=binding.waterTotalCup;
        ArrayAdapter<CharSequence> adapter_cup=ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.water_per_cup, android.R.layout.simple_spinner_item);
        adapter_cup.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_water_cup.setAdapter(adapter_cup);

        Spinner spinner_wake_up=binding.wakeUpTime;
        ArrayAdapter<CharSequence> adapter_wake_up_time=ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.hours, android.R.layout.simple_spinner_item);
        adapter_wake_up_time.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_wake_up.setAdapter(adapter_wake_up_time);

        Spinner spinner_sleep=binding.sleepTime;
        ArrayAdapter<CharSequence> adapter_sleep=ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.hours, android.R.layout.simple_spinner_item);
        adapter_sleep.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_sleep.setAdapter(adapter_sleep);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
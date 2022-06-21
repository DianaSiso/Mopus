package com.example.mopus.ui.hiking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.R;
import com.example.mopus.databinding.FragmentHikingMapBinding;
import com.example.mopus.databinding.FragmentHomeBinding;
import com.example.mopus.ui.home.HomeViewModel;

public class HikingFragment extends Fragment {

    private FragmentHikingMapBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HikingViewModel hikingViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HikingViewModel.class);

        binding = FragmentHikingMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

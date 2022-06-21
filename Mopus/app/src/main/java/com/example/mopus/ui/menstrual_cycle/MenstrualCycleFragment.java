package com.example.mopus.ui.menstrual_cycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.databinding.FragmentHomeBinding;
import com.example.mopus.databinding.FragmentMenstrualCycleBinding;
import com.example.mopus.ui.home.HomeViewModel;

public class MenstrualCycleFragment extends Fragment {
    private FragmentMenstrualCycleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MenstrualCycleViewModel menstrualCycleViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(MenstrualCycleViewModel.class);

        binding = FragmentMenstrualCycleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

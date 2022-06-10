package com.example.mopus.ui.analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.mopus.databinding.FragmentAnalysisBinding;

public class AnalysisFragment extends Fragment {

private FragmentAnalysisBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        AnalysisViewModel dashboardViewModel =
                new ViewModelProvider(this).get(AnalysisViewModel.class);

    binding = FragmentAnalysisBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textAnalysis;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
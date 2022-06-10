package com.example.mopus.ui.home_professional;

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
import com.example.mopus.databinding.FragmentHomeProfessionalBinding;

public class HomeProfessionalFragment extends Fragment {

    private FragmentHomeProfessionalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeProfessioanlViewModel homeProfessionalViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(HomeProfessioanlViewModel.class);

        binding = FragmentHomeProfessionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeProfessionalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
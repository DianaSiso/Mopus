package com.example.mopus.ui.profile_professional;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.databinding.FragmentProfileBinding;
import com.example.mopus.databinding.FragmentProfileProfessionalBinding;

public class ProfileProfessionalFragment extends Fragment {

    private FragmentProfileProfessionalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileProfessionalViewModel notificationsViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(ProfileProfessionalViewModel.class);

        binding = FragmentProfileProfessionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
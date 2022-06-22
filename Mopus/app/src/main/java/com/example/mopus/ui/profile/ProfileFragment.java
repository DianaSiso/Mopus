package com.example.mopus.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static final String TAG = "ProfessionalActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView firstName;
    private TextView lastName;
    private TextView birthDate;
    private TextView phoneNumber;
    private TextView email;
    private TextView imc;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firstName = binding.profileFirstName;
        lastName = binding.profileLastName;
        birthDate = binding.profileBirthDate;
        phoneNumber = binding.profilePhoneNumber;
        email = binding.profileEmail;
        imc = binding.profileImc;

        profileViewModel.getFirstName().observe(getViewLifecycleOwner(), firstName::setText);
        profileViewModel.getLastName().observe(getViewLifecycleOwner(), lastName::setText);
        profileViewModel.getBirthDate().observe(getViewLifecycleOwner(), birthDate::setText);
        profileViewModel.getPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber::setText);
        profileViewModel.getEmail().observe(getViewLifecycleOwner(), email::setText);
        profileViewModel.getIMC().observe(getViewLifecycleOwner(), imc::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
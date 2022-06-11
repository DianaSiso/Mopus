package com.example.mopus.ui.profile_professional;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mopus.HomeActivity;
import com.example.mopus.MainActivity;
import com.example.mopus.ProfessionalActivity2;
import com.example.mopus.databinding.FragmentProfileProfessionalBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class ProfileProfessionalFragment extends Fragment {

    private static final String TAG = "ProfessionalActivity";

    private FragmentProfileProfessionalBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView firstName;
    private TextView lastName;
    private TextView birthDate;
    private TextView phoneNumber;
    private TextView email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileProfessionalViewModel notificationsViewModel =
                new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.NewInstanceFactory()).get(ProfileProfessionalViewModel.class);

        binding = FragmentProfileProfessionalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstName = binding.profileFirstName;
        lastName = binding.profileLastName;
        birthDate = binding.profileBirthDate;
        phoneNumber = binding.profilePhoneNumber;
        email = binding.profileEmail;
        email.setText(mAuth.getCurrentUser().getEmail());

        db.collection("users")
                .whereEqualTo("id", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Map<String, Object> user = document.getData();
                                Log.d(TAG, String.valueOf(user.get("firstName")));
                                firstName.setText(String.valueOf(user.get("firstName")));
                                lastName.setText(String.valueOf(user.get("lastName")));
                                birthDate.setText(user.get("birthDay") == null ? "Birth Date" : String.valueOf(user.get("birthDay")));
                                phoneNumber.setText(user.get("phone") == null ? "Phone Number" : String.valueOf(user.get("phone")));
                                break;
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
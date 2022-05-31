package com.example.mopus.ui.profile;

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

import com.example.mopus.ChangeInfo;
import com.example.mopus.ChangePassword;
import com.example.mopus.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void clickToChangePassword(View view) {
        Intent intent = new Intent(view.getContext(), ChangePassword.class);
        startActivity(intent);
    }

    public void clickToChangeInfo(View view) {
        Log.d("clickID", String.valueOf(view.getId()));
        String id = String.valueOf(view.getId());
        String from = "";
        switch (id) {
            case "2131165334":
                from = "first_name";
                break;
            case "2131165343":
                from = "last_name";
                break;
            case "2131165337":
                from = "phone_number";
                break;
            case "2131165333":
                from = "email";
        }
        Intent intent = new Intent(view.getContext(), ChangeInfo.class);
        intent.putExtra("From", from);
        startActivity(intent);
    }
}
package com.example.mopus.ui.profile_professional;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileProfessionalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    //private FirebaseAuth mAuth;

    public ProfileProfessionalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");

        //mAuth = FirebaseAuth.getInstance();

    }

    public LiveData<String> getText() {
        return mText;
    }
}

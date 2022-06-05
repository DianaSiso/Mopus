package com.example.mopus.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    //private FirebaseAuth mAuth;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment");

        //mAuth = FirebaseAuth.getInstance();

    }

    public LiveData<String> getText() {
        return mText;
    }
}

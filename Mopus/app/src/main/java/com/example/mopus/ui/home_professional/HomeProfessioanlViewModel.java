package com.example.mopus.ui.home_professional;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeProfessioanlViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeProfessioanlViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
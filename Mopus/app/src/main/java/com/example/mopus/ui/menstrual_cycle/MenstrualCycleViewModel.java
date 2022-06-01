package com.example.mopus.ui.menstrual_cycle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MenstrualCycleViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MenstrualCycleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Menstrual Cycle fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

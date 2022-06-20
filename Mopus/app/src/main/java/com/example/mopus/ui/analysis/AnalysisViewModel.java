package com.example.mopus.ui.analysis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AnalysisViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mInstructionText;

    public AnalysisViewModel() {
        mText = new MutableLiveData<>();
        mInstructionText = new MutableLiveData<>();

        mText.setValue("Click to scan the QR code with the person's health information");
        mInstructionText.setValue("The person should go to the statistics page and click the button with the QR code");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getInstructionsText() {
        return mInstructionText;
    }
}
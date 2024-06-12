package com.example.leddemo3.ui.lightness;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LightnessViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LightnessViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is lightness fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
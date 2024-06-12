package com.example.leddemo3.ui.threemode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ThreeModeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ThreeModeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
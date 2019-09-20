package com.example.yourfood.ui.spesa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpesaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SpesaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is spesa fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
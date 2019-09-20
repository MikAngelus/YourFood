package com.example.yourfood.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Compila i dati per aggiungere un nuovo prodotto!");
    }

      

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.myapplication1.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication1.Repository;
import com.example.myapplication1.network.home.ValueResponse;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
      mText.setValue("");
    }
    private  LiveData resultData = Repository.getValueData();

    public LiveData<ValueResponse> getValue(){//提供livedata

            return Repository.getValueData();
    }

    public LiveData<String> getText() {
        return mText;
    }
}
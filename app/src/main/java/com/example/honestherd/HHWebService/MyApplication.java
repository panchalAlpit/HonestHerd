package com.example.honestherd.HHWebService;

import android.app.Application;

import com.example.honestherd.HHGlobal.Utils;
import com.hypertrack.sdk.HyperTrack;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HyperTrack.getInstance(this.getApplicationContext(),Utils.publishKey);
//        HyperTrack.initialize(this.getApplicationContext(), Utils.publishKey);
    }
}

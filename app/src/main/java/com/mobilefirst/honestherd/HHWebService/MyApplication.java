package com.mobilefirst.honestherd.HHWebService;

import android.app.Application;

import com.mobilefirst.honestherd.HHGlobal.Utils;
import com.hypertrack.sdk.HyperTrack;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HyperTrack.getInstance(this.getApplicationContext(),Utils.publishKey);
//        HyperTrack.initialize(this.getApplicationContext(), Utils.publishKey);
    }
}

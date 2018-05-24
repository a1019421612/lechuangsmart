package com.hbdiye.lechuangsmart;

import android.app.Application;

import com.coder.zzq.smartshow.toast.SmartToast;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SmartToast.plainToast(this);
    }
}

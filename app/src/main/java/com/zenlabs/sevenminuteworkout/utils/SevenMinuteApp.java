package com.zenlabs.sevenminuteworkout.utils;

import android.app.Application;

import me.kiip.sdk.Kiip;

/**
 * Created by madarashunor on 20/11/15.
 */
public class SevenMinuteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Kiip kiip = Kiip.init(this, UtilsValues.KIIP_KEY, UtilsValues.KIIP_SECRET);
        kiip.setTestMode(false);
        Kiip.setInstance(kiip);
    }
}
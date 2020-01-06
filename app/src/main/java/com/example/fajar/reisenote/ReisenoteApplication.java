package com.example.fajar.reisenote;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ReisenoteApplication extends Application {
    static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        MediaManager.init(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Muli-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

}
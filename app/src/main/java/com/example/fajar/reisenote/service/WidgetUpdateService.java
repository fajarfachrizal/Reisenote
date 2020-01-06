package com.example.fajar.reisenote.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.fajar.reisenote.ReiseWidgetProvider;
import com.example.fajar.reisenote.data.ReiseData;
import com.example.fajar.reisenote.utils.Constant;

public class WidgetUpdateService extends IntentService {
    public static final String UpdateWidget = "updateWidget";


    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    public static void startActionReiseWidget(Context context,
                                              ReiseData reiseData, int reisePosition) {

        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.putExtra(Constant.reiseData, reiseData);
        intent.putExtra(Constant.reisePosition, reisePosition);

        context.startService(intent);


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ReiseWidgetProvider.class));

        Bundle data = intent.getExtras();

        ReiseData reiseData = (ReiseData) data.get(Constant.reiseData);
        int reisePosition = data.getInt(Constant.reisePosition);

        ReiseWidgetProvider.updateAppWidgets(this, appWidgetManager,
                appWidgetIds, reiseData, reisePosition);


    }


}

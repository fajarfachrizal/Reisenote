package com.example.fajar.reisenote;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;

import com.example.fajar.reisenote.data.ReiseData;

public class ReiseWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, ReiseData reiseData, int reisePosition) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.reisenote_widget_view);

        if (reiseData != null) {
            remoteViews.setViewVisibility(R.id.reise_widget_title_id, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.reise_widget_desc_id, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.empty_view, View.GONE);
        } else {
            remoteViews.setViewVisibility(R.id.reise_widget_title_id, View.GONE);
            remoteViews.setViewVisibility(R.id.reise_widget_desc_id, View.GONE);
            remoteViews.setViewVisibility(R.id.empty_view, View.VISIBLE);
            return;
        }

        remoteViews.setEmptyView(R.id.widget_layout_id, R.id.empty_view);
        remoteViews.setTextViewText(R.id.reise_widget_desc_id, reiseData.getReiseDesc());
        remoteViews.setTextViewText(R.id.reise_widget_title_id, reisePosition + 1 + ". " + reiseData.getReiseTitle());
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                        int[] appWidgetIds, ReiseData reiseData, int reisePosition) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, reiseData, reisePosition);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null, 0);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


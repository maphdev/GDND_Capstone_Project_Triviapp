package com.capstone.maphdev.triviapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.capstone.maphdev.triviapp.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class QuizWidgetProvider extends AppWidgetProvider {

    private static final String WIDGET_UPDATE = "com.capstone.maphdev.triviapp.widget";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quiz_widget);

        SharedPreferences sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE);
        Long score = sharedPreferences.getLong("shared_score", -1);
        Long nbQuestions = sharedPreferences.getLong("shared_nb_questions", -1);

        if (score == -1 || nbQuestions == -1){
            remoteViews.setViewVisibility(R.id.widget_sign_in_sign_out, View.VISIBLE);
            remoteViews.setTextViewText(R.id.widget_sign_in_sign_out, context.getResources().getString(R.string.widget_sign_in_sign_up));
            remoteViews.setViewVisibility(R.id.widget_title, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_score, View.INVISIBLE);
        } else {
            remoteViews.setTextViewText(R.id.widget_score, Long.toString(score) + " / " + Long.toString(nbQuestions));
            remoteViews.setViewVisibility(R.id.widget_sign_in_sign_out, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_title, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_score, View.VISIBLE);
        }

        // set a PendingIntent in order to start the MainActivity when a click occurs on the widget
        Intent widgetIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), QuizWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public static void sendBroadCast(Context context, Class toClass){
        Intent intent = new Intent(context, toClass);
        intent.setAction(WIDGET_UPDATE);
        context.getApplicationContext().sendBroadcast(intent);
    }
}


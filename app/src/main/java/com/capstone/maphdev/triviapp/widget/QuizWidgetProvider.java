package com.capstone.maphdev.triviapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.activity.MainActivity;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.capstone.maphdev.triviapp.widget.RetrieveDataService;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Implementation of App Widget functionality.
 */
public class QuizWidgetProvider extends AppWidgetProvider {

    private static final String WIDGET_UPDATE = "com.capstone.maphdev.triviapp.widget";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, Long score, Long nbQuestions) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.quiz_widget);

        // if there is no connection, show a "no connection" message
        if (!NetworkUtils.isNetworkAvailable(context)){
            remoteViews.setViewVisibility(R.id.widget_sign_in_sign_out, View.VISIBLE);
            remoteViews.setTextViewText(R.id.widget_sign_in_sign_out, context.getResources().getString(R.string.no_internet_connection));
            remoteViews.setViewVisibility(R.id.widget_title, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_score, View.INVISIBLE);
        }
        // if no user is connected, ask the user to sign in or sign out
        else if (score == null && nbQuestions == null){
            remoteViews.setViewVisibility(R.id.widget_sign_in_sign_out, View.VISIBLE);
            remoteViews.setTextViewText(R.id.widget_sign_in_sign_out, context.getResources().getString(R.string.widget_sign_in_sign_up));
            remoteViews.setViewVisibility(R.id.widget_title, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_score, View.INVISIBLE);
        }
        // display the score
        else {
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
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            updateAllWidgets(context, appWidgetManager, appWidgetIds, null, null);
        } else {
            RetrieveDataService.startActionRetrieveData(context);
        }
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Long score, Long nbQuestions){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, score, nbQuestions);
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


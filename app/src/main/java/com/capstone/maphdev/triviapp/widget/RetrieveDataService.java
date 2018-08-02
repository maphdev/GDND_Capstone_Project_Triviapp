package com.capstone.maphdev.triviapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.capstone.maphdev.triviapp.utils.DataUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class RetrieveDataService extends IntentService {

    public static final String ACTION_RETRIEVE_DATA = "com.capstone.maphdev.triviapp.retrieve_data";

    public RetrieveDataService(){
        super("RetrieveDataService");
    }

    public static void startActionRetrieveData(Context context){
        Intent intent = new Intent(context, RetrieveDataService.class);
        intent.setAction(ACTION_RETRIEVE_DATA);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            if (ACTION_RETRIEVE_DATA.equals(action)) {
                handleActionRetrieveData();
            }
        }
    }

    private void handleActionRetrieveData(){
        // get the userRef in the firebase database
        DatabaseReference thisUserRef = null;
        try {
            thisUserRef = DataUtils.getDatabase().getReference().child(DataUtils.USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (Exception e){
            e.printStackTrace();
        }

        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long score = (Long)dataSnapshot.child(DataUtils.SCORE).getValue();
                Long nbQuestions = (Long)dataSnapshot.child(DataUtils.NB_QUESTIONS_ANSWERED).getValue();
                sendUpdate(score, nbQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get data " +
                        ""+databaseError);
            }
        });
    }

    public void sendUpdate(Long score, Long nbQuestions){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, QuizWidgetProvider.class));

        QuizWidgetProvider.updateAllWidgets(this, appWidgetManager, appWidgetIds, score, nbQuestions);
    }
}

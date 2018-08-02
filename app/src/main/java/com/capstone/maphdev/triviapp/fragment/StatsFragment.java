package com.capstone.maphdev.triviapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    @BindView(R.id.nbr_questions_answered) TextView nbrQuestionsAnsweredTextView;
    @BindView(R.id.nbr_correct_answers) TextView nbrCorrectAnswersTextView;
    @BindView(R.id.nbr_incorrect_answers) TextView nbrIncorrectAnswersTextView;
    @BindView(R.id.score) TextView scoreTextView;

    DatabaseReference thisUserRef;


    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

        try {
            thisUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        } catch (Exception e){
            e.printStackTrace();
        }

        populateViews();

        return rootView;
    }

    public void populateViews(){
        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                nbrQuestionsAnsweredTextView.setText(userData.getNbQuestionsAnswered());
                nbrCorrectAnswersTextView.setText(userData.getCorrectAnswers());
                nbrIncorrectAnswersTextView.setText(userData.getIncorrectAnswers());
                scoreTextView.setText(userData.getScore());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get classified ad for update " +
                        ""+databaseError);
            }
        });
    }

}

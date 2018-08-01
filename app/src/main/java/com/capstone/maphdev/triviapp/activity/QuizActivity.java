package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.fragment.CategoriesListFragment;
import com.capstone.maphdev.triviapp.fragment.QuizFragment;
import com.capstone.maphdev.triviapp.model.Question;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity implements QuizFragment.OnNextQuestionListener {

    private FirebaseAuth auth;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        auth = FirebaseAuth.getInstance();

        // if the user is not logged in anymore, then redirection to the Welcome Activity
        if (auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        // set fragment
        QuizFragment quizFragment = new QuizFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.quiz_container, quizFragment)
                .commit();
    }

    @Override
    public void onNextQuestionClick() {
        Log.v("TRY", "inside activity onclick");
        QuizFragment quizFragment = new QuizFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.quiz_container, quizFragment)
                .commit();
    }
}

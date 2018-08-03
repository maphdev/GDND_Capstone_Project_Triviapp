package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.fragment.QuizFragment;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity implements QuizFragment.OnNextQuestionListener {

    @BindView(R.id.quiz_activity_toolbar) Toolbar toolbar;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Firebase auth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // if the user is not logged in anymore, then redirection to the Welcome Activity
        if (auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        // toolbar
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setTitle(R.string.back_to_categories);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


        QuizFragment quizFragment = new QuizFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.quiz_container, quizFragment)
                .commit();
    }

    @Override
    public void onNextQuestionClick() {
        QuizFragment quizFragment = new QuizFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.quiz_container, quizFragment)
                .commit();
    }
}

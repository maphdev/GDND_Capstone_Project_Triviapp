package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.widget.QuizWidgetProvider;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            finish();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        QuizWidgetProvider.sendBroadCast(this, QuizWidgetProvider.class);
    }

    public void signInWelcome(View view){
        Intent startSignInActivity = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(startSignInActivity);
    }

    public void signUpWelcome(View view){
        Intent startSignUpActivity = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(startSignUpActivity);
    }
}

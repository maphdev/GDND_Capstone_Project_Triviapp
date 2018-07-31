package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.Question;
import com.capstone.maphdev.triviapp.utils.JsonUtils;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.capstone.maphdev.triviapp.utils.StringUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        //FirebaseUser user = auth.getCurrentUser();
        //TextView textView = (TextView) findViewById(R.id.myText);
        //textView.setText("Hello " + user.getEmail());

        Question question = NetworkUtils.getRandomQuestion();
        Log.v("azerty", question.toString());
    }



    public void signOut(View view){
        auth.signOut();
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}

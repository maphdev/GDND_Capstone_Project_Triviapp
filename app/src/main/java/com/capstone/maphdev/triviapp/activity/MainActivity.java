package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.fragment.CategoriesListFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        // if the user is not logged in anymore, then redirection to the Welcome Activity
        if (auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        // set fragment
        CategoriesListFragment categoriesListFragment = new CategoriesListFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.categories_list, categoriesListFragment)
                .commit();
    }

    public void signOut(View view){
        auth.signOut();
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
 }

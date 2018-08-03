package com.capstone.maphdev.triviapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.model.UserData;
import com.capstone.maphdev.triviapp.utils.DataUtils;
import com.capstone.maphdev.triviapp.utils.DesignUtils;
import com.capstone.maphdev.triviapp.utils.NetworkUtils;
import com.capstone.maphdev.triviapp.QuizWidgetProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.input_sign_in_email) TextInputEditText inputEmail;
    @BindView(R.id.input_sign_in_password) TextInputEditText inputPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private FirebaseAuth auth;
    private DatabaseReference thisUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        if (savedInstanceState != null){
            inputEmail.setText(savedInstanceState.getString(getString(R.string.ON_SAVE_INSTANCE_EMAIL)));
            inputPassword.setText(savedInstanceState.getString(getString(R.string.ON_SAVE_INSTANCE_PASSWORD)));
        }
    }

    public void signIn(final View view){
        if (!NetworkUtils.isNetworkAvailable(this)){
            DesignUtils.showSnackBar(view, getString(R.string.no_internet_connection), getApplicationContext());
            return;
        }

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //DesignUtils.showSnackBar(view, getString(R.string.exception_empty_email), getApplicationContext());
            return;
        }

        if (TextUtils.isEmpty(password)){
            //DesignUtils.showSnackBar(view, getString(R.string.exception_empty_password), getApplicationContext());
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email
                            catch (FirebaseAuthInvalidUserException invalidEmail) {
                                DesignUtils.showSnackBar(view, getString(R.string.exception_invalid_email), getApplicationContext());
                            }
                            // if user enters wrong password
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                                DesignUtils.showSnackBar(view, getString(R.string.exception_invalid_password), getApplicationContext());
                            }
                            // other
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (auth.getCurrentUser() != null){

                                setSharedScoreForWidget(auth);

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(getString(R.string.ON_SAVE_INSTANCE_EMAIL), inputEmail.getText().toString());
        outState.putString(getString(R.string.ON_SAVE_INSTANCE_PASSWORD), inputPassword.getText().toString());
    }

    private void setSharedScoreForWidget(FirebaseAuth auth){
        try {
            thisUserRef = DataUtils.getDatabase().getReference().child(DataUtils.USERS).child(auth.getCurrentUser().getUid());
        } catch (Exception e){
            e.printStackTrace();
        }

        thisUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);

                SharedPreferences sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("shared_score", (Long)dataSnapshot.child(DataUtils.SCORE).getValue());
                editor.putLong("shared_nb_questions", (Long)dataSnapshot.child(DataUtils.NB_QUESTIONS_ANSWERED).getValue());
                editor.apply();
                QuizWidgetProvider.sendBroadCast(getApplicationContext(), QuizWidgetProvider.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error trying to get data " +
                        ""+databaseError);
            }
        });
    }
}


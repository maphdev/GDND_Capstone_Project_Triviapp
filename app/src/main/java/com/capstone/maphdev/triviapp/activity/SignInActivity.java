package com.capstone.maphdev.triviapp.activity;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import com.capstone.maphdev.triviapp.R;
import com.capstone.maphdev.triviapp.utils.DesignUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.input_sign_in_email) TextInputEditText inputEmail;
    @BindView(R.id.input_sign_in_password) TextInputEditText inputPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private FirebaseAuth auth;

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
                                finish();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
}


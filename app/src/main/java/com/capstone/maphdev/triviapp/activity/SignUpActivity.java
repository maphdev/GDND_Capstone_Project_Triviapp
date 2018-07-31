package com.capstone.maphdev.triviapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.input_sign_up_email) TextInputEditText inputEmail;
    @BindView(R.id.input_sign_up_password) TextInputEditText inputPassword;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
    }

    public void signUp(final View view){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            DesignUtils.showSnackBar(view, getString(R.string.exception_empty_email), getApplicationContext());
            return;
        }

        if (TextUtils.isEmpty(password)){
            DesignUtils.showSnackBar(view, getString(R.string.exception_empty_password), getApplicationContext());
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()){
                            try {
                                throw task.getException();
                            }
                            // if the password is too weak
                            catch (FirebaseAuthWeakPasswordException weakPasswordException){
                                DesignUtils.showSnackBar(view, getString(R.string.exception_weak_password), getApplicationContext());
                                return;
                            }
                            // if the email is malformed
                            catch (FirebaseAuthInvalidCredentialsException invalidEmail){
                                DesignUtils.showSnackBar(view, getString(R.string.exception_malformed_email), getApplicationContext());
                                return;
                            }
                            // if the user already exists
                            catch (FirebaseAuthUserCollisionException alreadyExistException){
                                DesignUtils.showSnackBar(view, getString(R.string.exception_user_already_exists), getApplicationContext());
                                return;
                            }
                            // other
                             catch (Exception e){
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
}
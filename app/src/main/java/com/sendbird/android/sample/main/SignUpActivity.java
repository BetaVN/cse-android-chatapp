package com.sendbird.android.sample.main;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sendbird.android.sample.R;
import com.sendbird.android.sample.utils.PreferenceUtils;

public class SignUpActivity extends AppCompatActivity {

    private CoordinatorLayout mSignUpLayout;
    private TextInputEditText mUsernameEditText, mEmailEditText, mPasswordEditText, mConfirmPasswordEditText;
    private Button mLoginButton, mSignUpButton;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

        mUsernameEditText.setText(PreferenceUtils.getUsername());
        mEmailEditText.setText(PreferenceUtils.getEmail());

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();

                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                PreferenceUtils.setUsername(username);
                PreferenceUtils.setPassword(password);
                PreferenceUtils.setEmail(email);

                if (password.equals(confirmPassword)) {
                    signUpToServer(username, email, password);
                } else {
                    showSnackbar("Confirm password is not match");
                }
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        mUsernameEditText.setSelectAllOnFocus(true);
        mEmailEditText.setSelectAllOnFocus(true);
        mPasswordEditText.setSelectAllOnFocus(true);
        mConfirmPasswordEditText.setSelectAllOnFocus(true);
    }

    private void initViews() {
        mSignUpLayout = findViewById(R.id.layout_sign_up);

        mUsernameEditText = findViewById(R.id.edittext_signup_username);
        mEmailEditText = findViewById(R.id.edittext_signup_email);
        mPasswordEditText = findViewById(R.id.edittext_signup_password);
        mConfirmPasswordEditText = findViewById(R.id.edittext_signup_confirm_password);

        mLoginButton = (Button) findViewById(R.id.button_redirect_login);
        mSignUpButton = (Button) findViewById(R.id.button_signup);

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_signup);
    }

    private void signUpToServer(String username, String email, String password) {
        // Show the loading indicator
        showProgressBar(true);

        mLoginButton.setEnabled(false);
        mSignUpButton.setEnabled(false);

        // TODO
        ConnectionManager.signUp(username, email, password);

        mUsernameEditText.setText(PreferenceUtils.getUsername());

        // Hide the loading indicator
        showProgressBar(false);

        // TODO
        if (false) {
            // Error
            Toast.makeText(SignUpActivity.this, "Error Message", Toast.LENGTH_SHORT).show();

            // Show login failure snackbar
            showSnackbar("Login failed");
            mLoginButton.setEnabled(true);
            mSignUpButton.setEnabled(true);

            return;
        }

        // TODO
        // Model User
        // PreferenceUtils.setNickname(user.getNickname());
        // PreferenceUtils.setProfileUrl(user.getProfileUrl());

        // Update the user's nickname
        // updateCurrentUserInfo(password);
        // updateCurrentUserPushToken();

        // Proceed to MainActivity
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar.make(mSignUpLayout, text, Snackbar.LENGTH_SHORT).show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) mProgressBar.show();
        else mProgressBar.hide();
    }
}

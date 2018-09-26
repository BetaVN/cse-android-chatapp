package com.sendbird.android.sample.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.sample.R;
import com.sendbird.android.sample.utils.PreferenceUtils;
import com.sendbird.android.sample.utils.PushUtils;

public class LoginActivity extends AppCompatActivity {

    private CoordinatorLayout mLoginLayout;
    private TextInputEditText mUsernameEditText, mPasswordEditText;
    private Button mLoginButton, mSignUpButton;
    private ContentLoadingProgressBar mProgressBar;
    private TextView mForgetPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initViews();

        mUsernameEditText.setText(PreferenceUtils.getUsername());
        mPasswordEditText.setText(PreferenceUtils.getPassword());

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                PreferenceUtils.setUsername(username);
                PreferenceUtils.setPassword(password);

                loginToServer(username, password);
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        mForgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        mUsernameEditText.setSelectAllOnFocus(true);
        mPasswordEditText.setSelectAllOnFocus(true);
    }

    private void initViews() {
        mLoginLayout = findViewById(R.id.layout_login);

        mUsernameEditText = findViewById(R.id.edittext_login_username);
        mPasswordEditText = findViewById(R.id.edittext_login_password);

        mForgetPasswordText = findViewById(R.id.text_forget_password);

        mLoginButton = findViewById(R.id.button_login);
        mSignUpButton = findViewById(R.id.button_redirect_signup);

        // A loading indicator
        mProgressBar = findViewById(R.id.progress_bar_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Auto login when connection is live
        if (PreferenceUtils.getConnected()) {
            loginToServer(PreferenceUtils.getUsername(), PreferenceUtils.getPassword());
        }
    }

    private void loginToServer(String username, String password) {
        // Show the loading indicator
        showProgressBar(true);

        mLoginButton.setEnabled(false);
        mSignUpButton.setEnabled(false);

        // TODO
        ConnectionManager.login(username, password);

        // Hide the loading indicator
        showProgressBar(false);
        // TODO
        if (false) {
            // Error
            Toast.makeText(LoginActivity.this, "Error Message", Toast.LENGTH_SHORT).show();

            // Show login failure snackbar
            showSnackbar("Login failed");
            mLoginButton.setEnabled(true);
            mSignUpButton.setEnabled(true);

            PreferenceUtils.setConnected(false);
            return;
        }

        // TODO
        // Model User
        // PreferenceUtils.setNickname(user.getNickname());
        // PreferenceUtils.setProfileUrl(user.getProfileUrl());
        PreferenceUtils.setConnected(true);

        // Update the user's nickname
        // updateCurrentUserInfo(password);
        // updateCurrentUserPushToken();

        // Proceed to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    // private void connectToSendBird(final String username, final String password) {
    //     // Show the loading indicator
    //     showProgressBar(true);
    //     mLoginButton.setEnabled(false);
    //
    //     ConnectionManager.login(username, new SendBird.ConnectHandler() {
    //         @Override
    //         public void onConnected(User user, SendBirdException e) {
    //             // Callback received; hide the progress bar.
    //             showProgressBar(false);
    //
    //             if (e != null) {
    //                 // Error!
    //                 Toast.makeText(
    //                         LoginActivity.this, "" + e.getCode() + ": " + e.getMessage(),
    //                         Toast.LENGTH_SHORT)
    //                         .show();
    //
    //                 // Show login failure snackbar
    //                 showSnackbar("Login to SendBird failed");
    //                 mLoginButton.setEnabled(true);
    //                 PreferenceUtils.setConnected(false);
    //                 return;
    //             }
    //
    //             PreferenceUtils.setNickname(user.getNickname());
    //             PreferenceUtils.setProfileUrl(user.getProfileUrl());
    //             PreferenceUtils.setConnected(true);
    //
    //             // Update the user's nickname
    //             // updateCurrentUserInfo(password);
    //             updateCurrentUserPushToken();
    //
    //             // Proceed to MainActivity
    //             Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    //             startActivity(intent);
    //             finish();
    //         }
    //     });
    // }

    // /**
    //  * Update the user's push token.
    //  */
    // private void updateCurrentUserPushToken() {
    //     PushUtils.registerPushTokenForCurrentUser(LoginActivity.this, null);
    // }


    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT).show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) mProgressBar.show();
        else mProgressBar.hide();
    }
}

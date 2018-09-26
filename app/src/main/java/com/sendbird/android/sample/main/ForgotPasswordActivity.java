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

public class ForgotPasswordActivity extends AppCompatActivity {

    private CoordinatorLayout mForgotPasswordLayout;
    private TextInputEditText mUsernameEditText;
    private Button mLoginButton, mForgotPasswordButton;
    private ContentLoadingProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mForgotPasswordLayout = findViewById(R.id.layout_forgot_password);

        mUsernameEditText = findViewById(R.id.edittext_forgot_password_username);

        mUsernameEditText.setText(PreferenceUtils.getUsername());

        mLoginButton = findViewById(R.id.button_forgot_password_redirect_login);
        mForgotPasswordButton = findViewById(R.id.button_submit_forgot_password);
        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                PreferenceUtils.setUsername(username);

                submitResetPasswordToServer(username);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            }
        });

        mUsernameEditText.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_forgot_password);
    }

    private void submitResetPasswordToServer(String username) {
        // Show the loading indicator
        showProgressBar(true);
        mLoginButton.setEnabled(false);
        mForgotPasswordButton.setEnabled(false);

        // TODO
        ConnectionManager.forgotPassword(username);

        showProgressBar(false);

        // TODO
        if (false) {
            // Error
            Toast.makeText(ForgotPasswordActivity.this, "Error Message", Toast.LENGTH_SHORT).show();

            // Show login failure snackbar
            showSnackbar("Reset password failed");
            mLoginButton.setEnabled(true);
            mForgotPasswordButton.setEnabled(true);

            return;
        }

        // TODO

        showSnackbar("Reset password successfully!");
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mForgotPasswordLayout, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }
}

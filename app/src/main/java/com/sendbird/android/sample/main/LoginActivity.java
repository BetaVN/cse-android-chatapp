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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mLoginLayout = (CoordinatorLayout) findViewById(R.id.layout_login);

        mUsernameEditText = (TextInputEditText) findViewById(R.id.edittext_login_username);
        mPasswordEditText = (TextInputEditText) findViewById(R.id.edittext_login_password);

        mUsernameEditText.setText(PreferenceUtils.getUserId());
        mPasswordEditText.setText(PreferenceUtils.getNickname());

        mLoginButton = (Button) findViewById(R.id.button_login);
        mSignUpButton = (Button) findViewById(R.id.button_redirect_signup);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                // Remove all spaces from userID
                username = username.replaceAll("\\s", "");

                String password = mPasswordEditText.getText().toString();

                PreferenceUtils.setUsername(username);
                PreferenceUtils.setPassword(password);

                // connectToSendBird(username, password);
                loginToServer(username, password);

            }
        });

        mUsernameEditText.setSelectAllOnFocus(true);
        mPasswordEditText.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);

        // Display current SendBird and app versions in a TextView
        // String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
        //         BaseApplication.VERSION, SendBird.getSDKVersion());
        // ((TextView) findViewById(R.id.text_login_versions)).setText(sdkVersion);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferenceUtils.getConnected()) {
            // connectToSendBird(PreferenceUtils.getUserId(), PreferenceUtils.getNickname());
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

        showProgressBar(false);

        // TODO
        if (false) {
            // Error
            Toast.makeText(
                    LoginActivity.this, "Error Message",
                    Toast.LENGTH_SHORT)
                    .show();

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
        updateCurrentUserPushToken();

        // Proceed to MainActivity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
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

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(LoginActivity.this, null);
    }

    // /**
    //  * Updates the user's nickname.
    //  * @param userNickname  The new nickname of the user.
    //  */
    // private void updateCurrentUserInfo(final String userNickname) {
    //     SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
    //         @Override
    //         public void onUpdated(SendBirdException e) {
    //             if (e != null) {
    //                 // Error!
    //                 Toast.makeText(
    //                         LoginActivity.this, "" + e.getCode() + ":" + e.getMessage(),
    //                         Toast.LENGTH_SHORT)
    //                         .show();
    //
    //                 // Show update failed snackbar
    //                 showSnackbar("Update user nickname failed");
    //
    //                 return;
    //             }
    //
    //             PreferenceUtils.setNickname(userNickname);
    //         }
    //     });
    // }


    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);
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

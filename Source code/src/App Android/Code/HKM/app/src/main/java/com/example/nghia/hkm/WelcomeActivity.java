package com.example.nghia.hkm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghia.hkm.helper.CheckForNetworkState;
import com.example.nghia.hkm.helper.NetworkStateReceiver;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    TextView tvSkip;
    ProgressBar progressBar;
    CheckForNetworkState checker;
    AccessToken accessToken;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView tvdetails;
    private ProfilePictureView profilePictureView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_welcome);
        addControls();
        checker = new CheckForNetworkState(this);
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        tvdetails = (TextView) findViewById(R.id.text);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(WelcomeActivity.this, "Successful", Toast.LENGTH_LONG).show();
                if (checker.isNetworkAvailable())
                    moveToMainActivity(true);
            }

            @Override
            public void onCancel() {
                Toast.makeText(WelcomeActivity.this, "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(WelcomeActivity.this, "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });
        tvSkip.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, callback);
    }

    BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateReceiver.IS_NETWORK_AVAILABLE, false);
            Log.d(TAG, "receiver: " + isNetworkAvailable);
            if (!isNetworkAvailable)
                showAlertDialogNetworkStateChange();
            else {
                if (accessToken != null) {
                    moveToMainActivity(true);
                }
            }

        }
    };

    private void showAlertDialogNetworkStateChange() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("NETWORK NOT AVAILABLE")
                .setMessage("You need to connect to network");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        registerReceiver(updateUIReceiver, new IntentFilter(
                NetworkStateReceiver.UPDATE_UI_FROM_BROADCAST_CHANGE_NETWORK_STATE)
        );
        if (accessToken != null) {
            if (checker.isNetworkAvailable())
                moveToMainActivity(true);
            else {
                Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        unregisterReceiver(updateUIReceiver);
    }

    private void addControls() {
        tvSkip = (TextView) findViewById(R.id.tvSkip);

      progressBar = (ProgressBar) findViewById(R.id.progressBar);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    private void moveToMainActivity(boolean isLogin) {
        Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        mainIntent.putExtra(IS_LOGIN, isLogin);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (!checker.isNetworkAvailable())
            Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_SHORT).show();
        else
            moveToMainActivity(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Must set callbackManager.onActivityResult(requestCode, resultCode, data);
        // to create facebook login button callback to get results of your login
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            // Application code
                            try {
                                Log.d("ID", object.getString("id"));

                                String fnm = object.getString("first_name");
                                String lnm = object.getString("last_name");
                                String mail = object.getString("email");
                                String gender = object.getString("gender");
                                String fid = object.getString("id");

                                tvdetails.setText("User Name: " +fnm + " " + lnm + "\n Email: " + mail + " \n Sex: " + gender + " \n ID: " + fid + " \n");
                                profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
                                profilePictureView.setProfileId(object.getString("id"));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
            request.setParameters(parameters);
            request.executeAsync();
            moveToMainActivity(true);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }


    };
}

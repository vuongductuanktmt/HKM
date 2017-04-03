package com.example.nghia.hkmai;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nghia.hkmai.helper.CheckForNetworkState;
import com.example.nghia.hkmai.helper.NetworkStateReceiver;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    TextView tvSkip;
    ProgressBar progressBar;

    CallbackManager callbackManager;

    CheckForNetworkState checker;

    LoginButton loginButton;

    AccessToken accessToken;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_welcome);
        addControls();
        checker = new CheckForNetworkState(this);
        callbackManager = CallbackManager.Factory.create();

        accessToken = AccessToken.getCurrentAccessToken();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("TEST", "THANH CONG");
                if (checker.isNetworkAvailable())
                    moveToMainActivity(true);
            }

            @Override
            public void onCancel() {
                Log.d("TEST", "TAT DANG NHAP");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(WelcomeActivity.this, "Thiết bị chưa được kết nối Internet", Toast.LENGTH_SHORT).show();
                Log.d("TEST", "LOI ROI: ");
            }
        });

        tvSkip.setOnClickListener(this);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.trungnguyen.newsapp",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
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


}

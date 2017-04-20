//package com.example.nghia.hkm;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.widget.Toast;
//
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.Profile;
//import com.facebook.ProfileTracker;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//
///**
// * Created by Nghia on 10/04/2017.
// */
//
//public class User_profile extends AppCompatActivity {
//    private CallbackManager callbackManager;
//    private AccessTokenTracker accessTokenTracker;
//    private ProfileTracker profileTracker;
//
//    @Override
//    protected void onCreate(Bundle saveInstanceState) {
//        super.onCreate(saveInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        setContentView(R.layout.activity_welcome);
//        callbackManager = CallbackManager.Factory.create();
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken currentToken) {
//
//
//            }
//        };
//        profileTracker = new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                nextActivity(newProfile);
//
//            }
//        };
//        accessTokenTracker.startTracking();
//        profileTracker.startTracking();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
//        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Profile profile = Profile.getCurrentProfile();
//                nextActivity(profile);
//                Toast.makeText(getApplicationContext(), "Loggin in...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        };
//        loginButton.setReadPermissions("user_friends");
//        loginButton.registerCallback(callbackManager, callback);
//    }
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        Profile profile = Profile.getCurrentProfile();
//        nextActivity(profile);
//    }
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//    }
//    protected void onStop()
//    {
//        super.onStop();
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int reponseCode, Intent intent){
//        super.onActivityResult(requestCode,reponseCode,intent);
//        callbackManager.onActivityResult(requestCode,requestCode,intent);
//    }
//    private void nextActivity(Profile profile)
//    {
//        if (profile != null){
//            Intent main = new Intent(User_profile.this,UserProfile.class);
//                    main.putExtra("name",profile.getFirstName());
//                    main.putExtra("surname",profile.getLastName());
//                    main.putExtra("ImageUrl",profile.getProfilePictureUri(200,200).toString());
//        }
//    }
//
//
//}
//

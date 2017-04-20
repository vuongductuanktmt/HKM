//package com.example.nghia.hkm;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.widget.ShareDialog;
//
//import java.io.InputStream;
//
///**
// * Created by Nghia on 10/04/2017.
// */
//
//public class UserProfile extends AppCompatActivity{
//    private ShareDialog shareDialog;
//    private Button logout;
//    @Override
//    protected void onCreate(Bundle saveInstanceState){
//        super.onCreate(saveInstanceState);
//        FacebookSdk.sdkInitialize(this);
//        setContentView(R.layout.user_profile);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        shareDialog = new ShareDialog(this);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareLinkContent content = new ShareLinkContent.Builder().build();
//                shareDialog.show(content);
//
//            }
//        });
//        Bundle inBundle = getIntent().getExtras();
//        String name = inBundle.get("name").toString();
//        String surname = inBundle.get("surname").toString();
//        String imageUrl = inBundle.get("imageUrl").toString();
//        TextView nameView = (TextView) findViewById(R.id.nameAndSurname);
//        nameView.setText("" + name + " " + surname);
//        Button logout = (Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginManager.getInstance().logOut();
//                Intent login = new Intent(UserProfile.this,WelcomeActivity.class);
//                startActivity(login);
//                finish();
//            }
//        });
//    new UserProfile.DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);
//        }
//    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
//        ImageView bmImage;
//        public DownloadImage(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//        protected Bitmap doInBackground(String... urls){
//            String urldisplay = urls [0];
//            Bitmap mIconll = null;
//            try{
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIconll = BitmapFactory.decodeStream(in);
//            }catch(Exception e){
//                Log.e("Error",e.getMessage());
//                e.printStackTrace();
//            }
//            return  mIconll;
//    }
//        protected void onPostExecute(Bitmap result){
//            bmImage.setImageBitmap(result);
//        }
//}
//}

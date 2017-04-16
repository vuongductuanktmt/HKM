package com.example.nghia.hkm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nghia.hkm.Adapter.ViewPagerAdapter;
import com.facebook.login.LoginManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    Toolbar toolbar;
    TabLayout tab;
    Button btSearch;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
//    AccessToken mAccessToken;
//    String facebookUserName;
    boolean isUserLogin = false;
//    String loginMenuTitle;
//    String facebookPictureUrl;
    FragmentManager fragmentManager;
//    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        control();
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getSupportFragmentManager(),bundle);
        viewPager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(viewPager);

    }
    private void control() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tab = (TabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        btSearch = (Button) findViewById(R.id.btSearch);
        btSearch.setOnClickListener(this);


    }
//    private void changeViewPagerPage(final int position) {
//        viewPager.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                viewPager.setCurrentItem(position, true);
//            }
//        }, 100);
//    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Thoát ứng dụng")
                    .setMessage("Bạn có muốn thoát ứng dụng?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.thongbao) {
            Toast.makeText(this, "Bạn đã click vào Thông báo ", Toast.LENGTH_SHORT).show();
//            drawerLayout.openDrawer(GravityCompat.START);
        }
        if (id == R.id.danhsachmongmuon) {
            Toast.makeText(this, "Bạn đã click vào Danh sách mong muốn ", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.caidat) {
            Toast.makeText(this, "Bạn đã click vào Cài đặt ", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.trogiup) {
            Toast.makeText(this, "Bạn đã click vào Trợ giúp ", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.dangxuat) {
            Toast.makeText(this, "Bạn đã click vào Đăng xuất ", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
            isUserLogin = false;
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.spdyt:
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                FragmentSPDYT fragmentSPDYT= new FragmentSPDYT();
//                transaction.replace(R.id.content_layout,fragmentSPDYT);
//                transaction.commit();
                ;break;
            case R.id.spbcn:
//                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
//                FragmentSPBCN fragmentSPBCN= new FragmentSPBCN();
//                transaction1.replace(R.id.content_layout,fragmentSPBCN);
//                transaction1.commit();
                ;break;
            case R.id.spdmnn:
//                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
//                FragmentSPDMNN fragmentSPDMNN= new FragmentSPDMNN();
//                transaction2.replace(R.id.content_layout,fragmentSPDMNN);
//                transaction2.commit();
                ;break;
            case R.id.spdnnt:
//                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
//                FragmentSPDNNT fragmentSPDNNT= new FragmentSPDNNT();
//                transaction3.replace(R.id.content_layout,fragmentSPDNNT);
//                transaction3.commit();
                ;break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent searchIntent = new Intent(MainActivity.this, SearchingActivity.class);
        startActivity(searchIntent);
    }
    }


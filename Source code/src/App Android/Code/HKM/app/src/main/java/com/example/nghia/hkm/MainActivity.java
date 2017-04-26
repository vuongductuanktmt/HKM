package com.example.nghia.hkm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nghia.hkm.Adapter.CustomAdapter;
import com.example.nghia.hkm.Adapter.ViewPagerAdapter;
import com.example.nghia.hkm.Class.ClientToServer;
import com.example.nghia.hkm.Class.Products;
import com.example.nghia.hkm.Class.ServerToClient;
import com.example.nghia.hkm.Fragment.FragmentSPDYT;
import com.facebook.login.LoginManager;

import org.bson.Document;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IS_LOGIN = "is_login";
    public WebSocketClient mWebSocketClient;
    public static String message;
    public static List<Document> mDocuments = new ArrayList<Document>();
    private List<Products> newList = new ArrayList<>();
    public CustomAdapter mAdapter;
    public  ViewPagerAdapter mViewPagerAdapter;
    public WebSocketClient mRegister;
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
    private  ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        //
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
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), new Bundle());
        viewPager.setAdapter(mViewPagerAdapter);
        tab.setupWithViewPager(viewPager);
//        mAdapter = new CustomAdapter(this, newList);
//        connectWebSocket(0);
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

    private void changeViewPagerPage(final int position) {
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position, true);
            }
        }, 100);
    }


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
        if (id == R.id.spdyt)
            changeViewPagerPage(0);
        else if (id == R.id.spbcn)
            changeViewPagerPage(1);
        else if (id == R.id.spdmnn)
            changeViewPagerPage(2);
        else if (id == R.id.spdnnt)
            changeViewPagerPage(3);
        drawerLayout.openDrawer(GravityCompat.START);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent searchIntent = new Intent(MainActivity.this, SearchingActivity.class);
        startActivity(searchIntent);
    }

    private void register() {
        URI uriR = null;
        try {
            uriR = new URI("ws://130.211.149.139:6969/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mRegister = new WebSocketClient(uriR) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("Socket", "Open 1");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Websocket Open 1", Toast.LENGTH_LONG).show();
                    }
                });
                ClientToServer Rst = new ClientToServer("LuuHung", "user", "register_user", "UserClient");
                mRegister.send(Rst.request());
            }

            @Override
            public void onMessage(final String s1) {
                Log.d("Message", s1);

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };
    }

    public void connectWebSocket(final int i,final String Topic) {
        URI uri;
        try {
            uri = new URI("ws://130.211.149.139:6969/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.d("Socket", "Open 2");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Đang tải, vui lòng chờ ...", Toast.LENGTH_SHORT).show();
                    }
                });

                ClientToServer Cts = new ClientToServer("LuuHung", "user", "get_pagination", "TableWebInfo", Topic , "*", 25, i+1);
                 mWebSocketClient.send(Cts.request());
//                Cts = new ClientToServer("LuuHung", "user", "get_pagination", "TableWebInfo", "Event", "*", 15, 2);
//                 mWebSocketClient.send(Cts.request());
//                onGetLink();
            }

            @Override
            public void onMessage(final String s) {
                Log.d("message", s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (s != null) {
                                if (!s.equals("true") && !s.equals("false") && s != null) {
                                    mDocuments = ServerToClient.getDataListProducts(s);
                                    message = s;
                                    Log.d("data", mDocuments.toString());
                                    for (Document mDocument : mDocuments) {
                                        //List<Document> products = (List<Document>) mDocument.get("Product");
                                        String title = mDocument.getString("__Title__");
                                        Log.d("in ra", title);
                                        newList = ServerToClient.GetListArrayProducts(message);
                                    }
                                    Log.d("List", newList.toString());
                                    Log.d("SIze", String.valueOf(newList.size()));

//                                    for(Products AHIHI : newList){
//                                        AHIHI.get__Title__();
//                                    }
//                                    mAdapter.notifyDataSetChanged();
                                    Fragment fragment = mViewPagerAdapter.getItem(viewPager.getCurrentItem());
                                    ((FragmentSPDYT) fragment).PushData(newList);
//                                    Log.d("newList", newList.toString());
                                    break;
                                }
                            }

                        }
                    }

                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

//    public void onGetLink()
//    {
//        JSONObject request = new JSONObject();
//        try {
//            request.put("__Request__", "get_pagination"); // thông tin reuest lên server
//            request.put("__User__","");			// thông tin user
//            request.put("__Password__",""); // thông tin password của người dùng
//            request.put("__Security__",""); // Thông tin bảo mật
//            request.put("__PageSize__",15); //Số lượng record của 1 trang
//            request.put("__PageNumber__",1); // Số trang hiện tại
//            request.put("__Var__", ""); // tên biến
//            request.put("__Value__",""); // giá tri của biến
//            request.put("__Condition__",""); // điều kiện để lọc
//            request.put("__Authorities__","admin"); // quyền sử dụng
//            request.put("__Table__","TableWebInfo"); // collection truy vấn
//            request.put("__Search__","*"); // giá trị cần tìm
//            request.put("__Status__","Promotion"); // thuộc loại sản phẩm nào
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mWebSocketClient.send(request.toString());
////        documents = ServerToClient.getDataListProducts(data);
}






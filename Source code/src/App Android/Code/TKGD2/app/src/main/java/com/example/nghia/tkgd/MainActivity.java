package com.example.nghia.tkgd;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.v4.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

import CustomAdapter.AdapterListViewDrawer;
import DT0.MenuDrawer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    String itemdrawer[] = {"Hình ảnh","Phim"};
    int itemhinhanh[] = {R.drawable.ic_menu_gallery,R.drawable.ic_menu_manage};
    List<MenuDrawer> listDrawer;
    ListView lvmenudrawer;
    Button btSearch;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listDrawer = new ArrayList<MenuDrawer>();
        for (int i=0;i<itemdrawer.length;i++) {
            MenuDrawer item = new MenuDrawer();
            item.setHinhanh(itemhinhanh[i]);
            item.setTenMenu(itemdrawer[i]);
            listDrawer.add(item);
        }
        lvmenudrawer = (ListView) findViewById(R.id.lvMenuDrawer);
        AdapterListViewDrawer adapter = new AdapterListViewDrawer(this,R.layout.customdrawerlayout,listDrawer);

        lvmenudrawer.setAdapter(adapter);
    }
//    @Override
//  public void onBackPressed() {
//        if (drawerLayout.openDrawer(GravityCompat.START);
//            drawerLayout.closeDrawer(GravityCompat.START);
//        else {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setTitle("Thoát ứng dụng")
//                    .setMessage("Bạn có muốn thoát ứng dụng?")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            finish();
//                        }
//                    });
//            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            dialog.show();
//        }
//    }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    private void addControls() {
//        toolbar = (Toolbar) findViewById(R.id.toolbarID);
//        tab = (TabLayout) findViewById(R.id.tab);
//        viewPager = (ViewPager) findViewById(R.id.viewPager);
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(MainActivity.this);
//        btSearch = (Button) findViewById(R.id.btSearch);
//        btSearch.setOnClickListener(this);
//    }

    private void changeViewPagerPage(final int position) {
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(position, true);
            }
        }, 100);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.thongbao)
            changeViewPagerPage(0);
        else if (id == R.id.danhsachmongmuon)
            changeViewPagerPage(1);
        else if (id == R.id.donhangcuatoi)
            changeViewPagerPage(2);
        else if (id == R.id.diachicuatoi)
            changeViewPagerPage(3);
        else if (id == R.id.caidat)
            changeViewPagerPage(4);
        else if (id == R.id.trogiup)
            changeViewPagerPage(5);
        else if (id == R.id.dangxuat)
            changeViewPagerPage(6);
        else changeViewPagerPage(7);
        Log.d("KIEMTRA", "onNavigationItemSelected " + viewPager.getCurrentItem());
        return true;
    }
    public void onClick(View view) {
        Intent searchIntent = new Intent(MainActivity.this, SearchingActivity.class);
        startActivity(searchIntent);
    }
}
//}

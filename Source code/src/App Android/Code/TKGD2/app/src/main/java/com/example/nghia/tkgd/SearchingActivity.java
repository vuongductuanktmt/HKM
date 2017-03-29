package com.example.nghia.tkgd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by Nghia on 24/03/2017.
 */

public class SearchingActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    SearchView searchView;
    String [] Action ={"Hình ảnh","Phim"};
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Action);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false); // TODO: auto focus to search view, do not clicking on search button
    }
    public boolean onQueryTextSubmit(String query) {
        if (!query.equals("")) {
            Log.d("Search", "onQueryTextSubmit 1 " + query);
            searchView.clearFocus(); //TODO: fix onQueryTextSubmit called twice
            // On real device the method just called once
            Log.d("Search", "onQueryTextSubmit 2 " + query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Search", "onQueryTextChange " + newText);
        return false;
    }
}

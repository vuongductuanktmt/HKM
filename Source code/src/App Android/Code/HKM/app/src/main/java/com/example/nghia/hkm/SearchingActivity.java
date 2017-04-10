package com.example.nghia.hkm;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.EditText;

public class SearchingActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    SearchView searchView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchView = (SearchView) findViewById(R.id.searchView);
        EditText editText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(Color.parseColor("#CC37474F"));
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false); // TODO: auto focus to search view, do not clicking on search button
    }

    @Override
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

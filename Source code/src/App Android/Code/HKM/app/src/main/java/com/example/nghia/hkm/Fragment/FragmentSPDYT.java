package com.example.nghia.hkm.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.nghia.hkm.Adapter.ExpandableAdapter;
import com.example.nghia.hkm.DetailActivity;
import com.example.nghia.hkm.ExpandableListViewImp;
import com.example.nghia.hkm.MainActivity;
import com.example.nghia.hkm.R;
import com.example.nghia.hkm.helper.CheckForNetworkState;
import com.example.nghia.hkm.model.News;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nghia on 12/04/2017.
 */

public class FragmentSPDYT extends Fragment implements ExpandableListViewImp,ExpandableListView.OnGroupClickListener,SwipyRefreshLayout.OnRefreshListener {
    List<News> newsList;
    ExpandableListView expListView;
    SwipyRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;
    CheckForNetworkState checker;
    boolean isLogin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.expandablelistview_spbcn,container,false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
        checker = new CheckForNetworkState(getContext());
        newsList = new ArrayList<News>();
        newsList.add(new News(1, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(2, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(3, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(4, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(5, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(6, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(7, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(8, getString(R.string.title), "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(9, getString(R.string.title), "Minh Nghia", getString(R.string.des)));

        addControls(mview);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expListView.setNestedScrollingEnabled(true);
        } else {
//            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mSwipeLayout.getLayoutParams();
//            params.bottomMargin = appBarLayout.getHeight();
//            mSwipeLayout.setLayoutParams(params);
            // TODO fix error on API < 20 devices
        }
        return mview;
    }

    private void addControls(View mView) {
        mSwipeLayout = (SwipyRefreshLayout) mView.findViewById(R.id.swipeToRefresh);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);


        expListView = (ExpandableListView) mView.findViewById(R.id.lvExp);
        expListView.setOnGroupClickListener(this);

        appBarLayout = (AppBarLayout) mView.findViewById(R.id.appbar);

        ExpandableAdapter adapter = new ExpandableAdapter(getContext(), newsList, expListView);
        expListView.setAdapter(adapter);
        adapter.setOnButtonClickExpand(this);
    }

    @Override
    public void onButtonExpandableClick(int groupPosition) {
        Log.d("KIEMTRA", "Đã click MORE");
        if (expListView.isGroupExpanded(groupPosition))
            expListView.collapseGroup(groupPosition);
        else expListView.expandGroup(groupPosition);
    }
    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        Log.d("KIEMTRA", "BUTTON");
        if (checker.isNetworkAvailable()) {
            Intent intent = new Intent(getActivity(),DetailActivity.class);
            getActivity().startActivity(intent);
        }
        // Must return true to remove action expand or collapse when we click on Group (not button)
        return true;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        mSwipeLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Test", "RUN HANDLER");
                mSwipeLayout.setRefreshing(false);
            }
        }, 3000);
        Log.d("TEST", "REFRESHING");
    }
}

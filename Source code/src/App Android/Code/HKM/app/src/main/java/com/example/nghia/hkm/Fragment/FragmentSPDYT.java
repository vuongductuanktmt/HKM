package com.example.nghia.hkm.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.nghia.hkm.Adapter.ExpandableAdapter;
import com.example.nghia.hkm.CommentActivity;
import com.example.nghia.hkm.ExpandableListViewImp;
import com.example.nghia.hkm.MainActivity;
import com.example.nghia.hkm.R;
import com.example.nghia.hkm.helper.CheckForNetworkState;
import com.example.nghia.hkm.model.Comment;
import com.example.nghia.hkm.model.News;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class FragmentSPDYT extends Fragment implements ExpandableListViewImp,ExpandableListView.OnGroupClickListener,SwipyRefreshLayout.OnRefreshListener {
    List<News> newsList;
    ExpandableListView expListView;
    SwipyRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;
    CheckForNetworkState checker;
    boolean isLogin = false;
    public static final String COMMENT = "comment";
    private static final String COMMENT_DIALOG = "comment_dialog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.expandablelistview_spbcn, container, false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
        checker = new CheckForNetworkState(getContext());
        newsList = new ArrayList<News>();
        newsList.add(new News(1, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(2, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(3, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(4, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(5, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(6, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(7, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(8, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(9, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(10, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(11, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(12, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(13, "Minh Nghia", getString(R.string.des)));
        newsList.add(new News(14, "Minh Nghia", getString(R.string.des)));

        addControls(mview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expListView.setNestedScrollingEnabled(true);
        } else {
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
        List<Comment> comments = new ArrayList<>();
        CommentActivity commentActivity = new CommentActivity();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
        bundle.putBoolean(MainActivity.IS_LOGIN, isLogin);
        commentActivity.setArguments(bundle);
        if (CheckForNetworkState.isNetworkAvailable())
            commentActivity.show(getActivity().getSupportFragmentManager(), COMMENT_DIALOG);
    }


//    @Override
//    public void onCommentsClick(int position) {
//        List<Comment> comments = new ArrayList<>();
//        String url = "https://s3-us-west-1.amazonaws.com/powr/defaults/image-slider2.jpg";
//        User user = new User(url, "Minh Nghia", "", "");
//        String content = "aaaaaaaaaaaaaaaaaa";
//        Comment comment = new Comment(user, content);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        CommentActivity commentActivity = new CommentActivity();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
//        bundle.putBoolean(MainActivity.IS_LOGIN, isLogin);
//        commentActivity.setArguments(bundle);
//        if (CheckForNetworkState.isNetworkAvailable())
//            commentActivity.show(getActivity().getSupportFragmentManager(), COMMENT_DIALOG);
//    }

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        return true;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {

    }
}

//    @Override
//    public void onRefresh(SwipyRefreshLayoutDirection direction) {
//        mSwipeLayout.setRefreshing(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("Test", "RUN HANDLER");
//                mSwipeLayout.setRefreshing(false);
//            }
//        }, 3000);
//        Log.d("TEST", "REFRESHING");
//    }
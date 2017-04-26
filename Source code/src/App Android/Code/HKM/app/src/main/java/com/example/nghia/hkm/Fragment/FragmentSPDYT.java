package com.example.nghia.hkm.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.example.nghia.hkm.Adapter.CustomAdapter;
import com.example.nghia.hkm.Adapter.ExpandableAdapter;
import com.example.nghia.hkm.Class.Products;
import com.example.nghia.hkm.Interface.OnLoadMoreListener;
import com.example.nghia.hkm.MainActivity;
import com.example.nghia.hkm.R;
import com.example.nghia.hkm.helper.CheckForNetworkState;
import com.example.nghia.hkm.model.Comment;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
//import com.example.nghia.hkm.model.News;
//import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
//import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class FragmentSPDYT extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    List<Products> newsList;
    ExpandableListView expListView;
    SwipeRefreshLayout mSwipeLayout;
    AppBarLayout appBarLayout;
    CheckForNetworkState checker;
    private RecyclerView.OnScrollListener mLoadingMore;
    boolean isLogin = false;
    public static final String COMMENT = "comment";
    private static final String COMMENT_DIALOG = "comment_dialog";
    public static final String ProductURL = "ProductURL";
    public static final String CHECK_NETWORK = "check_network";
    public CustomAdapter mAdapter;

    public RecyclerView rvView;
    public LinearLayoutManager mLayoutManager;
    private FloatingActionButton fabScrollTop;
    private boolean mIsLoading;
    private Animation mAnimBottomOut;
    private Animation mAnimBottomIn;
    public static int CURRENT_PAGE=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_spdyt, container, false);
        isLogin = getArguments().getBoolean(MainActivity.IS_LOGIN);
        checker = new CheckForNetworkState(getContext());
        newsList = new ArrayList<>();
//        newsList.add(new News(1, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(2, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(3, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(4, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(5, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(6, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(7, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(8, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(9, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(10, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(11, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(12, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(13, "Minh Nghia", getString(R.string.des)));
//        newsList.add(new News(14, "Minh Nghia", getString(R.string.des)));
        ((MainActivity) getActivity()).connectWebSocket(0,"Promotion");
        intialListener();
        addControls(mview);
        rvView = (RecyclerView) mview.findViewById(R.id.RecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(layoutManager);
        rvView.setHasFixedSize(true);
        mAdapter = new CustomAdapter(getActivity(), newsList);
        rvView.setAdapter(mAdapter);
        rvView.addOnScrollListener(mLoadingMore);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                try{
                    mIsLoading=true;
                    mAdapter.addProgressItem(null);
                    UpdateCurrentPage();
                    ((MainActivity) getActivity()).connectWebSocket(CURRENT_PAGE,"Promotion");


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        mLayoutManager=new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(mLayoutManager);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            expListView.setNestedScrollingEnabled(true);
//        } else {
//        }

        mAdapter.setOnRefreshCompleted(new CustomAdapter.OnRefreshCompleted() {
            @Override
            public void onRefreshCompleted() {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }
        });


             return mview;
    }

    private void addControls(View mView) {
        mSwipeLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeToRefresh);
        mSwipeLayout.setOnRefreshListener(this);
//        mSwipeLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);

//        mProgressBar = (ProgressBar) mReturnView.findViewById(R.id.progressBarTheThao);
//        fabScrollTop = (FloatingActionButton) mView.findViewById(R.id.fab_scroll_top);
//        fabScrollTop.setVisibility(View.INVISIBLE);
//        fabScrollTop.setOnClickListener(this);

//        expListView = (ExpandableListView) mView.findViewById(R.id.lvExp);
//        expListView.setOnGroupClickListener(this);

        appBarLayout = (AppBarLayout) mView.findViewById(R.id.appbar);

//        ExpandableAdapter adapter = new ExpandableAdapter(getContext(), newsList, expListView);
//        expListView.setAdapter(adapter);
//        adapter.setOnButtonClickExpand(this);
    }

    @Override
    public void onRefresh() {
//        Log.d(TAG, "REFRESH");
        mSwipeLayout.setRefreshing(true);
        CURRENT_PAGE= 0;
        if (mAdapter.getItemCount() > 0) {
//            Log.d(TAG, "clear data");
            mAdapter.clearData();
        }
        try {
            ((MainActivity) getActivity()).connectWebSocket(CURRENT_PAGE,"Promotion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void onButtonExpandableClick(int groupPosition) {
//        List<Comment> comments = new ArrayList<>();
//        CommentActivity commentActivity = new CommentActivity();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(COMMENT, (ArrayList<? extends Parcelable>) comments);
//        bundle.putBoolean(MainActivity.IS_LOGIN, isLogin);
//        commentActivity.setArguments(bundle);
//        if (CheckForNetworkState.isNetworkAvailable())
//            commentActivity.show(getActivity().getSupportFragmentManager(), COMMENT_DIALOG);
//    }


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

    private void UpdateCurrentPage(){
        CURRENT_PAGE=CURRENT_PAGE+1;
    };

//    @Override
//    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
//        return true;
//    }

//    @Override
//    public void onRefresh(SwipyRefreshLayoutDirection direction) {
//
//    }

    public void PushData(List<Products> list) {
        mIsLoading = false;
        mAdapter.loadingfinish();
        updateCurrentPage();
        mAdapter.addData(list);
    }

    @Override
    public void onClick(View view) {

    }

    public interface LoadingMore{
        void loadingStart();

        void loadingfinish();
    }

    private void updateCurrentPage(){
        CURRENT_PAGE=CURRENT_PAGE+1;
    }

    private void intialListener() {

        mLoadingMore = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
//                    if (fabScrollTop.getVisibility() == View.VISIBLE && dy > 20) {
//                        fabScrollTop.startAnimation(mAnimBottomOut);
//                        fabScrollTop.setVisibility(View.INVISIBLE);
//                    }
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (!mIsLoading && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        mAdapter.loadingStart();
                    }
//                } else {
//                    if (fabScrollTop.getVisibility() == View.INVISIBLE && dy < -10 && dy != 0) {
//                        fabScrollTop.setVisibility(View.VISIBLE);
//                        fabScrollTop.startAnimation(mAnimBottomIn);
//                    }
                }
            }
        };




}}


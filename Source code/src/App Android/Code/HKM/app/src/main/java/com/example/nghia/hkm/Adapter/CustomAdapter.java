package com.example.nghia.hkm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.nghia.hkm.Class.Products;
import com.example.nghia.hkm.DetailActivity;
import com.example.nghia.hkm.Fragment.FragmentSPDYT;
import com.example.nghia.hkm.Interface.OnLoadMoreListener;
import com.example.nghia.hkm.R;
import com.example.nghia.hkm.helper.CheckForNetworkState;

import java.util.List;

import static com.example.nghia.hkm.R.id.masked;
import static com.example.nghia.hkm.R.id.view;

/**
 * Created by huu21 on 25/04/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FragmentSPDYT.LoadingMore {

    private final int VIEW_TYPE_ITEM=-1;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private OnLoadMoreListener mLoadMoreListener;
    private OnRefreshCompleted mListener;
    private List<Products> mProducts;
    Context context;
    public CustomAdapter(Context mContext, List<Products> mProducts) {
        this.context = mContext;
        this.mProducts = mProducts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        MyViewHolder holder = new MyViewHolder(mView);
//        return holder;
        switch (viewType)
        {
            case VIEW_TYPE_ITEM:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false));
            case VIEW_TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.loading,parent,false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Products products = mProducts.get(position);
//
//        viewHolder.title.setText(products.get__Title__());
//        viewHolder.datetime.setText((products.get__DateInsert__().toString()));
////        Picasso.with().load(products.get__LinkImage__()).resize(120,60).into(viewHolder.image);
        int type = getItemViewType(position);
        switch (type){
            case VIEW_TYPE_ITEM:
                bindViewHolderNormal((MyViewHolder) holder ,position);
                break;
            case VIEW_TYPE_LOADING:
                bindLoadingViewHold((LoadingViewHolder)holder,position);
                break;
        }

    }

    private void bindLoadingViewHold(LoadingViewHolder holder, int position) {
        holder.progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private void bindViewHolderNormal(final MyViewHolder holder, final int position)
    {
        Products products = mProducts.get(position);
        holder.title.setText(products.get__Title__());
        holder.datetime.setText((products.get__DateInsert__().toString()));
        Glide.with(context).load(products.get__LinkImage__()).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(holder.image);
        holder.viewC.setText(String.valueOf(products.get__ViewCount__()));
        holder.loveC.setText(String.valueOf(products.get__LoveCount__()));
        holder.oldPrice.setText(products.get__OldPrice__());
        holder.newPrice.setText(products.get__CurrentPrice__());
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        Products products = mProducts.get(position);
//
//        viewHolder.title.setText(products.get__Title__());
//        viewHolder.datetime.setText((products.get__DateInsert__().toString()));
//        Glide.with(context).load(products.get__LinkImage__()).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(viewHolder.image);
//        viewHolder.viewC.setText(String.valueOf(products.get__ViewCount__()));
//        viewHolder.loveC.setText(String.valueOf(products.get__LoveCount__()));
////        viewHolder.oldPrice.setText(products.get__OldPrice__());
////        viewHolder.newPrice.setText(products.get__CurrentPrice__());
//    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView datetime;
        public ImageView image;
        public TextView viewC;
        public TextView loveC;
        public TextView oldPrice;
        public TextView newPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            image = (ImageView) itemView.findViewById(R.id.imgV);
            viewC = (TextView) itemView.findViewById(R.id.viewC);
            loveC = (TextView) itemView.findViewById(R.id.loveC);
            itemView.setOnClickListener(this);
            oldPrice = (TextView) itemView.findViewById(R.id.giacu);
            newPrice = (TextView) itemView.findViewById(R.id.giamoi);
        }

        @Override
        public void onClick(View view) {
            try{
                if(CheckForNetworkState.isNetworkAvailable()){
                    Intent intent= new Intent(context , DetailActivity.class);
                    intent.putExtra(FragmentSPDYT.ProductURL, mProducts.get(getAdapterPosition()).get__Href__());
                    intent.putExtra(FragmentSPDYT.CHECK_NETWORK, CheckForNetworkState.isNetworkAvailable());
                    context.startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mLoadMoreListener = onLoadMoreListener;
    }

    public void setOnRefreshCompleted(OnRefreshCompleted listener) {
        mListener = listener;
}

    public void addMoreData(List<Products>List){
        try{
            mProducts.addAll(List);
            notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addData(List<Products> list) {
        try {
            mProducts.addAll(list);
            notifyItemRangeChanged(0, mProducts.size());
            notifyDataSetChanged();
            mListener.onRefreshCompleted();
        }catch (Exception e){e.printStackTrace();}
    }

    public void addProgressItem(Products progressItem) {
        try {
            mProducts.add(progressItem);
            notifyItemInserted(getLoadingMoreItemPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearData() {
        try {
            mProducts.clear();
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mProducts.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    private int getDataItemCount() {
        Log.d("HOHO", mProducts.size() + "");
        return mProducts.size();
    }

    private int getLoadingMoreItemPosition() {
        return isLoading ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    @Override
    public void loadingStart() {
        if (isLoading) return;
        isLoading = true;
//        notifyItemInserted(getLoadingMoreItemPosition());
        if (mLoadMoreListener != null) {
            mLoadMoreListener.onLoadMore();
        }
    }

    public interface OnRefreshCompleted {
        void onRefreshCompleted();
    }

    @Override
    public void loadingfinish() {
//        Log.d("MainActivity", "loadingfinish");

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!isLoading) return;
        final int removePosition = getLoadingMoreItemPosition();
        isLoading = false;

        try {
            mProducts.remove(removePosition);
            notifyItemRemoved(removePosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
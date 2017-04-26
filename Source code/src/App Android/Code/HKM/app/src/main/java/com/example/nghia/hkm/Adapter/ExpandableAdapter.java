package com.example.nghia.hkm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nghia.hkm.Class.Products;
import com.example.nghia.hkm.ExpandableListViewImp;
import com.example.nghia.hkm.R;
import com.example.nghia.hkm.model.News;

import java.util.List;
public class ExpandableAdapter extends BaseExpandableListAdapter {

    ExpandableListViewImp mListener;
    Context mContext;
    List<Products> mNewsList;
    NewsViewHolder holder;
    ExpandableListView expandableListView;

    public ExpandableAdapter(Context context, List<Products> list, ExpandableListView exp) {
        mContext = context;
        mNewsList = list;
        expandableListView = exp;
    }


    public void setOnButtonClickExpand(ExpandableListViewImp listener) {
        mListener = listener;
    }

    public int getGroupCount() {
        return mNewsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int parentPos) {
        return mNewsList.get(parentPos);
    }

    @Override
    public Object getChild(int parentPos, int childPos) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

//    @Override
//    public long getGroupId(int parentPos) {
//        return mNewsList.get(parentPos).get__Title__();
//    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int parentPos, boolean isExpanded, View view, ViewGroup viewGroup) {
        View viewGroupParent = view;
        NewsViewHolder holder;
            holder = new NewsViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewGroupParent = inflater.inflate(R.layout.fragment_spdyt, viewGroup, false);
//            holder.tvDes = (TextView) viewGroupParent.findViewById(R.id.tvDes);
//            holder.imgMiniPic = (ImageView) viewGroupParent.findViewById(R.id.imgPic);
            holder.btMore = (ImageView) viewGroupParent.findViewById(R.id.btMore);
            viewGroupParent.setTag(holder);
        holder.btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onButtonExpandableClick(parentPos);
            }
        });
        return viewGroupParent;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public class NewsViewHolder {
        TextView tvDes;
        ImageView imgMiniPic;
        ImageView btMore;

}
}
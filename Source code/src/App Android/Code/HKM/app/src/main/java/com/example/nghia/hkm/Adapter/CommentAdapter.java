package com.example.nghia.hkm.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nghia.hkm.R;
import com.example.nghia.hkm.model.Comment;

import java.util.ArrayList;
import java.util.List;

//import com.example.nghia.hkm.helper.ImageHelper;

public class CommentAdapter extends ArrayAdapter<Comment> {

    List<Comment> mCommentList;
    Context mContext;
    CommentViewHolder holder;
//    ImageHelper imageHelper;
    List<Bitmap> avatarList;
    ArrayList<String> avatars;
    ArrayList<String> names;
    ArrayList<String> contents;
    boolean isLogin = false;

    public CommentAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        mCommentList = objects;
        mContext = context;
//        imageHelper = new ImageHelper(mContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            holder = new CommentViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_comments_showinfoproduct, parent, false);
            // set up the ViewHolder
            holder.imgView = (ImageView) convertView.findViewById(R.id.imgview);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            holder.etCmt= (EditText) convertView.findViewById(R.id.etCmt);
            this.configData();
            //store the holder with the view
            convertView.setTag(holder);
        } else
            holder = (CommentViewHolder) convertView.getTag();
//        holder.imgView.setImageBitmap(imageHelper.getBitmapFromUrl(avatars.get(position), new ImageHelper.LoadSuccess() {
//            @Override
//            public void onLoadSuccess() {
//                Log.d("CMTADAP", "CALLBACK");
//                holder.progressBar.setVisibility(View.GONE);
//            }
//        }));
        holder.tvUserName.setText(names.get(position));
        holder.etCmt.setText(contents.get(position));

        return convertView;
    }

    public void configData() {
        avatars = new ArrayList<>();
        names = new ArrayList<>();
        contents = new ArrayList<>();
        for (Comment cmt : mCommentList) {
            avatars.add(cmt.getmCmtUser().getmAvatarUrl());
        names.add(cmt.getmCmtUser().getmUserName());
        contents.add(cmt.getmCmtContent());
    }
    }

    public class CommentViewHolder {
        ImageView imgView;
        TextView tvUserName;
        EditText etCmt;
        ProgressBar progressBar;
    }
}

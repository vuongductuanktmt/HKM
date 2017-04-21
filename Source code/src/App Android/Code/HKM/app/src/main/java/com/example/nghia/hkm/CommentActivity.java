package com.example.nghia.hkm;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nghia.hkm.Adapter.CommentAdapter;
import com.example.nghia.hkm.Fragment.FragmentSPDYT;
import com.example.nghia.hkm.model.Comment;

import java.util.List;


public class CommentActivity extends DialogFragment {

    TextView tvUserName;
    ImageView imgview;
    ListView lvCmt;
    TextView tvCmt;
    TextView tvthongtin;
    EditText etCmt;
    ProgressDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getContext());
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mReturnView = inflater.inflate(R.layout.activity_comments_showinfoproduct, container, false);
        tvUserName = (TextView) mReturnView.findViewById(R.id.tvUserName);
        etCmt = (EditText) mReturnView.findViewById(R.id.etCmt);
        tvCmt = (TextView) mReturnView.findViewById(R.id.tvCmt);
        tvthongtin = (TextView) mReturnView.findViewById(R.id.tvthongtin);
        lvCmt = (ListView) mReturnView.findViewById(R.id.lvCmt);
        imgview = (ImageView) mReturnView.findViewById(R.id.imgview);

        // bên fragment nhận
        Bundle bundle = getArguments(); // trả ra 1 bundle tương ứng
        if (!bundle.getBoolean(MainActivity.IS_LOGIN)) {
            //Returns the value associated with the given key, or false if no mapping of the desired type exists for the given key.
            etCmt.setVisibility(View.GONE);
            tvCmt.setVisibility(View.VISIBLE);
        } else {
            etCmt.setVisibility(View.VISIBLE);
            tvCmt.setVisibility(View.GONE);
        }
        // Setup dialog
        getDialog().getWindow().setGravity(Gravity.VERTICAL_GRAVITY_MASK | Gravity.TOP); // todo set position of dialog fragment

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK)); // todo: check out below note

        // Must set getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // to set dialog full of screen width and if keyboard is showing, the dialog will be collapsed
        // and one more function, the dialog will be have the radius attributes


        List<Comment> comments = bundle.getParcelableArrayList(FragmentSPDYT.COMMENT);

        CommentAdapter adapter = new CommentAdapter(getContext(), R.layout.activity_comments_showinfoproduct, comments);

        lvCmt.setAdapter(adapter);

        return mReturnView;
    }


    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = getResources().getDimensionPixelSize(R.dimen.comment_dialog_height);

        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.dismiss();
        super.onResume();
    }
}

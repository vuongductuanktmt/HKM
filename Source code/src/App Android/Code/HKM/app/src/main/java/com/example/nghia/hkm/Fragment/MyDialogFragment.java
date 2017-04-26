//package com.example.nghia.hkm.Fragment;
//
//import android.app.DialogFragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import com.example.nghia.hkm.R;
//
//public class MyDialogFragment extends DialogFragment {
//    Button btn;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_comments_showinfoproduct,container,false);
//        getDialog().setTitle("Lasted News");
//        btn = (Button) rootView.findViewById(R.id.dismiss);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        return rootView;
//    }
//
//}
//
//

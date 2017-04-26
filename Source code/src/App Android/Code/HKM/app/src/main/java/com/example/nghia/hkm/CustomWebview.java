package com.example.nghia.hkm;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by huu21 on 26/04/2017.
 */
public class CustomWebview extends WebView {

    public interface OnPageContentDisplayed {
        public void onPageContentDisplayed();
    }

    OnPageContentDisplayed mListener;

    public CustomWebview(Context context) {
        super(context);
    }

    public CustomWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnPageContentDisplayed(OnPageContentDisplayed listener) {
        mListener = listener;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if (getContentHeight() > 1000) {
            Log.d("TEST", getContentHeight()+"");
            mListener.onPageContentDisplayed();
        }
    }

}
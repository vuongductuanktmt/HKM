package com.example.nghia.hkm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nghia.hkm.Fragment.FragmentSPDYT;
import com.example.nghia.hkm.helper.CheckForNetworkState;

public class DetailActivity extends AppCompatActivity implements CustomWebview.OnPageContentDisplayed {
    private String mUrl;
    private TextView mWebViewLoadingTextView;
    private CustomWebview mWebView;
    private ProgressBar mWebViewProgressBar;
    private CheckForNetworkState mChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mWebView = (CustomWebview) findViewById(R.id.wv_detail);
        mWebViewProgressBar = (ProgressBar) findViewById(R.id.wv_progress_bar);
        mWebViewLoadingTextView = (TextView) findViewById(R.id.wv_loading_text);
        mChecker = new CheckForNetworkState(this);
        mUrl = getIntent().getStringExtra(FragmentSPDYT.ProductURL);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        mWebView.setPictureListener(this);
        mWebView.setOnPageContentDisplayed(this);
        mWebView.setWebViewClient(new WebViewClient() {

//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//                if (url.startsWith("tel:")) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//                    startActivity(intent);
//                } else {
//
//                    view.loadUrl(url);
//
//                }
//            }
//
//            //Show loader on url load
//            public void onLoadResource(WebView view, String url) {
//                if (progressDialog == null) {
//                    // in standard case YourActivity.this
//                    progressDialog = new ProgressDialog(ShowWebView.this);
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//                }
//            }

            public void onPageFinished(WebView view, String url) {
                mWebView.setPictureListener(null);
            }
        });
        if (mUrl != null && mChecker.isNetworkAvailable()) mWebView.loadUrl(mUrl);
    }

//    @Override
//    public void onNewPicture(WebView webView, Picture picture) {
//
//    }

    @Override
    public void onPageContentDisplayed() {
        try {
            if (mWebViewProgressBar.getVisibility() == View.VISIBLE) {
                mWebViewProgressBar.setVisibility(View.INVISIBLE);
                mWebViewLoadingTextView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}


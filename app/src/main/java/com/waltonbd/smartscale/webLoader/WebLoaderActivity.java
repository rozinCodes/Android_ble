package com.waltonbd.smartscale.webLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.waltonbd.smartscale.databinding.ActivityWebLoaderBinding;

public class WebLoaderActivity extends AppCompatActivity {

    protected ActivityWebLoaderBinding binding;

    private KProgressHUD progressDialog;

    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";

    protected String title, url;

    public static Intent getStartIntent(Context context, String title, String url) {
        Intent intent = new Intent(context, WebLoaderActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebLoaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create progress dialog
        progressDialog = KProgressHUD.create(this)
                .setLabel("Loading...")
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);

        title = getIntent().getStringExtra(KEY_TITLE);
        url = getIntent().getStringExtra(KEY_URL);

        if (!TextUtils.isEmpty(title)) binding.actionTitle.setText(title);
        binding.actionBack.setOnClickListener(view -> finish());

        initializeWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {
        // show loading progress
        progressDialog.show();
        binding.progressBar.setVisibility(View.GONE);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.loadUrl(url);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //binding.progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //binding.progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //binding.progressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.webView.canGoBack())
            binding.webView.goBack();   // if there is previous page open it
        else
            super.onBackPressed();  //if there is no previous page, close app
    }
}

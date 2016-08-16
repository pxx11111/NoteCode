package com.example.pxx.notecode.phpman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pxx.notecode.R;

public class PHPcompileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phpcompile);
        WebView webView_PHPcompile = (WebView)findViewById(R.id.webView_PHPcompile);
        WebSettings webSettings = webView_PHPcompile.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView_PHPcompile.loadUrl("file:///android_asset/runphp.html");
        webView_PHPcompile.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}

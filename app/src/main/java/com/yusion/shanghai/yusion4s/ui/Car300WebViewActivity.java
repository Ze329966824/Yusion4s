package com.yusion.shanghai.yusion4s.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseWebViewActivity;
import com.yusion.shanghai.yusion4s.widget.ProgressWebView;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

public class Car300WebViewActivity extends BaseWebViewActivity {

    private ProgressWebView webView;
    private String url;
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car300_web_view);
        webView = (ProgressWebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        url = "https://m.che300.com/estimate/result/1/1/1/1/1/1-1/1/1/null/";
        webView.loadUrl(url);
        titleBar = initTitleBar(this, "车300估值报告");
        titleBar.setLeftClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                onBackPressed();
            }
        });
        View car300hint = findViewById(R.id.car300_hint);
        findViewById(R.id.car300_hint_close).setOnClickListener(v -> car300hint.setVisibility(View.GONE));
        findViewById(R.id.car300_confirm).setOnClickListener(v -> Toast.makeText(myApp, "确定", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

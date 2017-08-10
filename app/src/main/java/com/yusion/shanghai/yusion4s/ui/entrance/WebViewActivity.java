package com.yusion.shanghai.yusion4s.ui.entrance;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.utils.LoadingUtils;

public class WebViewActivity extends BaseActivity {

    private String url;
    private String title;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        initWebView();

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if (url != null && title != null) {
            initTitleBar(this, title);
            webView.loadUrl(url);
        }
        String type = getIntent().getStringExtra("type");

        if (type == null)
            return;
        if (type.equals("Agreement")) {
            initTitleBar(this, "用户协议");
            webView.loadUrl(Yusion4sApp.CONFIG_RESP.agreement_url);
        }


    }

    private void initWebView() {
        webView.setOnClickListener(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LoadingUtils.createLoadingDialog(WebViewActivity.this);
            }
        });

    }
}

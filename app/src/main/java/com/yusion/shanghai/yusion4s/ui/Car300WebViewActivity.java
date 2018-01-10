package com.yusion.shanghai.yusion4s.ui;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.base.BaseWebViewActivity;
import com.yusion.shanghai.yusion4s.utils.ToastUtil;
import com.yusion.shanghai.yusion4s.widget.ProgressWebView;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

public class Car300WebViewActivity extends BaseWebViewActivity {

    private ProgressWebView webView;
    private RelativeLayout bottom_rel;
    private String url;
    private TitleBar titleBar;
    private int retryTime = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car300_web_view);
        url = getIntent().getStringExtra("cheUrl");
        webView = (ProgressWebView) findViewById(R.id.webView);
        bottom_rel = findViewById(R.id.bottom_rel);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e("TAG", "onReceivedError() called with: view = [" + view + "], request = [" + request + "], error = [" + error + "]");
//                if (retryTime > 0) {
//                    retryTime = retryTime - 1;
//                    webView.loadUrl(url);
//                } else {
//                    bottom_rel.setVisibility(View.GONE);
//                    ToastUtil.showShort(Car300WebViewActivity.this, "网络繁忙");
//                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("TAG", "onReceivedError() called with: view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
                if (retryTime > 0) {
                    retryTime = retryTime - 1;
                    webView.loadUrl(url);
                } else {
                    bottom_rel.setVisibility(View.GONE);
                    ToastUtil.showShort(Car300WebViewActivity.this, "网络繁忙");
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e("TAG", "onReceivedHttpError() called with: view = [" + view + "], request = [" + request + "], errorResponse = [" + errorResponse + "]");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.e("TAG", "onReceivedSslError() called with: view = [" + view + "], handler = [" + handler + "], error = [" + error + "]");
            }
        });
        titleBar = initTitleBar(this, "车300估值报告");
//        titleBar.setLeftClickListener(v -> {
//            if (webView.canGoBack()) {
//                webView.goBack();
//            } else {
//                onBackPressed();
//            }
//        });
        View car300hint = findViewById(R.id.car300_hint);
        findViewById(R.id.car300_hint_close).setOnClickListener(v -> car300hint.setVisibility(View.GONE));
        findViewById(R.id.car300_confirm).setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            webView.goBack();// 返回前一个页面
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

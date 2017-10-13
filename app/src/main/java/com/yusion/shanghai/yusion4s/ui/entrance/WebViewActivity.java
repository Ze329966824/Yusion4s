package com.yusion.shanghai.yusion4s.ui.entrance;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yusion.shanghai.yusion4s.R;
import com.yusion.shanghai.yusion4s.Yusion4sApp;
import com.yusion.shanghai.yusion4s.base.BaseActivity;
import com.yusion.shanghai.yusion4s.widget.ProgressWebView;
import com.yusion.shanghai.yusion4s.widget.TitleBar;

public class WebViewActivity extends BaseActivity {

    private String url;
    private String title;
    private ProgressWebView webView;
    private TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (ProgressWebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        initWebView();

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        if (url != null && title != null) {
            titleBar = initTitleBar(this, title);
            webView.loadUrl(url);
        }
        String type = getIntent().getStringExtra("type");

        if (type == null)
            return;
        if (type.equals("Agreement")) {
            titleBar = initTitleBar(this, "用户协议");
            webView.loadUrl(Yusion4sApp.getConfigResp().agreement_url);
        }
        if (type.equals("homepage")) {
            titleBar = initTitleBar(this, "关于予见汽车");
            webView.loadUrl("http://www.yusiontech.com/");
        }
        if (type.equals("contract")) {
            titleBar = initTitleBar(this, "合同规范模板");
            webView.loadUrl(Yusion4sApp.getConfigResp().contract_list_url);
        }
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }else {
                    onBackPressed();
                }
            }
        });

    }

    private void initWebView() {
        webView.setOnClickListener(this);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                view.loadUrl(url);
//                return true;
//            }
//        });

//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                LoadingUtils.createLoadingDialog(WebViewActivity.this);
//            }
//        });

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

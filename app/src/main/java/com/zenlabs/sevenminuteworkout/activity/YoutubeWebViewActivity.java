package com.zenlabs.sevenminuteworkout.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zenlabs.sevenminuteworkout.R;

public class YoutubeWebViewActivity extends Activity {

    private final String blackUrl = "about:blank";
    private  WebView webView;

    public static final String YOUTUBE_WEBVIEW_ACTIVITY_URL_EXTRA_DATA = "YOUTUBE_WEBVIEW_ACTIVITY_URL_EXTRA_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_web_view);

        Bundle extra = getIntent().getExtras();

        String url = "";

        if(extra!=null){
            url = extra.getString(YOUTUBE_WEBVIEW_ACTIVITY_URL_EXTRA_DATA,blackUrl);
        }

        webView = (WebView) findViewById(R.id.webViewVideoDialogWebView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSaveFormData(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setBackgroundColor(Color.BLACK);
        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.loadUrl(blackUrl);
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();

    }
}

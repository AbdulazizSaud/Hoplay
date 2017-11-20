package com.hoplay.kay.hoplay.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.hoplay.kay.hoplay.R;

public class HoplayWebview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        String updateSite = "https://play.google.com/store/apps/details?id=com.hoplay.kay.hoplay&hl=en";
        WebView webView = (WebView) findViewById(R.id.hoplay_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(updateSite);



    }
}

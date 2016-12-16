package com.br.pagpeg.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.br.pagpeg.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by brunolemgruber on 16/12/16.
 */

public class OpenCatalog extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_pdf);

        webView = (WebView) findViewById(R.id.wv);

        String url = getIntent().getStringExtra("url");

        try {

            String urlEncoded = URLEncoder.encode(url, "UTF-8");
            url = "http://docs.google.com/gview?embedded=true&url=" + urlEncoded;

            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }
}

package com.example.rishabh.dailynews;

/**
 * Created by rishabh on 18/7/17.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;

import static com.example.rishabh.dailynews.dummy.IndiaNews.TAG_URLwebview;


public class Webindia extends AppCompatActivity {

    ProgressDialog pd;
    android.webkit.WebView mWebview;

    String weburl;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inner);


        final Intent intent = getIntent();
        final HashMap<String,String> places = (HashMap<String, String>) intent.getSerializableExtra("Mywebdata");
        weburl=places.get(TAG_URLwebview);


        mWebview  = new android.webkit.WebView(this);
        pd = new ProgressDialog(Webindia.this);
        pd.setMessage("Loading...");
        pd.show();
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.loadUrl(weburl);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(android.webkit.WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview .loadUrl(weburl);
        setContentView(mWebview );

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}

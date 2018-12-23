package com.twt.zhihu.zhihudaily.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.twt.zhihu.zhihudaily.Model.ContentBean;
import com.twt.zhihu.zhihudaily.R;

public class ContentActivity extends AppCompatActivity {
    WebView webView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        webView = findViewById(R.id.webview);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            id = b.getString("id");
        }
        webView.loadUrl("http://daily.zhihu.com/story/" + id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_toolbar, menu);
        return true;
    }
}

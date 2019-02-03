package com.twt.zhihu.zhihudaily.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.webkit.WebView

import com.twt.zhihu.zhihudaily.R


class ContentActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    internal var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        webView = findViewById(R.id.webview)
        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id")
        }
        webView.loadUrl("http://daily.zhihu.com/story/" + id!!)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.content_toolbar, menu)
        return true
    }
}

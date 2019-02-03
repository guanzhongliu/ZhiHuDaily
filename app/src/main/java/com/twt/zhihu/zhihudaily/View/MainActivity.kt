package com.twt.zhihu.zhihudaily.View

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.widget.Toast


import com.twt.zhihu.zhihudaily.R
import com.twt.zhihu.zhihudaily.model.MainBean
import com.twt.zhihu.zhihudaily.presenter.ListAdapter
import com.twt.zhihu.zhihudaily.presenter.MainContract
import com.twt.zhihu.zhihudaily.presenter.MainPresenter

class MainActivity : AppCompatActivity(), MainContract.UIview {
    private var recyclerView: RecyclerView? = null
    private lateinit var listAdapter: ListAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    var bean = MainBean()
    private val presenter = MainPresenter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.getInit()

    }

    fun initLayout() {
        listAdapter = ListAdapter(this, bean)
        recyclerView = findViewById(R.id.rv)
        refreshLayout = findViewById(R.id.swipe)
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = listAdapter
    }

    fun refresh() {
        refreshLayout.setOnRefreshListener { presenter.getList() }
    }

    override fun setData(bean: MainBean) {
        this.bean = bean
    }


    fun Loading() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager
                val lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && recyclerView.adapter!!.itemCount > 1
                        && lastVisibleItemPosition + 1 == recyclerView.adapter!!.itemCount) {
                    presenter.getLoad(bean.date!!)
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun initView() {
        initLayout()
        listAdapter.mainBeanList.clear()
        listAdapter.addData(bean)
        refreshLayout.isRefreshing = false
        listAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun updateView() {
        listAdapter.addData(bean)
        listAdapter.notifyDataSetChanged()
    }

    override fun firstInitView() {
        initLayout()
        refresh()
        Loading()
        listAdapter.mainBeanList.clear()
        listAdapter.addData(bean)
        refreshLayout.isRefreshing = false
        listAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }
}

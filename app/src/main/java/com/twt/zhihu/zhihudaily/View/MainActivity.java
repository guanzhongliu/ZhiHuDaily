package com.twt.zhihu.zhihudaily.View;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import com.twt.zhihu.zhihudaily.Presenter.ListAdapter;
import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.Model.MainModel;
import com.twt.zhihu.zhihudaily.Presenter.MainContract;
import com.twt.zhihu.zhihudaily.Presenter.MainPresenter;
import com.twt.zhihu.zhihudaily.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainContract.UIview {
    private RecyclerView recyclerView;
    public ListAdapter listAdapter;
    public SwipeRefreshLayout refreshLayout;
    public MainBean bean = new MainBean();
    private MainContract.ListPresenter presenter = new MainPresenter(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter.getInit();

    }

    public void initLayout() {
        listAdapter = new ListAdapter(this, bean);
        recyclerView = findViewById(R.id.rv);
        refreshLayout = findViewById(R.id.swipe);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listAdapter);
    }

    public void refresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getList();
            }
        });
    }

    @Override
    public void setData(MainBean bean) {
        this.bean = bean;
    }


    public void Loading() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() > 1
                        && lastVisibleItemPosition + 1 == recyclerView.getAdapter().getItemCount()
                        ) {
                    presenter.getLoad(bean.date);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public void initView() {
        initLayout();
        listAdapter.mainBeanList.clear();
        listAdapter.addData(bean);
        refreshLayout.setRefreshing(false);
        listAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        listAdapter.addData(bean);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void firstInitView() {
        initLayout();
        refresh();
        Loading();
        listAdapter.mainBeanList.clear();
        listAdapter.addData(bean);
        refreshLayout.setRefreshing(false);
        listAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }
}




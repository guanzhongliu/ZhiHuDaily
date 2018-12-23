package com.twt.zhihu.zhihudaily.Presenter;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.Model.RetrofitApi;
import com.twt.zhihu.zhihudaily.View.MainActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRequest {

    private static final String BASE_URL = "https://news-at.zhihu.com/api/4/";
    private MainActivity mainActivity;

    public NetRequest(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void getList() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(7777, TimeUnit.MILLISECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        RetrofitApi api = retrofit.create(RetrofitApi.class);
        Call<MainBean> call = api.getList();

        call.enqueue(new Callback<MainBean>() {
            @Override
            public void onResponse(@NonNull Call<MainBean> call, @NonNull Response<MainBean> response) {
                mainActivity.setData(response.body());
                mainActivity.initLayout();
                mainActivity.refresh();
                mainActivity.Loading();
                mainActivity.listAdapter.mainBeanList.clear();
                mainActivity.listAdapter.addData(mainActivity.bean);
                mainActivity.refreshLayout.setRefreshing(false);
                mainActivity.listAdapter.notifyDataSetChanged();
                Toast.makeText(mainActivity, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable throwable) {
                Toast.makeText(mainActivity, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    public void getLoad(String date) {
        mainActivity.loading = true;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(7777, TimeUnit.MILLISECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        RetrofitApi loadApi = retrofit.create(RetrofitApi.class);
        Call<MainBean> call = loadApi.getList(date);
        call.enqueue(new Callback<MainBean>() {
            @Override
            public void onResponse(@NonNull Call<MainBean> call, @NonNull Response<MainBean> response) {
                mainActivity.setData(response.body());
                mainActivity.listAdapter.addData(mainActivity.bean);
                mainActivity.listAdapter.notifyDataSetChanged();
                mainActivity.loading = false;
            }

            @Override
            public void onFailure(@NonNull Call<MainBean> call, @NonNull Throwable t) {
                mainActivity.loading = false;
                Toast.makeText(mainActivity, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show();
            }
        });

    }
}


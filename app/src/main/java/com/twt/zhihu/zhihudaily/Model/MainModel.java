package com.twt.zhihu.zhihudaily.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.twt.zhihu.zhihudaily.NetService.RetrofitApi;
import com.twt.zhihu.zhihudaily.View.MainActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainModel {

    private static final String BASE_URL = "https://news-at.zhihu.com/api/4/";
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

    public Call<MainBean> refresh() {
        return api.getList();
    }

    public Call<MainBean> update(String date) {
        return api.getList(date);
    }

}


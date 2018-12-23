package com.twt.zhihu.zhihudaily.Presenter;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.twt.zhihu.zhihudaily.Model.ContentBean;
import com.twt.zhihu.zhihudaily.Model.RetrofitApi;
import com.twt.zhihu.zhihudaily.View.ContentActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsRequest {
    private static final String BASE_URL = "http://daily.zhihu.com/story/";

    private ContentActivity contentActivity;

    public NewsRequest(ContentActivity detailActivity) {
        this.contentActivity = detailActivity;
    }

    public void getNews(String index) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectTimeout(7777, TimeUnit.MILLISECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        RetrofitApi detailApi = retrofit.create(RetrofitApi.class);
        Call<ContentBean> call = detailApi.getNews(index);
        call.enqueue(new Callback<ContentBean>() {
            @Override
            public void onResponse(@NonNull Call<ContentBean> call, @NonNull Response<ContentBean> response) {
                contentActivity.setData(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ContentBean> call, @NonNull Throwable t) {
                Toast.makeText(contentActivity, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

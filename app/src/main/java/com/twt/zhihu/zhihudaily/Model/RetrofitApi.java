package com.twt.zhihu.zhihudaily.Model;

import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.Model.ContentBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitApi {

    @GET("stories/latest")
    Call<MainBean> getList();

    @GET("news/before/{date}")
    Call<MainBean> getList(@Path("date") String date);

    @GET("news/{id}")
    Call<ContentBean> getNews(@Path("id") String id);
}

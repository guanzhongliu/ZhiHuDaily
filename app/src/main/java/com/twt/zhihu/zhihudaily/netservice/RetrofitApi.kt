package com.twt.zhihu.zhihudaily.netservice

import com.twt.zhihu.zhihudaily.model.MainBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitApi {

    @get:GET("stories/latest")
    val list: Call<MainBean>

    @GET("news/before/{date}")
    fun getList(@Path("date") date: String): Call<MainBean>
}

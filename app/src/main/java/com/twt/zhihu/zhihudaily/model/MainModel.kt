package com.twt.zhihu.zhihudaily.model

import com.twt.zhihu.zhihudaily.netservice.RetrofitApi
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainModel {
    private val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .connectTimeout(7777, TimeUnit.MILLISECONDS)
            .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    private val api = retrofit.create(RetrofitApi::class.java)

    fun refresh(): Call<MainBean> {
        return api.list
    }

    fun update(date: String): Call<MainBean> {
        return api.getList(date)
    }

    companion object {
        private const val BASE_URL = "https://news-at.zhihu.com/api/4/"
    }

}


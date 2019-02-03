package com.twt.zhihu.zhihudaily.presenter

import com.twt.zhihu.zhihudaily.model.MainBean

class MainContract {
    interface UIview {
        fun initView()
        fun updateView()
        fun firstInitView()
        fun setData(mainBean: MainBean)
    }

    interface ListPresenter {
        fun getList()
        fun getLoad(date: String)
        fun getInit()
    }
}

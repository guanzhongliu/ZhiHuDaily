package com.twt.zhihu.zhihudaily.Presenter;

import com.twt.zhihu.zhihudaily.Model.MainBean;

public class MainContract {
    public interface UIview {

        void initView();
        void updateView();
        void setData(MainBean mainBean);
    }

    public interface ListPresenter{
        void getList();
        void getLoad(String date);
    }
}

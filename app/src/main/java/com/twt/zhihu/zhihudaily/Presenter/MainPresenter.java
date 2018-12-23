package com.twt.zhihu.zhihudaily.Presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.Model.MainModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.ListPresenter{
    /**
     * presenter应该持有 view 层和 model 层的引用
     * 这样才能完成两层之间的逻辑交互
     * 同时使 view 层和 model 层完全隔离开来
     */
    private MainContract.UIview uiView;
    private MainModel mainModel = new MainModel();

    /**
     * presenter 层对应的类持有的 view 层对应的类是没有办法在 presenter 内部实例化的（此时的view是有方法但是没有具体实现的接口）
     * view 层的具体实现是 activity 继承 view 层，并重写 view 层的所有方法，即 activity 就是 view 层
     * 故成员对象 view 的实例化对象是在activity中传给presenter的
     * 所以 presenter 的构造函数中应该传入 view
     * model 层是有具体实现类的，并且已经在 presenter 类的内部实例化了，这样才能拿到 model 的具体数据，进行操作
     */
    public MainPresenter(MainContract.UIview uiView){
        this.uiView = uiView;
    }



    /**
     * view 层和 model 层的逻辑交互，根据model的数据，执行相关的view层操作
     */
    @Override
    public void getList() {
        Call<MainBean> call = mainModel.refresh();
        call.enqueue(new Callback<MainBean>() {
            @Override
            public void onResponse(@NonNull Call<MainBean> call, @NonNull Response<MainBean> response) {
                uiView.setData(response.body());
                uiView.initView();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable throwable) {
                Toast.makeText((Context) uiView, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void getLoad(String date) {
        Call<MainBean> call = mainModel.update(date);
        call.enqueue(new Callback<MainBean>() {
            @Override
            public void onResponse(@NonNull Call<MainBean> call, @NonNull Response<MainBean> response) {
                uiView.setData(response.body());
                uiView.updateView();
            }

            @Override
            public void onFailure(@NonNull Call<MainBean> call, @NonNull Throwable t) {
                //uiView.loading = false;
                Toast.makeText((Context) uiView, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

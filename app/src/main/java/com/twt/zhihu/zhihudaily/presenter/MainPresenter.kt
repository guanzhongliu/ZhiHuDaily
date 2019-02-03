package com.twt.zhihu.zhihudaily.presenter

import android.content.Context
import android.widget.Toast
import com.twt.zhihu.zhihudaily.model.MainBean
import com.twt.zhihu.zhihudaily.model.MainModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter
/**
 * presenter 层对应的类持有的 view 层对应的类是没有办法在 presenter 内部实例化的（此时的view是有方法但是没有具体实现的接口）
 * view 层的具体实现是 activity 继承 view 层，并重写 view 层的所有方法，即 activity 就是 view 层
 * 故成员对象 view 的实例化对象是在activity中传给presenter的
 * 所以 presenter 的构造函数中应该传入 view
 * model 层是有具体实现类的，并且已经在 presenter 类的内部实例化了，这样才能拿到 model 的具体数据，进行操作
 */
(
        /**
         * presenter应该持有 view 层和 model 层的引用
         * 这样才能完成两层之间的逻辑交互
         * 同时使 view 层和 model 层完全隔离开来
         */
        private val uiView: MainContract.UIview) : MainContract.ListPresenter {

    private val mainModel = MainModel()


    /**
     * view 层和 model 层的逻辑交互，根据model的数据，执行相关的view层操作
     */
    override fun getList() {
        val call = mainModel.refresh()
        call.enqueue(object : Callback<MainBean> {
            override fun onResponse(call: Call<MainBean>, response: Response<MainBean>) {
                uiView.setData(response.body()!!)
                uiView.initView()
            }

            override fun onFailure(call: Call<MainBean>, throwable: Throwable) {
                Toast.makeText(uiView as Context, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show()
                throwable.printStackTrace()
            }
        })
    }

    override fun getLoad(date: String) {
        val call = mainModel.update(date)
        call.enqueue(object : Callback<MainBean> {
            override fun onResponse(call: Call<MainBean>, response: Response<MainBean>) {
                uiView.setData(response.body()!!)
                uiView.updateView()
            }

            override fun onFailure(call: Call<MainBean>, t: Throwable) {
                Toast.makeText(uiView as Context, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getInit() {
        val call = mainModel.refresh()
        call.enqueue(object : Callback<MainBean> {
            override fun onResponse(call: Call<MainBean>, response: Response<MainBean>) {
                uiView.setData(response.body()!!)
                uiView.firstInitView()
            }

            override fun onFailure(call: Call<MainBean>, throwable: Throwable) {
                Toast.makeText(uiView as Context, "Fail,请检查您的网络设置", Toast.LENGTH_SHORT).show()
                throwable.printStackTrace()
            }
        })
    }

}

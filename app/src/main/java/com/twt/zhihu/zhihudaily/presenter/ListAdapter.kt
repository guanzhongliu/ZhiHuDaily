package com.twt.zhihu.zhihudaily.presenter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso
import com.twt.zhihu.zhihudaily.R
import com.twt.zhihu.zhihudaily.model.MainBean

import com.twt.zhihu.zhihudaily.View.ContentActivity

import java.util.ArrayList

class ListAdapter(private val context: Context, latestBean: MainBean) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var mainBeanList: MutableList<MainBean> = ArrayList()

    init {
        mainBeanList.add(latestBean)
    }

    fun addData(latestBean: MainBean) {
        mainBeanList.add(latestBean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == HEADER) {
            val bannerAdapter: BannerAdapter = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false) as BannerAdapter
            ViewHolder(bannerAdapter)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == HEADER) HEADER else LIST         //判断是否为头部
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var realPosition = position - 1
        if (position == HEADER) {
            (holder.view as BannerAdapter).setList(this.mainBeanList[0].top_stories!!)
            //头部为ViewPager
        } else {
            var group = 0
            var length = 0
            for (latestBean in mainBeanList) {
                if (realPosition >= latestBean.stories.size) {
                    if (realPosition != latestBean.stories.size || length <= latestBean.stories.size) {
                        realPosition -= latestBean.stories.size
                        group++
                    }
                }
                length = latestBean.stories.size
            }

            if (realPosition >= mainBeanList[group].stories.size) {
                realPosition -= mainBeanList[group].stories.size
                group++
            }                                               //解决前后两天列表长度不一而导致的group错乱的bug

            val bean = group
            val beanPosition = realPosition
            holder.textView?.text = mainBeanList[bean].stories[beanPosition].title
            Picasso.with(context)
                    .load(mainBeanList[bean].stories[beanPosition].images!![0])
                    .fit()
                    .centerCrop()
                    .into(holder.imageView)
            holder.cardView?.setOnClickListener {
                val intent = Intent(context, ContentActivity::class.java)
                val bundle = Bundle()
                bundle.putString("id", mainBeanList[bean].stories[beanPosition].id.toString())
                intent.putExtras(bundle)
                context.startActivity(intent)
            }

            if (beanPosition == ITEM_DATE) {
                holder.item_date?.visibility = View.VISIBLE
                if (bean != 0) {

                    holder.item_date?.text = mainBeanList[bean].date
                } else {
                    holder.item_date?.text = "今日热闻"
                }                                               //日期栏
            } else {
                holder.item_date?.visibility = View.GONE
            }                                                   //其余时间隐藏


        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (latestBean in mainBeanList) {
            count += latestBean.stories.size
        }
        return count + 1
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView? = view.findViewById(R.id.tv)
        var imageView: ImageView? = view.findViewById(R.id.iv)
        var item_date: TextView? = view.findViewById(R.id.item_date)
        var cardView: View? = view.findViewById(R.id.cardview)

    }

    companion object {
        private const val HEADER = 0
        private const val LIST = 1
        private const val ITEM_DATE = 0
    }

}

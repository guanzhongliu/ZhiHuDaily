package com.twt.zhihu.zhihudaily.presenter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.squareup.picasso.Picasso


import com.twt.zhihu.zhihudaily.R
import com.twt.zhihu.zhihudaily.model.MainBean
import com.twt.zhihu.zhihudaily.view.ContentActivity


class BannerAdapter @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes), ViewPager.OnPageChangeListener, View.OnTouchListener {
    private var list: List<MainBean.TopStoriesBean>? = null
    private var linearLayout: LinearLayout? = null              //底部原点指示
    private var prePosition = 0

    init {
        init()
    }

    private fun init() {
        val viewPager = ViewPager(context)
        val pagerParam = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(viewPager, pagerParam)
        viewPager.addOnPageChangeListener(this)
        val pagerAdapter = PagerAdapter()
        viewPager.adapter = pagerAdapter

        linearLayout = LinearLayout(context)
        val pointParam = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pointParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        pointParam.addRule(RelativeLayout.CENTER_HORIZONTAL)
        pointParam.bottomMargin = 30
        addView(linearLayout, pointParam)                 //圆点指示
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setList(list: List<MainBean.TopStoriesBean>) {
        this.list = list
        prePosition = 0
        linearLayout!!.removeAllViews()
        setOnIndicator(this.list!!.size)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun setOnIndicator(size: Int) {
        for (i in 0 until size) {
            val point = ImageView(context)
            point.background = background           //此方法有api要求
            val pointView = LinearLayout.LayoutParams(RADIUS, RADIUS)
            if (i == 0) {
                point.isEnabled = true
            } else {
                pointView.leftMargin = RADIUS          //圆点间隔
                point.isEnabled = false
            }
            linearLayout!!.addView(point, pointView)
        }
    }

    //显示当前图片的位置
    override fun getBackground(): Drawable {
        val enable = GradientDrawable()
        enable.setColor(Color.parseColor("#ffffff"))
        enable.shape = GradientDrawable.OVAL                    //设置形状
        val normal = GradientDrawable()
        normal.setColor(Color.parseColor("#888888"))
        normal.shape = GradientDrawable.OVAL
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), enable)
        stateListDrawable.addState(intArrayOf(), normal)
        return stateListDrawable
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        var index = position
        index %= list!!.size
        linearLayout!!.getChildAt(this.prePosition).isEnabled = false
        linearLayout!!.getChildAt(index).isEnabled = true         //防止初始化时的意外
        this.prePosition = index
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    fun setList(container: ViewGroup, position: Int, list: List<MainBean.TopStoriesBean>?): View {
        var index = position
        index %= list!!.size   //可以从最后一张划至第一张
        val relativeLayout = LayoutInflater.from(context).inflate(R.layout.banner, container, false) as RelativeLayout
        val imageView = relativeLayout.findViewById<ImageView>(R.id.iv_head)
        val textView = relativeLayout.findViewById<TextView>(R.id.tv_head)
        Picasso.with(context)
                .load(list[index].image)
                .fit()
                .centerCrop()
                .into(imageView)
        textView.text = list[index].title
        val finalPosition = index
        relativeLayout.setOnClickListener {
            val intent = Intent(context, ContentActivity::class.java)
            val bundle = Bundle()
            bundle.putString("id", list[finalPosition].id.toString())
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        return relativeLayout
    }

    internal inner class PagerAdapter : android.support.v4.view.PagerAdapter() {

        override fun destroyItem(container: ViewGroup, position: Int, something: Any) {
            container.removeView(something as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = this@BannerAdapter.setList(container, position, list)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return Integer.MAX_VALUE                   //保证Banner可以一直滑动
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

    companion object {
        private const val RADIUS = 20             //原点直径
    }

}

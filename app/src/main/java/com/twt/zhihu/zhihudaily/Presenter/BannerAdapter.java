package com.twt.zhihu.zhihudaily.Presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.R;
import com.twt.zhihu.zhihudaily.View.ContentActivity;

import java.util.List;


public class BannerAdapter extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    private List<MainBean.TopStoriesBean> list;
    private LinearLayout linearLayout;              //底部原点指示
    int prePosition = 0;
    private final static int RADIUS = 20;             //原点直径


    public BannerAdapter(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerAdapter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerAdapter(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerAdapter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ViewPager viewPager = new ViewPager(getContext());
        LayoutParams pagerParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(viewPager, pagerParam);
        viewPager.addOnPageChangeListener(this);
        android.support.v4.view.PagerAdapter pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);

        linearLayout = new LinearLayout(getContext());
        LayoutParams pointParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParam.addRule(ALIGN_PARENT_BOTTOM);
        pointParam.addRule(CENTER_HORIZONTAL);
        pointParam.bottomMargin = 30;
        addView(linearLayout, pointParam);                 //圆点指示
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setList(List<MainBean.TopStoriesBean> list) {
        this.list = list;
        prePosition = 0;
        linearLayout.removeAllViews();
        setOnIndicator(this.list.size());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setOnIndicator(int size) {
        for (int i = 0; i < size; i++) {
            ImageView point = new ImageView(getContext());
            point.setBackground(getBackground());           //此方法有api要求
            LinearLayout.LayoutParams pointView = new LinearLayout.LayoutParams(RADIUS, RADIUS);
            if (i == 0) {
                point.setEnabled(true);
            } else {
                pointView.leftMargin = RADIUS;          //圆点间隔
                point.setEnabled(false);
            }
            linearLayout.addView(point, pointView);
        }
    }

    //显示当前图片的位置
    public Drawable getBackground() {
        GradientDrawable enable = new GradientDrawable();
        enable.setColor(Color.parseColor("#ffffff"));
        enable.setShape(GradientDrawable.OVAL);                    //设置形状
        GradientDrawable normal = new GradientDrawable();
        normal.setColor(Color.parseColor("#888888"));
        normal.setShape(GradientDrawable.OVAL);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, enable);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position % list.size();
        linearLayout.getChildAt(this.prePosition).setEnabled(false);
        linearLayout.getChildAt(position).setEnabled(true);         //防止初始化时的意外
        this.prePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @NonNull
    public View setList(@NonNull ViewGroup container, int position, final List<MainBean.TopStoriesBean> list) {
        position = position % list.size();   //可以从最后一张划至第一张
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.banner, container, false);
        ImageView imageView = relativeLayout.findViewById(R.id.iv_head);
        TextView textView = relativeLayout.findViewById(R.id.tv_head);
        Picasso.with(getContext())
                .load(list.get(position).image)
                .fit()
                .centerCrop()
                .into(imageView);
        textView.setText(list.get(position).title);
        final int finalPosition = position;
        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(list.get(finalPosition).id));
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        return relativeLayout;
    }


    class PagerAdapter extends android.support.v4.view.PagerAdapter {
        PagerAdapter() {}

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = BannerAdapter.this.setList(container, position, list);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;                   //保证Banner可以一直滑动
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

}

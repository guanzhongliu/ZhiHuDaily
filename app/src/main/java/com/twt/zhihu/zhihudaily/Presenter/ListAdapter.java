package com.twt.zhihu.zhihudaily.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twt.zhihu.zhihudaily.Model.MainBean;
import com.twt.zhihu.zhihudaily.R;
import com.twt.zhihu.zhihudaily.View.ContentActivity;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private Context context;
    public List<MainBean> mainBeanList;
    private int times = 0;
    private static final int HEADER = 0;
    private static final int LIST = 1;
    private static final int ITEM_DATE = 0;

    public ListAdapter(Context context, MainBean latestBean) {
        this.context = context;
        mainBeanList = new ArrayList<>();
        mainBeanList.add(latestBean);
    }

    public void addData(MainBean latestBean) {
        mainBeanList.add(latestBean);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            BannerAdapter bannerAdapter;
            bannerAdapter = (BannerAdapter) LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
            return new ViewHolder(bannerAdapter);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == HEADER ? HEADER : LIST;         //判断是否为头部
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int realPosition = position - 1;
        if (position == HEADER) {
            ((BannerAdapter) holder.view).setList(mainBeanList.get(0).top_stories);         //头部为ViewPager
        } else {
            int group = 0;
            int length = 0;
            for (MainBean latestBean : mainBeanList) {
                if (realPosition >= latestBean.stories.size()) {
                    if (realPosition != latestBean.stories.size() || length <= latestBean.stories.size()) {
                        realPosition -= latestBean.stories.size();
                        group++;
                    }
                }
                length = latestBean.stories.size();
            }

            if(realPosition >= mainBeanList.get(group).stories.size()){
                realPosition -= mainBeanList.get(group).stories.size();
                group++;
            }                                               //解决前后两天列表长度不一而导致的group错乱的bug

            final int bean = group;
            final int beanPosition = realPosition;
            holder.textView.setText(mainBeanList.get(bean).stories.get(beanPosition).title);
            Picasso.with(context)
                    .load(mainBeanList.get(bean).stories.get(beanPosition).images.get(0))
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(mainBeanList.get(bean).stories.get(beanPosition).id));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            if (beanPosition == ITEM_DATE) {
                holder.item_date.setVisibility(View.VISIBLE);
                if (bean != 0) {

                    holder.item_date.setText(mainBeanList.get(bean).date);
                } else {
                    holder.item_date.setText("今日热闻");
                }                                               //日期栏
            } else {
                holder.item_date.setVisibility(View.GONE);
            }                                                   //其余时间隐藏


        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (MainBean latestBean : mainBeanList) {
            count += latestBean.stories.size();
        }
        return count + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView item_date;
        View view;
        View cardView;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            item_date = itemView.findViewById(R.id.item_date);
            cardView = itemView.findViewById(R.id.cardview);
            textView = itemView.findViewById(R.id.tv);
            imageView = itemView.findViewById(R.id.iv);
        }

    }

}

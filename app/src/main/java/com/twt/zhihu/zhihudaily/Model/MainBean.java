package com.twt.zhihu.zhihudaily.Model;

import java.util.List;

public class MainBean {
    public String date;
    public List<StoriesBean> stories;
    public List<TopStoriesBean> top_stories;


    public static class StoriesBean {
        public String date;
        public int type;
        public int id;
        public String ga_prefix;
        public String title;
        public boolean multipic;
        public List<String> images;
    }


    public static class TopStoriesBean {
        public String image;
        public int type;
        public int id;
        public String ga_prefix;
        public String title;
    }
}

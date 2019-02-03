package com.twt.zhihu.zhihudaily.model

class MainBean {
    var date: String? = null
    lateinit var stories: List<StoriesBean>
    var top_stories: List<TopStoriesBean>? = null

    class StoriesBean {
        var date: String? = null
        var type: Int = 0
        var id: Int = 0
        var ga_prefix: String? = null
        var title: String? = null
        var multipic: Boolean = false
        var images: List<String>? = null
    }


    class TopStoriesBean {
        var image: String? = null
        var type: Int = 0
        var id: Int = 0
        var ga_prefix: String? = null
        var title: String? = null
    }
}

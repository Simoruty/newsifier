package com.newsifier.dao.interfaces;

import com.newsifier.rss.bean.Feed;
import com.newsifier.rss.bean.News;

import java.util.List;

public interface NewsDAO {
    void insertNews(List<News> news, Feed f);
    void insertNews(List<Feed> f, int limit);
    List<News> getNews(Feed f);
}

package com.newsifier.dao.interfaces;

import com.newsifier.rss.bean.Feed;

import java.util.List;

public interface FeedDAO {
    void insertFeeds(List<Feed> feeds);
    List<Feed> getFeeds();
}

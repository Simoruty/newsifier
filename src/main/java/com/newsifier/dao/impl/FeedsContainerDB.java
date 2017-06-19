package com.newsifier.dao.impl;

import com.newsifier.rss.bean.Feed;

import java.util.ArrayList;

public class FeedsContainerDB {

    private String _id;
    private String _rev;
    private ArrayList<Feed> feeds;

    public String getId() {
        return _id;
    }

    public String getRev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public ArrayList<Feed> getFeedslist() {
        return feeds;
    }

    public void setFeeds(ArrayList<Feed> feeds) {
        this.feeds = feeds;
    }

    public void addFeed(Feed f) {
        if (!feeds.contains(f)) {
            feeds.add(f);
        }
    }
}

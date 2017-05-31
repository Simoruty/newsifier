package com.newsifier.dao.impl;

import com.newsifier.rss.bean.News;

import java.util.ArrayList;

public class NewsDB {

    private String _id;
    private String _rev;
    private ArrayList<News> news;

    public String getId() {
        return _id;
    }

    public String getRev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public ArrayList<News> getNewslist() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public void addNews(News n){
        if (!news.contains(n)){
            news.add(n);
        }
    }
}

package com.newsifier.dao.impl;

import com.newsifier.watson.bean.NewsWithKeywords;

import java.util.ArrayList;

/**
 * Represents a generic category in the DB
 * For each category there is a set of related news discovered by NLU 
 */
public class CategoryDB {
    private String _id;
    private String _rev;
    private ArrayList<NewsWithKeywords> news;

    public String getId() {
        return _id;
    }

    public String getRev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public ArrayList<NewsWithKeywords> getNewslist() {
        return news;
    }

    public void setNews(ArrayList<NewsWithKeywords> news) {
        this.news = news;
    }

    public void addNews(NewsWithKeywords n) {
        if (!news.contains(n)) {
            news.add(n);
        }
    }

}

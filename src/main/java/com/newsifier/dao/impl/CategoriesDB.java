package com.newsifier.dao.impl;

import com.newsifier.watson.bean.NewsNLUByCat;

import java.util.ArrayList;

public class CategoriesDB {
    private String _id;
    private String _rev;
    private ArrayList<NewsNLUByCat> news;

    public String getId() {
        return _id;
    }

    public String getRev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public ArrayList<NewsNLUByCat> getNewslist() {
        return news;
    }

    public void setNews(ArrayList<NewsNLUByCat> news) {
        this.news = news;
    }

    public void addNews(NewsNLUByCat n){
        if (!news.contains(n)){
            news.add(n);
        }
    }

}

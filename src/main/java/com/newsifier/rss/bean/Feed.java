package com.newsifier.rss.bean;

import java.util.ArrayList;

public class Feed {
    //TODO POJO
    private String name;
    private ArrayList<String> urls;

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Feed(String name, ArrayList<String> urls) {
        this.name = name;
        this.urls = urls;
    }

    public Feed(String name) {
        this.name = name;
    }
}

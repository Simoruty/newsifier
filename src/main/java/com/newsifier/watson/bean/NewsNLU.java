package com.newsifier.watson.bean;

import java.util.List;

public class NewsNLU {

    private String urlNews;
    private List<String> categories;
    private List<String> keywords;

    public NewsNLU(String urlNews, List<String> categories, List<String> keywords) {
        this.urlNews = urlNews;
        this.categories = categories;
        this.keywords = keywords;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getUrlNews() {
        return urlNews;
    }

    public void setUrlNews(String urlNews) {
        this.urlNews = urlNews;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}

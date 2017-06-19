package com.newsifier.watson.bean;

public class NewsWithKeywords {

    private String uri;
    private String keywords;


    public NewsWithKeywords(String uri, String keywords) {
        this.uri = uri;
        this.keywords = keywords;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsWithKeywords news = (NewsWithKeywords) o;

        return uri.equals(news.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
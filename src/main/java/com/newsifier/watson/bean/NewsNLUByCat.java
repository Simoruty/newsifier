package com.newsifier.watson.bean;

public class NewsNLUByCat {

    private String uri;
    private String keywords;


    public NewsNLUByCat(String uri, String keywords) {
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

        NewsNLUByCat news = (NewsNLUByCat) o;

        return uri.equals(news.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
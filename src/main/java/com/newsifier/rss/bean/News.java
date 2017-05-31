package com.newsifier.rss.bean;

public class News {

    private String title;
    private String uri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public News(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return uri.equals(news.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}

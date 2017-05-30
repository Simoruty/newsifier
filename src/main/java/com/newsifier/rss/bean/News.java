package com.newsifier.rss.bean;

public class News {

    private String feedName;
    private String title;
    private String uri;

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

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

    public News(String feedName, String title, String uri) {
        this.feedName = feedName;
        this.title = title;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "News{" +
                "feedName='" + feedName + '\'' +
                ", title='" + title + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}

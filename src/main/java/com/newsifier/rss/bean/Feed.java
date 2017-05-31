package com.newsifier.rss.bean;

import java.net.URL;

public class Feed{
    private String name;
    private URL url;

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Feed(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    public Feed(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "name='" + name + '\'' +
                ", url=" + url +
                '}';
    }


    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (!name.equals(feed.name)) return false;
        return url.equals(feed.url);
    }
}

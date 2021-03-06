package com.newsifier.watson.bean;

/**
 * Represent a dataset entry
 */
public class DatasetEntry {

    private String category;
    private String keywords;


    public DatasetEntry(String category, String keywords) {
        this.category = category;
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public String getKeywords() {
        return keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatasetEntry that = (DatasetEntry) o;

        if (!category.equals(that.category)) return false;
        return keywords.equals(that.keywords);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + keywords.hashCode();
        return result;
    }
}

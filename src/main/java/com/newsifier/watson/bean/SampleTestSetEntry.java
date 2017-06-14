package com.newsifier.watson.bean;


public class SampleTestSetEntry {
    private String keywords;
    private String classificatedClass;
    private double scoreClassification;

    public SampleTestSetEntry(String keywords, String classificatedClass, double scoreClassification) {
        this.keywords = keywords;
        this.classificatedClass = classificatedClass;
        this.scoreClassification = scoreClassification;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getClassificatedClass() {
        return classificatedClass;
    }

    public double getScoreClassification() {
        return scoreClassification;
    }

    @Override
    public String toString() {
        return "SampleTestSetEntry{" +
                "keywords='" + keywords + '\'' +
                ", classificatedClass='" + classificatedClass + '\'' +
                ", scoreClassification=" + scoreClassification +
                '}';
    }
}

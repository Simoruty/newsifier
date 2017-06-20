package com.newsifier.watson.bean;

/**
 * Represent a test result set entry
 */
public class TestResultsetEntry {
    private String keywords;
    private String classificatedClass;
    private double scoreClassification;

    public TestResultsetEntry(String keywords, String classificatedClass, double scoreClassification) {
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
        return "TestResultsetEntry{" +
                "keywords='" + keywords + '\'' +
                ", classificatedClass='" + classificatedClass + '\'' +
                ", scoreClassification=" + scoreClassification +
                '}';
    }
}

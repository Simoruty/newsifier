package com.newsifier;

public class Settings {

    // Set limit news for feed
    private int limitNews = 10;
    public int getLimitNews() {
        return limitNews;
    }

    //limit keywords from news
    private int limitKeywordsNews = 20;
    public int getLimitKeywordsNews() {
        return limitKeywordsNews;
    }

    // "score" is the threeshold for retrieve the categories
    // "relevance" is the threeshold for retrieve the keywords

    private double score = 0.5;
    private double relevance = 0.5;

    public double getScore() {
        return score;
    }
    public double getRelevance() {
        return relevance;
    }

    // Training percent
    private double trainingDimension = 0.7;

    public double getTrainingDimension() {
        return trainingDimension;
    }

    public Settings(int limitNews, int limitKeywordsNews, double score, double relevance, double trainingDimension) {
        this.limitNews = limitNews;
        this.limitKeywordsNews = limitKeywordsNews;
        this.score = score;
        this.relevance = relevance;
        this.trainingDimension = trainingDimension;
    }
}

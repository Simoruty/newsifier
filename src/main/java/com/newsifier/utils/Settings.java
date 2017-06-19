package com.newsifier.utils;

public class Settings {

    // Set limit news for feed
    private int limitNews = 10;

    public int getLimitNews() {
        return limitNews;
    }

    //limit keywords from news
    private int limitKeywordsNews = 10;

    public int getLimitKeywordsNews() {
        return limitKeywordsNews;
    }

    // "score" is the threshold to retrieve the categories
    // "relevance" is the threshold to retrieve the keywords

    private double score = 0.5;
    private double relevance = 0.5;

    public double getScore() {
        return score;
    }

    public double getRelevance() {
        return relevance;
    }

    // Training percentage
    private double trainingDimension = 0.7;

    public double getTrainingDimension() {
        return trainingDimension;
    }

    public Settings(String limitNews, String limitKeywordsNews, String score, String relevance, String trainingDimension) {

        if (!limitNews.isEmpty())
            this.limitNews = Integer.parseInt(limitNews);
        if (!limitKeywordsNews.isEmpty())
            this.limitKeywordsNews = Integer.parseInt(limitKeywordsNews);
        if (!score.isEmpty())
            this.score = Double.parseDouble(score);
        if (!relevance.isEmpty())
            this.relevance = Double.parseDouble(relevance);
        if (!trainingDimension.isEmpty())
            this.trainingDimension = Double.parseDouble(trainingDimension);
    }

    @Override
    public String toString() {
        return "Settings{" +
                "limitNews=" + limitNews +
                ", limitKeywordsNews=" + limitKeywordsNews +
                ", score=" + score +
                ", relevance=" + relevance +
                ", trainingDimension=" + trainingDimension +
                '}';
    }
}

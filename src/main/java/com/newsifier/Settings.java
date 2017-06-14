package com.newsifier;

public class Settings {

    // Set limit news for feed
    private static final int LIMITNEWS = 10;
    public static int getLimitnews() {
        return LIMITNEWS;
    }

    //limit keywords from news
    private static final int LIMITKEYWORDSNEWS = 20;
    public static int getLimitkeywordsnews() {
        return LIMITKEYWORDSNEWS;
    }

    // "score" is the threeshold for retrieve the categories
    // "relevance" is the threeshold for retrieve the keywords

    private static final double SCORE = 0.5;
    private static final double RELEVANCE = 0.5;

    public static double getScore() {
        return SCORE;
    }
    public static double getRelevance() {
        return RELEVANCE;
    }

    // Training percent
    private static final double TRAININGDIMENSION = 0.7;

    public static double getTrainingdimension() {
        return TRAININGDIMENSION;
    }
}

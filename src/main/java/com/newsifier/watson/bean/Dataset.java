package com.newsifier.watson.bean;

public class Dataset {
    private String trainingSet;
    private int trainingSetSize;

    private String testSet;
    private int testSetSize;

    public Dataset(String trainingSet, int trainingSetSize, String testSet, int testSetSize) {
        this.trainingSet = trainingSet;
        this.trainingSetSize = trainingSetSize;
        this.testSet = testSet;
        this.testSetSize = testSetSize;
    }

    public String getTrainingSet() {
        return trainingSet;
    }

    public String getTestSet() {
        return testSet;
    }

    public int getTrainingSetSize() {
        return trainingSetSize;
    }

    public int getTestSetSize() {
        return testSetSize;
    }
}

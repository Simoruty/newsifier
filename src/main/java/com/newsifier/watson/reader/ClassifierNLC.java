package com.newsifier.watson.reader;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifiers;
import com.ibm.watson.developer_cloud.service.exception.BadRequestException;
import com.newsifier.Credentials;
import com.newsifier.Logger;
import com.newsifier.watson.bean.Dataset;
import com.newsifier.watson.bean.SampleDatasetEntry;
import com.newsifier.watson.bean.SampleTestSetEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class ClassifierNLC {


    private static NaturalLanguageClassifier service;
    private static Classifier classifier;

    public void createClassifier(File file, String classifierName) {

        try {
        String classifierId = getClassifierId(classifierName);

        if (!classifierId.isEmpty()) {
            classifier = service.getClassifier(classifierId).execute();
            Logger.log("Classifier found");
            Logger.webLog("Classifier found");
        } else {
            classifier = service.createClassifier(classifierName, "en", file).execute();
            Logger.log("Classifier created at " + classifier.getCreated());
            Logger.webLog("Classifier created at " + classifier.getCreated());
        }
    }catch(BadRequestException e){
            Logger.webLog("Data too small for the classifier's creation");
            Logger.logErr("Data too small for the classifier's creation");
        }
    }

    private String getClassifierId(String classifierName){

        String classifierId = "";

        Classifiers classifiers = service.getClassifiers().execute();
        for (Classifier classifier1 : classifiers.getClassifiers()) {
            if (classifier1.getName().equals(classifierName)) {
                classifierId = classifier1.getId();
            }
        }
        return classifierId;
    }

    public void eraseClassifier(String classifierName){

        String classifierId = getClassifierId(classifierName);

        if (!classifierId.isEmpty()) {
            service.deleteClassifier(classifierId).execute();
            Logger.log("Classifier deleted");
            Logger.webLog("Classifier deleted");
        }
        else {
            Logger.log("Classifier not found");
            Logger.webLog("Classifier not found");
        }
    }

    public Dataset splitDataset(File datasetFile, double trainingDimension) {

        StringBuilder trainingSet = new StringBuilder();
        StringBuilder testSet = new StringBuilder();

        List<SampleDatasetEntry> datasetEntries = parseCSV(datasetFile);

        int numberSampleLimit = (int) (datasetEntries.size() * trainingDimension);

        for (int i = 0; i < numberSampleLimit; i++) {
            trainingSet.append(datasetEntries.get(i).getKeywords()).append(",").append(datasetEntries.get(i).getCategory()).append("\n");
        }

        for (int i = numberSampleLimit; i < datasetEntries.size(); i++) {
            testSet.append(datasetEntries.get(i).getKeywords()).append("\n");
        }
        Dataset datasetOutput = new Dataset(trainingSet.toString(), numberSampleLimit, testSet.toString(), (datasetEntries.size() - numberSampleLimit));

        /*
        Logger.log("-------------------- Training set ----- Size: " + datasetOutput.getTrainingSetSize() + " ----");
        Logger.log(datasetOutput.getTrainingSet());
        Logger.log("-------------------- ------------ -------------------");

        Logger.log("------------------------ Test set ----- Size: " + datasetOutput.getTestSetSize() + " ----");
        Logger.log(datasetOutput.getTestSet());
        Logger.log("-------------------- ------------ -------------------");
        */

        return datasetOutput;

    }

    private static List<SampleDatasetEntry> parseCSV(File file) {

        List<SampleDatasetEntry> datasets = new ArrayList<>();
        CSVParser parser;
        try {
            parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withAllowMissingColumnNames());

            for (CSVRecord record : parser) {
                SampleDatasetEntry sampleDatasetEntry = new SampleDatasetEntry(record.get(1), record.get(0));
                datasets.add(sampleDatasetEntry);
                //System.out.printf("%s\t%s", record.get(1), record.get(0));
            }
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(datasets);
        return datasets;
    }


    public ClassifierNLC() {
        JsonObject credentials = getCredentials("natural_language_classifier", Credentials.getUsernameNlc(), Credentials.getPasswordNlc());
        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();
        service = new NaturalLanguageClassifier(username, password);

        Logger.log("NLC Service init");
    }

    public List<SampleTestSetEntry> testClassifier(File testSet, String classifierName) {
        try {
            List<String> testSample = FileUtils.readLines(testSet, "UTF-8");

            List<SampleTestSetEntry> testSetEntries = new ArrayList<>(testSample.size());

            String classifierId = getClassifierId(classifierName);

            // Classifier is not created
            if (classifierId.isEmpty())
                return null;

            boolean success = false;
            while (!success) {
                try {
                    for (String sample : testSample) {
                        Classification classification = service.classify(classifierId, sample).execute();
                        SampleTestSetEntry setEntry = new SampleTestSetEntry(sample, classification.getTopClass(), classification.getClasses().get(0).getConfidence());
                        testSetEntries.add(setEntry);

                    }
                    Logger.webLog("The classifier is ready");
                    success = true;

                } catch (com.ibm.watson.developer_cloud.service.exception.ConflictException ex) {
                    Logger.logErr(ex.getMessage());
                    Logger.webLog("The classifier is being trained.. Please wait");
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        Logger.logErr(e.getMessage());
                    }
                } catch (com.ibm.watson.developer_cloud.service.exception.InternalServerErrorException ex1) {
                    Logger.logErr(ex1.getMessage());
                    return null;
                }
            }
            return testSetEntries;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public double precisionClassifier(File dataset, List<SampleTestSetEntry> testSetResult) {

        List<SampleDatasetEntry> datasetEntries = parseCSV(dataset);

        // True Positive
        double tp = 0.0;
        double tot = 0.0;

        for (SampleTestSetEntry setEntry : testSetResult) {
            tot++;
            for (SampleDatasetEntry datasetEntry : datasetEntries) {
                if (datasetEntry.getKeywords().equals(setEntry.getKeywords()) &&
                        datasetEntry.getCategory().equals(setEntry.getClassificatedClass())) {
                    tp++;
                    break;
                }
            }
        }

        Logger.log(" True positive: " + tp + " Total: " + tot);
        Logger.webLog(" True positive: " + tp + " Total: " + tot);

        if (tot == 0.0)
            return 0.0;

        return tp / tot;
    }
}

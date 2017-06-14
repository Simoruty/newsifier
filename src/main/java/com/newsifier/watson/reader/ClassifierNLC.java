package com.newsifier.watson.reader;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifiers;
import com.newsifier.Credentials;
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

        boolean found =false;
        String classifierId = "";

        Classifiers classifiers = service.getClassifiers().execute();
        for (Classifier classifier1 : classifiers.getClassifiers()) {
            if (classifier1.getName().equals(classifierName))
            {
                found=true;
                classifierId = classifier1.getId();
            }
        }

        if (found){
            classifier = service.getClassifier(classifierId).execute();
            System.out.println("Classifier found");
        }
        else {
            classifier = service.createClassifier(classifierName, "en", file).execute();
            System.out.println("Classifier created at " + classifier.getCreated());
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
        System.out.println("-------------------- Training set ----- Size: " + datasetOutput.getTrainingSetSize() + " ----");
        System.out.println(datasetOutput.getTrainingSet());
        System.out.println("-------------------- ------------ -------------------");

        System.out.println("------------------------ Test set ----- Size: " + datasetOutput.getTestSetSize() + " ----");
        System.out.println(datasetOutput.getTestSet());
        System.out.println("-------------------- ------------ -------------------");
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
        JsonObject credentials = getCredentials("natural-language-classifier", Credentials.getUsernameNlc(), Credentials.getPasswordNlc());
        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();
        service = new NaturalLanguageClassifier(username, password);

        System.out.println("NLC Service init");
    }

    public List<SampleTestSetEntry> testClassifier(File testSet) {
        try {
            List<String> testSample = FileUtils.readLines(testSet, "UTF-8");

            List<SampleTestSetEntry> testSetEntries = new ArrayList<>(testSample.size());

            String classifierId = classifier.getId();

            boolean success = false;
            while (!success) {
                try {
                    for (String sample : testSample) {
                        Classification classification = service.classify(classifierId, sample).execute();
                        SampleTestSetEntry setEntry = new SampleTestSetEntry(sample, classification.getTopClass(), classification.getClasses().get(0).getConfidence());
                        testSetEntries.add(setEntry);

                    }
                    success = true;

                } catch (com.ibm.watson.developer_cloud.service.exception.ConflictException ex) {
                    System.err.println(ex.getMessage());
                    try {
                        Thread.sleep(80000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }
                }
                catch (com.ibm.watson.developer_cloud.service.exception.InternalServerErrorException ex1){
                    System.err.println(ex1.getMessage());
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

        System.out.println(" True positive: " + tp + " Total: " + tot);

        return tp / tot;
    }
}

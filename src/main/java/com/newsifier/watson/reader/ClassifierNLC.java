package com.newsifier.watson.reader;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.newsifier.Credentials;

import java.io.File;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class ClassifierNLC {


    private static NaturalLanguageClassifier service;

    public void createClassifier(File file){
        Classifier classifier = service.createClassifier("NLCClassifier", "en", file).execute();
        System.out.println("Classifier created at " + classifier.getCreated());
    }


    public ClassifierNLC() {
        JsonObject credentials = getCredentials("natural-language-classifier", Credentials.getUsernameNlc(), Credentials.getPasswordNlc());
        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();
        service = new NaturalLanguageClassifier(username,password);

        System.out.println("NLC Service created");
    }
}

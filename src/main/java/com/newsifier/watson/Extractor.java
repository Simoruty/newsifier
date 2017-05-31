package com.newsifier.watson;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;

import java.util.List;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class Extractor {

    private static final String USERNAME_NLU = "61ee67ae-0385-44d0-bcff-46c618ca3d44";
    private static final String PASSWORD_NLU = "HrJqOhhtim5P" ;

    public void extractInfo(String urlNews){

        JsonObject credentials = getCredentials("natural-language-understanding", USERNAME_NLU, PASSWORD_NLU);

        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();

        NaturalLanguageUnderstanding service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27, username, password
        );




        EntitiesOptions entities = new EntitiesOptions.Builder().sentiment(true).limit(1).build();
        Features features = new Features.Builder().entities(entities).build();
        AnalyzeOptions parameters = new AnalyzeOptions.Builder().url(urlNews).features(features).build();
        AnalysisResults results = service.analyze(parameters).execute();
/*
        List<CategoriesResult> cats = results.getCategories();


        for (CategoriesResult cat : cats) {
            System.out.println("The category : " + cat.getLabel() + " with score " + cat.getScore());
        }

        List<KeywordsResult> keys = results.getKeywords();

        for (KeywordsResult key : keys) {
            System.out.println("The keyword: " +key.getText() + " with score " + key.getRelevance());
        }
*/
    }
}

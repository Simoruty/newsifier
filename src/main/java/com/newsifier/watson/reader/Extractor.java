package com.newsifier.watson.reader;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.newsifier.watson.bean.NewsNLU;

import java.util.ArrayList;
import java.util.List;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class Extractor {

    private NaturalLanguageUnderstanding service;
    private Features features;

    private static final String USERNAME_NLU = "61ee67ae-0385-44d0-bcff-46c618ca3d44";
    private static final String PASSWORD_NLU = "HrJqOhhtim5P" ;

    public Extractor(int limit) {

        JsonObject credentials = getCredentials("natural-language-understanding", USERNAME_NLU, PASSWORD_NLU);

        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();

         service = new NaturalLanguageUnderstanding(
                NaturalLanguageUnderstanding.VERSION_DATE_2017_02_27,
                username,
                password
        );

        CategoriesOptions categories = new CategoriesOptions();

        KeywordsOptions keywords = new KeywordsOptions.Builder()
                .emotion(false)         //Set this to true to enable emotion analysis for detected keywords
                .sentiment(false)       //Set this to true to enable sentiment analysis for detected keywords
                .limit(limit)              // Maximum number of keywords to return
                .build();

        features = new Features.Builder()
                .categories(categories)
                .keywords(keywords)
                .build();

    }

    public NewsNLU extractInfo(String urlNews) {

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .url(urlNews)
                .features(features)
                .build();

        AnalysisResults results = service
                .analyze(parameters)
                .execute();

        List<CategoriesResult> cats = results.getCategories();
        List<String> categoriesLabel = new ArrayList();
        List<String> keywordsLabel = new ArrayList();

        for (CategoriesResult cat : cats) {
            //System.out.println("The category : " + cat.getLabel() + " with score " + cat.getScore());
            categoriesLabel.add(cat.getLabel());
        }

        List<KeywordsResult> keys = results.getKeywords();

        for (KeywordsResult key : keys) {
            //System.out.println("The keyword: " +key.getText() + " with score " + key.getRelevance());
            keywordsLabel.add(key.getText());
        }

        return new NewsNLU(urlNews,categoriesLabel,keywordsLabel);
    }
}

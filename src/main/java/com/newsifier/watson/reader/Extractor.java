package com.newsifier.watson.reader;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.*;
import com.newsifier.Credentials;
import com.newsifier.watson.bean.NewsNLU;

import java.util.ArrayList;
import java.util.List;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class Extractor {

    private NaturalLanguageUnderstanding service;
    private Features features;

    public Extractor(int limit) {

        JsonObject credentials = getCredentials("natural-language-understanding", Credentials.getUsernameNlu(), Credentials.getPasswordNlu());

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

    public NewsNLU extractInfo(String urlNews, double score, double relevance) {

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
            if (cat.getScore() > score) {
                categoriesLabel.add(cat.getLabel().replaceAll("\\s+", "_").replaceAll(",",""));
            }
        }

        List<KeywordsResult> keys = results.getKeywords();

        for (KeywordsResult key : keys) {
            if (key.getRelevance() > relevance) {
                //System.out.println("The keyword: " + key.getText() + " with score " + key.getRelevance());
                keywordsLabel.add(key.getText());
            }
        }

        return new NewsNLU(urlNews, categoriesLabel, keywordsLabel);
    }
}

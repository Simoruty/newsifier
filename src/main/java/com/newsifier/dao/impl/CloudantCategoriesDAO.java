package com.newsifier.dao.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.newsifier.dao.interfaces.CategoriesDAO;
import com.newsifier.watson.bean.NewsNLU;
import com.newsifier.watson.bean.NewsNLUByCat;

import java.util.ArrayList;
import java.util.List;

import static com.newsifier.dao.impl.CloudantUtilsDAO.*;

public class CloudantCategoriesDAO implements CategoriesDAO {

    @Override
    public void insertCategories(List<NewsNLU> news) {
        for (NewsNLU newsNLU : news) {
            insertCategories(newsNLU);
        }
    }

    @Override
    public void insertCategories(NewsNLU newsNLU) {

        for (String cat : newsNLU.getCategories()) {

            StringBuilder keywordsConcat = new StringBuilder();
            for (String s : newsNLU.getKeywords()) {
                String mod = s.replaceAll("\\s+", "_");
                keywordsConcat.append(mod).append(" ");
            }
            keywordsConcat = keywordsConcat.deleteCharAt(keywordsConcat.length() - 1);

            NewsNLUByCat newsNLUByCat = new NewsNLUByCat(newsNLU.getUrlNews(), keywordsConcat.toString());

            JsonObject cloudantCats = new JsonObject();
            cloudantCats.addProperty("_id", cat);
            JsonArray newsforCatArray = new JsonArray();

            JsonObject newsforCatMap = new JsonObject();
            newsforCatMap.addProperty("uri", newsNLUByCat.getUri());
            newsforCatMap.addProperty("keywords", newsNLUByCat.getKeywords());
            newsforCatArray.add(newsforCatMap);

            cloudantCats.add("news", newsforCatArray);

            while (!createConnectionWithDBCategories()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {

                getDbCategories().save(cloudantCats);

                System.out.println("Created document Categories " + cat + " saved");

            } catch (com.cloudant.client.org.lightcouch.DocumentConflictException e) {

                //Reading the existing document
                JsonObject read = jsonObjectreaderFromCloudantId(cat, getDbCategories());
                CategoriesDB newsFromCloudant = new Gson().fromJson(read, CategoriesDB.class);

                //Added the new news to existing list
                newsFromCloudant.addNews(newsNLUByCat);

                //Remove old document
                getDbCategories().remove(read);

                //Remove revision id for the new creation
                newsFromCloudant.set_rev(null);

                try {
                    //Save the updated document
                    getDbCategories().save(newsFromCloudant);
                    System.out.println("Added new news keywords for the category " + cat + " saved");
                } catch (com.cloudant.client.org.lightcouch.CouchDbException e1) {
                    System.err.println(e1.getMessage());
                }
            }

        }
    }


    @Override
    public List<NewsNLUByCat> getNewsbyCat(String cat) {
        createConnectionWithDBCategories();

        JsonObject read = jsonObjectreaderFromCloudantId(cat, getDbCategories());
        CategoriesDB newsFromCloudant = new Gson().fromJson(read, CategoriesDB.class);
        return newsFromCloudant.getNewslist();
    }

    @Override
    public String newsToCSV(String cat) {
        StringBuilder csvFile = new StringBuilder();
        List<NewsNLUByCat> newsNLUByCats = getNewsbyCat(cat);
        for (NewsNLUByCat newsNLUByCat : newsNLUByCats) {
            csvFile.append(newsNLUByCat.getKeywords()).append(",").append(cat).append("\n");
        }
        return csvFile.toString();
    }


    //TODO
    @Override
    public List<String> allCategories() {

        List<String> allcat = new ArrayList<>();

        createConnectionWithDBCategories();

        //Query for retrieve all documents of db
        JsonObject read = jsonObjectreaderFromCloudantId("_all_docs", getDbCategories());
        JsonArray jsonElements = read.getAsJsonArray("rows");

        for (JsonElement jsonElement : jsonElements) {
            String id = jsonElement.getAsJsonObject().get("id").getAsString();
            allcat.add(id);
        }

        return allcat;
    }


}

package com.newsifier.dao.impl;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.rss.bean.Feed;
import com.newsifier.rss.bean.News;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CloudantFeedDAO implements FeedDAO {

    private static Database db;

    @Override
    public void insertFeeds(List<Feed> feeds) {

        createConnection();
        JsonObject cloudantFeeds = new JsonObject();
        cloudantFeeds.addProperty("_id", "Feeds");
        JsonArray feedsArr = new JsonArray();

        for (Feed feed : feeds) {
            JsonObject feedMap = new JsonObject();
            feedMap.addProperty("name", feed.getName());
            feedMap.addProperty("url", String.valueOf(feed.getUrl()));
            feedsArr.add(feedMap);
        }
        cloudantFeeds.add("feeds", feedsArr);

        try {
            db.save(cloudantFeeds);
            System.out.println("Created document Feeds saved");


        } catch (com.cloudant.client.org.lightcouch.DocumentConflictException e) {

            //Reading the existing document
            JsonObject read = jsonObjectreaderFromCloudantId("Feeds");
            CloudantFeed feedFromCloudant = new Gson().fromJson(read, CloudantFeed.class);

            //Added the new feed to existing list
            for (Feed feed : feeds) {
                feedFromCloudant.addFeed(feed);
            }

            //Remove old document
            db.remove(read);

            //Remove revision id for the new creation
            feedFromCloudant.set_rev(null);

            //Save the updated document
            db.save(feedFromCloudant);
        }

    }

    @Override
    public List<Feed> getFeeds() {

        createConnection();

        JsonObject read = jsonObjectreaderFromCloudantId("Feeds");
        CloudantFeed feedFromCloudant = new Gson().fromJson(read, CloudantFeed.class);
        return feedFromCloudant.getFeedslist();
    }

    private JsonObject jsonObjectreaderFromCloudantId(String id) {
        JsonObject output;
        InputStream is = db.find(id);
        int i;
        char c;
        String doc = "";
        try {
            while ((i = is.read()) != -1) {
                c = (char) i;
                doc += c;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        output = parser.parse(doc).getAsJsonObject();
        return output;
    }

    @Override
    public List<News> getNewsFromFeed(Feed f) {
        return null;
    }

    private static void createConnection() {
        // Create a new CloudantClient instance for account endpoint example.cloudant.com
        CloudantClient client = ClientBuilder.account("0eb7cef6-2fbb-45c5-8e21-ca56349b4870-bluemix")
                .username("0eb7cef6-2fbb-45c5-8e21-ca56349b4870-bluemix")
                .password("c8e8913e64a247ae74fa0e90339335a27ed63044331d16cefdbb1138ea5739f7")
                .build();

        // Show the server version
        System.out.println("Server Version: " + client.serverVersion());

        // Get a List of all the databases this Cloudant account
        List<String> databases = client.getAllDbs();
        System.out.println("All my databases : ");
        for (String db : databases) {
            System.out.println(db);
        }

        // Create a new database.
        //client.createDB("example_db");
        db = client.database("newsifier_db", true);

    }
}

package com.newsifier.dao.impl;

import com.cloudant.client.org.lightcouch.TooManyRequestsException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.newsifier.Logger;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.rss.bean.Feed;

import java.util.ArrayList;
import java.util.List;

import static com.newsifier.dao.impl.CloudantUtilsDAO.*;

public class CloudantFeedDAO implements FeedDAO {

    @Override
    public void insertFeeds(List<Feed> feeds) {

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
            getDbMaster().save(cloudantFeeds);
            Logger.log("Created cloudant document : Feeds");
            Logger.log("Created cloudant document : Feeds");


        } catch (com.cloudant.client.org.lightcouch.DocumentConflictException e) {

            //Reading the existing document
            JsonObject read = jsonObjectreaderFromCloudantId("Feeds", getDbMaster());
            FeedDB feedFromCloudant = new Gson().fromJson(read, FeedDB.class);

            //Added the new feed to existing list
            for (Feed feed : feeds) {
                feedFromCloudant.addFeed(feed);
            }

            //Remove old document
            getDbMaster().remove(read);

            //Remove revision id for the new creation
            feedFromCloudant.set_rev(null);

            //Save the updated document
            getDbMaster().save(feedFromCloudant);
        }
    }

    @Override
    public List<Feed> getFeeds() {
        try {
            JsonObject read = jsonObjectreaderFromCloudantId("Feeds", getDbMaster());
            FeedDB feedFromCloudant = new Gson().fromJson(read, FeedDB.class);
            return feedFromCloudant.getFeedslist();
        }
        catch (TooManyRequestsException e){
            Logger.webLog("Error: too_many_requests. Reason: You've exceeded your current limit of 20 requests per second for lookup class\n");
            Logger.logErr("Error: too_many_requests. Reason: You've exceeded your current limit of 20 requests per second for lookup class\n");
            return new ArrayList<>();
        }
    }

    public CloudantFeedDAO() {
        createConnectionWithCloudant();
    }
}

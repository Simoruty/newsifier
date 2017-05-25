package com.newsifier.dao.impl;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.newsifier.dao.interfaces.FeedDAO;
import com.newsifier.rss.bean.Feed;

import javax.naming.NamingException;
import java.util.List;

public class CloudantFeedDAO implements FeedDAO {

    private Database db;

    @Override
    public void insertFeeds(List<Feed> feeds) {

        /*
        Map<String, Object> h;
        for (Feed feed : feeds) {
            h = new HashMap<>();
            h.put("_id", feed.getName());
            h.put("url", feed.getUrls());
            db.save(h);
        }
        */
        db.save(feeds);
    }

    @Override
    public List<Feed> getFeeds(Feed f) {
        // TODO
        return null;
    }

    public CloudantFeedDAO() throws NamingException {
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
        System.out.println(db.info());

    }
}

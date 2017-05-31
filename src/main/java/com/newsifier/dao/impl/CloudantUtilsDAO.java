package com.newsifier.dao.impl;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class CloudantUtilsDAO {

    private static Database db;

    protected static Database getDb() {
        return db;
    }

    // Credential Cloudant
    private static final String USERNAME_DB = "0eb7cef6-2fbb-45c5-8e21-ca56349b4870-bluemix";
    private static final String PASSWORD_DB = "c8e8913e64a247ae74fa0e90339335a27ed63044331d16cefdbb1138ea5739f7";

    protected static JsonObject jsonObjectreaderFromCloudantId(String id) {
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

    private static CloudantClient getConnection() {
        JsonObject credentials = getCredentials("cloudantNoSQLDB", USERNAME_DB, PASSWORD_DB);

        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();

        try {
            CloudantClient client = ClientBuilder.url(new URL("https://" + username + ".cloudant.com"))
                    .username(username)
                    .password(password)
                    .build();
            return client;
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    protected static void createConnectionWithDB() {
        // Create a new CloudantClient instance
        CloudantClient client = getConnection();

        // Show the server version
        //System.out.println("Server Version: " + client.serverVersion());

        // Get a List of all the databases this Cloudant account
//        List<String> databases = client.getAllDbs();
//        System.out.println("All my databases : ");
//        for (String db : databases) {
//            System.out.println(db);
//        }

        // Create a new database.
        //client.createDB("example_db");
        db = client.database("newsifier_db", true);
    }

}

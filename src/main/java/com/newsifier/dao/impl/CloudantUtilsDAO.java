package com.newsifier.dao.impl;

import static com.newsifier.dao.impl.Utils.getCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.newsifier.Credentials;

public class CloudantUtilsDAO {

    private static Database dbMaster;
    private static Database dbCategories;
    private static CloudantClient cloudantClient;

    protected static Database getDbMaster() {
        return dbMaster;
    }

    protected static Database getDbCategories() {
        return dbCategories;
    }

    protected static JsonObject jsonObjectreaderFromCloudantId(String id, Database d) {
        JsonObject output;
        InputStream is = d.find(id);
        BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        int i;
        char c;
        String doc = "";
        try {
            while ((i = in.read()) != -1) {
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

    private static CloudantClient getCloudantClient() {
        JsonObject credentials = getCredentials("cloudantNoSQLDB", Credentials.getUsernameDbCloudant(), Credentials.getPasswordDbCloudant());

        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();

        try {
            cloudantClient = ClientBuilder.url(new URL("https://" + username + ".cloudant.com"))
                    .username(username)
                    .password(password)
                    .build();
            return cloudantClient;
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        }

        return null;
    }

    protected static boolean createConnectionWithDBMaster() {

        try {
            // Create a new CloudantClient instance
            cloudantClient = getCloudantClient();

            // Show the server version
            //System.out.println("Server Version: " + client.serverVersion());

            // Get a List of all the databases this Cloudant account
//        List<String> databases = client.getAllDbs();
//        System.out.println("All my databases : ");
//        for (String dbMaster : databases) {
//            System.out.println(dbMaster);
//        }

            // Create a new database.
            dbMaster = cloudantClient.database("newsifier_db", true);
            return true;
        } catch (com.cloudant.client.org.lightcouch.CouchDbException ex) {
            System.err.println("Error retrieving server response");
            return false;
        }
    }

    protected static boolean createConnectionWithDBCategories() {

        try {
            // Create a new CloudantClient instance
            cloudantClient = getCloudantClient();

            // Create a new database.
            dbCategories = cloudantClient.database("newsifier_db_categories", true);

            return true;
        } catch (com.cloudant.client.org.lightcouch.CouchDbException ex) {
            System.err.println("Error retrieving server response");
            return false;
        }
    }
}

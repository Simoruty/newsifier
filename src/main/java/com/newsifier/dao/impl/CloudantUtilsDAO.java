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

    // Credential Cloudant
    private static final String USERNAME_DB = "0eb7cef6-2fbb-45c5-8e21-ca56349b4870-bluemix";
    private static final String PASSWORD_DB = "c8e8913e64a247ae74fa0e90339335a27ed63044331d16cefdbb1138ea5739f7";
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

    /*
    public static void closeConnectionClient() {
        cloudantClient.shutdown();
    }
    */

    protected static boolean createConnectionWithDBMaster() {

        try {
            // Create a new CloudantClient instance
            cloudantClient = getConnection();

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
            cloudantClient = getConnection();

            // Create a new database.
            dbCategories = cloudantClient.database("newsifier_db_categories", true);

            return true;
        } catch (com.cloudant.client.org.lightcouch.CouchDbException ex) {
            System.err.println("Error retrieving server response");
            return false;
        }
    }
}

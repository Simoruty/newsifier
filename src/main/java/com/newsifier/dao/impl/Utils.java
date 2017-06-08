package com.newsifier.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Utils {

    /**
     * For deployment
     *
     * @return credential JsonObject
     */

    public static JsonObject getCredentials(String serviceName, String usr, String pwd) {
        //for local deployment
        if (System.getenv("VCAP_SERVICES") == null || System.getenv("VCAP_SERVICES").isEmpty()) {
            return readProperties(usr, pwd);
        }

        //for bluemix deployment
        else {
            JsonParser parser = new JsonParser();
            JsonObject allServices = parser.parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
            return ((JsonObject) allServices.getAsJsonArray(serviceName).get(0)).getAsJsonObject("credentials");
        }

    }


    /**
     * For local deployment
     *
     * @return credential JsonObject
     */
    private static JsonObject readProperties(String username, String pwd) {

        JsonObject credentialsJson = new JsonObject();

        // set the JSONOBject with the username and password
        credentialsJson.addProperty("username", username);
        credentialsJson.addProperty("password", pwd);

        return credentialsJson;
    }
}

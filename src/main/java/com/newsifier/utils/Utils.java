package com.newsifier.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Utils {

    public static JsonObject getCredentials(String serviceName, String usr, String pwd) {
        //for local deployment
        if (System.getenv("VCAP_SERVICES") == null || System.getenv("VCAP_SERVICES").isEmpty()) {
            return readProperties(usr, pwd);
        }

        //On Bluemix
        else {
            JsonParser parser = new JsonParser();
            JsonObject allServices = parser.parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
            return ((JsonObject) allServices.getAsJsonArray(serviceName).get(0)).getAsJsonObject("credentials");
        }

    }


    public static JsonObject getCredentials(String serviceName, String usrId, String usrName, String pwd, String domainId, String domainName, String projectId, String projectName, String authUrl, String region, String role) {
        //for local deployment
        if (System.getenv("VCAP_SERVICES") == null || System.getenv("VCAP_SERVICES").isEmpty()) {
            return readProperties(usrId, usrName, pwd, domainId, domainName, projectId, projectName, authUrl, region, role);
        }

        //for bluemix deployment
        else {
            JsonParser parser = new JsonParser();
            JsonObject allServices = parser.parse(System.getenv("VCAP_SERVICES")).getAsJsonObject();
            return ((JsonObject) allServices.getAsJsonArray(serviceName).get(0)).getAsJsonObject("credentials");
        }

    }

    private static JsonObject readProperties(String username, String pwd) {

        JsonObject credentialsJson = new JsonObject();

        // set the JSONOBject with the username and password
        credentialsJson.addProperty("username", username);
        credentialsJson.addProperty("password", pwd);

        return credentialsJson;
    }

    private static JsonObject readProperties(String usrId, String usrName, String pwd, String domainId, String domainName, String projectId, String projectName, String authUrl, String region, String role) {
        JsonObject credentialsJson = new JsonObject();

        // set the JSONOBject with the username and password
        credentialsJson.addProperty("userId", usrId);
        credentialsJson.addProperty("username", usrName);
        credentialsJson.addProperty("password", pwd);
        credentialsJson.addProperty("auth_url", authUrl);
        credentialsJson.addProperty("domainName", domainName);
        credentialsJson.addProperty("domainId", domainId);
        credentialsJson.addProperty("projectId", projectId);
        credentialsJson.addProperty("project", projectName);
        credentialsJson.addProperty("region", region);
        credentialsJson.addProperty("role", role);

        return credentialsJson;
    }
}

package com.newsifier.dao.impl;

import com.google.gson.JsonObject;
import com.newsifier.dao.interfaces.DatasetDAO;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.storage.ObjectStorageService;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.openstack.OSFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class ObjectStorageDatasetDAO implements DatasetDAO {

    //Get these credentials from Bluemix by going to your Object Storage service, and clicking on Service Credentials:
    private static final String USERIDOBJ = "7627741b299f4541afbc3e18ee62e498";
    private static final String USERNAMEOBJ = "admin_2d7a1f0c87bc98e2c6a7935a9e0227ed94e2a83c";
    private static final String PASSWORDOBJ = "vSSlx(.u3et/SA5b";
    private static final String DOMAIN_ID = "26987fab99a847c89125147af010e28d";
    private static final String DOMAIN_NAME = "795153";
    private static final String PROJECT_ID = "36d1d6cdd212453a9de944badb87f22f";
    private static final String PROJECT = "object_storage_cd39e834_ad71_41a9_b117_84e9d0ea9801";
    private static final String AUTH_URL = "https://lon-identity.open.softlayer.com";
    private static final String REGION = "london";
    private static final String ROLE = "admin";

    private ObjectStorageService authenticateAndGetObjectStorageService() {

        JsonObject credentials = getCredentials("Object-Storage", USERIDOBJ, USERNAMEOBJ, PASSWORDOBJ, DOMAIN_ID, DOMAIN_NAME, PROJECT_ID, PROJECT, AUTH_URL, REGION, ROLE);

        String userId = credentials.get("userId").getAsString();
        String userName = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();
        String auth_url = credentials.get("auth_url").getAsString().concat("/v3");
        String domainName = credentials.get("domainName").getAsString();
        String project = credentials.get("project").getAsString();
        String projectId = credentials.get("projectId").getAsString();
        String domainId = credentials.get("domainId").getAsString();
        String region = credentials.get("region").getAsString();

        System.out.println("Authenticating...");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(auth_url)
                .credentials(userName, password, Identifier.byName(domainName))
                .scopeToProject(Identifier.byId(projectId))
                .authenticate()
                .useRegion(region);

        System.out.println("Authenticated successfully!");
        return os.objectStorage();
    }

    @Override
    public String getDataset(String containerName, String fileName) {


        ObjectStorageService objectStorage = authenticateAndGetObjectStorageService();

        System.out.println("Retrieving file from Object Storage...");


        SwiftObject fileObj = objectStorage.objects().get(containerName, fileName);

        if (fileObj == null) { //The specified file was not found
            System.out.println("File not found.");
            return null;
        }

        DLPayload payload = fileObj.download();

        InputStream in = payload.getInputStream();
        String outp = getStringFromInputStream(in);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully retrieved file from Object Storage!");

        return outp;
    }


    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    @Override
    public void saveDataset(String datasetStr, String containerName, String fileName) {

        ObjectStorageService objectStorage = authenticateAndGetObjectStorageService();

        System.out.println("Storing file in Object Storage...");

        if (containerName == null || fileName == null) {
            //No file was specified to be found, or container name is missing
            System.out.println("File not found.");
            return;
        }

        InputStream streamIn = new ByteArrayInputStream(datasetStr.getBytes(StandardCharsets.UTF_8));

        Payload<InputStream> payload = new PayloadClass(streamIn);

        objectStorage.objects().put(containerName, fileName, payload);

        System.out.println("Stored successfully!");

    }


    private class PayloadClass implements Payload<InputStream> {
        private InputStream stream = null;

        public PayloadClass(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public void close() throws IOException {
            stream.close();
        }

        @Override
        public InputStream open() {
            return stream;
        }

        @Override
        public void closeQuietly() {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public InputStream getRaw() {
            return stream;
        }

    }

}

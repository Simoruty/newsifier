package com.newsifier.dao.impl;

import com.google.gson.JsonObject;
import com.newsifier.dao.interfaces.DatasetDAO;
import com.newsifier.utils.Credentials;
import com.newsifier.utils.Logger;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.storage.ObjectStorageService;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.storage.object.SwiftObject;
import org.openstack4j.openstack.OSFactory;

import static com.newsifier.utils.Utils.getCredentials;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ObjectStorageDatasetDAO implements DatasetDAO {

    private static ObjectStorageService objectStorage;

    public ObjectStorageDatasetDAO() {
        objectStorage = authenticateAndGetObjectStorageService();
    }

    private ObjectStorageService authenticateAndGetObjectStorageService() {

        JsonObject credentials = getCredentials("Object-Storage", Credentials.getUseridObj(), Credentials.getUsernameObj(), Credentials.getPasswordObj(), Credentials.getDomainIdObj(), Credentials.getDomainNameObj(), Credentials.getProjectIdObj(), Credentials.getProjectObj(), Credentials.getAuthUrlObj(), Credentials.getRegionObj(), Credentials.getRoleObj());

        String userId = credentials.get("userId").getAsString();
        String userName = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();
        String auth_url = credentials.get("auth_url").getAsString().concat("/v3");
        String domainName = credentials.get("domainName").getAsString();
        String project = credentials.get("project").getAsString();
        String projectId = credentials.get("projectId").getAsString();
        String domainId = credentials.get("domainId").getAsString();
        String region = credentials.get("region").getAsString();

        Logger.log("Object Storage Authenticating...");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(auth_url)
                .credentials(userName, password, Identifier.byName(domainName))
                .scopeToProject(Identifier.byId(projectId))
                .authenticate()
                .useRegion(region);

        Logger.log("Object Storage Authenticated successfully!");
        Logger.webLog("Authentication with Object Storage successfully");

        return os.objectStorage();
    }

    @Override
    public String getDataset(String containerName, String fileName) {

        Logger.log("Retrieving file from Object Storage...");

        SwiftObject fileObj = objectStorage.objects().get(containerName, fileName);

        if (fileObj == null) { //The specified file was not found
            Logger.log(fileName + " not found.");
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

        Logger.log(fileName + " successfully retrieved from Object Storage!");
        Logger.webLog(fileName + " successfully retrieved from Object Storage");

        return outp;
    }

    @Override
    public File getDatasetFile(String containerName, String fileName) {

        Logger.log("Retrieving file from Object Storage...");

        SwiftObject fileObj = objectStorage.objects().get(containerName, fileName);

        if (fileObj == null) { //The specified file was not found
            Logger.log(fileName + " not found.");
            return null;
        }

        DLPayload payload = fileObj.download();

        File file = new File(fileName);
        try {
            payload.writeToFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
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

        Logger.log("Storing file in Object Storage...");

        if (containerName == null || fileName == null) {
            //No file was specified to be found, or container name is missing
            Logger.log(fileName + " not found.");
            return;
        }

        InputStream streamIn = new ByteArrayInputStream(datasetStr.getBytes(StandardCharsets.UTF_8));

        Payload<InputStream> payload = new PayloadClass(streamIn);

        if (objectStorage.containers().create(containerName).isSuccess()) {
            objectStorage.objects().put(containerName, fileName, payload);
            Logger.log(fileName + " successfully stored");
            Logger.webLog(fileName + " successfully stored");
        } else {
            Logger.logErr(containerName + " is not created");
        }
    }


    @Override
    public void eraseDataset(String containerName) {

        objectStorage.containers().delete(containerName);
        Logger.webLog("All files into Object storage deleted");
        Logger.log("All files into Object storage deleted");
    }

    @Override
    public void eraseDataset(String containerName, String fileName) {

        objectStorage.objects().delete(containerName,fileName);
        Logger.webLog(fileName + " erased from Object storage");
        Logger.log(fileName + " erased from Object storage");
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

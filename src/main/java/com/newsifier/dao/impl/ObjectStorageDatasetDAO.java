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

import java.io.IOException;
import java.io.InputStream;

import static com.newsifier.dao.impl.Utils.getCredentials;

public class ObjectStorageDatasetDAO implements DatasetDAO {

    //Get these credentials from Bluemix by going to your Object Storage service, and clicking on Service Credentials:
    private static final String USERIDOBJ = "6b6e4edaa8614beb8445aa761b25c22a";
    private static final String PASSWORDOBJ = "co6?~EcjR~P.G3cV";
    private static final String DOMAIN_ID = "26987fab99a847c89125147af010e28d";
    private static final String DOMAIN_NAME = "795153";
    private static final String PROJECT_ID = "36d1d6cdd212453a9de944badb87f22f";
    private static final String PROJECT_NAME = "object_storage_cd39e834_ad71_41a9_b117_84e9d0ea9801";
    private static final String AUTH_URL = "https://lon-identity.open.softlayer.com";


    private ObjectStorageService authenticateAndGetObjectStorageService() {

        JsonObject credentials = getCredentials("Object-Storage", USERIDOBJ, PASSWORDOBJ, DOMAIN_ID, DOMAIN_NAME, PROJECT_ID, PROJECT_NAME, AUTH_URL);

        String userId = credentials.get("userId").getAsString();
        String password = credentials.get("password").getAsString();
        String auth_url = credentials.get("auth_url").getAsString().concat("/v3");
        String domain = credentials.get("domainName").getAsString();
        String project = credentials.get("project").getAsString();

        Identifier domainIdent = Identifier.byName(domain);
        Identifier projectIdent = Identifier.byName(project);

        System.out.println("Authenticating...");

        OSClient.OSClientV3 os = OSFactory.builderV3()
                .endpoint(auth_url)
                .credentials(userId, password)
                .scopeToProject(projectIdent, domainIdent)
                .authenticate();

        System.out.println("Authenticated successfully!");

        return os.objectStorage();
    }

    @Override
    public Object getDataset(String containerName, String fileName) {


        ObjectStorageService objectStorage = authenticateAndGetObjectStorageService();

        System.out.println("Retrieving file from Object Storage...");


        SwiftObject fileObj = objectStorage.objects().get(containerName, fileName);

        if (fileObj == null) { //The specified file was not found
            System.out.println("File not found.");
            return null;
        }

        DLPayload payload = fileObj.download();

        InputStream in = payload.getInputStream();
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

            /*
            OutputStream out = response.getOutputStream();

            IOUtils.copy(in, out);
            in.close();
            out.close();
*/
        System.out.println("Successfully retrieved file from Object Storage!");


        return null;
    }

    @Override
    public void saveDataset(InputStream in, String containerName, String fileName) {

        ObjectStorageService objectStorage = authenticateAndGetObjectStorageService();

        System.out.println("Storing file in Object Storage...");

        if (containerName == null || fileName == null) {
            //No file was specified to be found, or container name is missing
            System.out.println("File not found.");
            return;
        }

        Payload<InputStream> payload = new PayloadClass(in);

        objectStorage.objects().put(containerName, fileName, payload);


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

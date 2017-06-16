package com.newsifier;

public class Credentials {

    // natural-language-classifier Credential

    private static final String USERNAME_NLC = "337aa53b-22de-41f4-910e-d9579ca2e8ab";
    private static final String PASSWORD_NLC = "iEuycPODP1WS";

    private static final String CLASSIFIERNAME = "NLCClassifier";

    public static String getClassifierName() {
        return CLASSIFIERNAME;
    }

    public static String getUsernameNlc() {
        return USERNAME_NLC;
    }

    public static String getPasswordNlc() {
        return PASSWORD_NLC;
    }


    // natural-language-understanding Credential

    private static final String USERNAME_NLU = "824c475f-f2b4-4282-aa65-3500d113edd9";
    private static final String PASSWORD_NLU = "HC1kWyLeSr3i";

    public static String getUsernameNlu() {
        return USERNAME_NLU;
    }

    public static String getPasswordNlu() {
        return PASSWORD_NLU;
    }

    // Credential Cloudant
    private static final String USERNAME_DB_CLOUDANT = "5e265712-6c53-481c-8cc6-d9740b8717bc-bluemix";
    private static final String PASSWORD_DB_CLOUDANT = "96254734f60ef7900917a11c7a2b899091f8e3b28f9468321196d7dcf0627431";

    public static String getUsernameDbCloudant() {
        return USERNAME_DB_CLOUDANT;
    }

    public static String getPasswordDbCloudant() {
        return PASSWORD_DB_CLOUDANT;
    }

    //Credentials Object Storage service
    private static final String USERID_OBJ = "ee984cbda6854a6c913992399319f1f4";
    private static final String USERNAME_OBJ = "admin_b1320375b5f42ddd89d4f45248cd9bfcce1d07c2";
    private static final String PASSWORD_OBJ = "at8HgJO]kj&o8ig(";
    private static final String DOMAIN_ID_OBJ = "5a5263e2f4554cf395e9d2077ab851c4";
    private static final String DOMAIN_NAME_OBJ = "786039";
    private static final String PROJECT_ID_OBJ = "a2c3b32faa5143e09f848413d2251240";
    private static final String PROJECT_OBJ = "object_storage_cd733896_3e33_4ec6_a495_58908ad3f365";
    private static final String AUTH_URL_OBJ = "https://identity.open.softlayer.com";
    private static final String REGION_OBJ = "dallas";
    private static final String ROLE_OBJ = "admin";


    private static final String CONTAINERNAMEOBJ = "filesDataset";
    private static final String DATASETNAMEOBJ = "dataset.csv";
    private static final String TRAININGSETNAMEOBJ = "trainingSet.csv";

    private static final String TESTSETNAMEOBJ = "testSet.csv";

    public static String getTestsetnameObj() {
        return TESTSETNAMEOBJ;
    }

    public static String getTrainingsetnameObj() {
        return TRAININGSETNAMEOBJ;
    }

    public static String getDatasetnameObj() {
        return DATASETNAMEOBJ;
    }

    public static String getContainernameObj() {
        return CONTAINERNAMEOBJ;
    }

    public static String getUseridObj() {
        return USERID_OBJ;
    }

    public static String getUsernameObj() {
        return USERNAME_OBJ;
    }

    public static String getPasswordObj() {
        return PASSWORD_OBJ;
    }

    public static String getDomainIdObj() {
        return DOMAIN_ID_OBJ;
    }

    public static String getDomainNameObj() {
        return DOMAIN_NAME_OBJ;
    }

    public static String getProjectIdObj() {
        return PROJECT_ID_OBJ;
    }

    public static String getProjectObj() {
        return PROJECT_OBJ;
    }

    public static String getAuthUrlObj() {
        return AUTH_URL_OBJ;
    }

    public static String getRegionObj() {
        return REGION_OBJ;
    }

    public static String getRoleObj() {
        return ROLE_OBJ;
    }
}

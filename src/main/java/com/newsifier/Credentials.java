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
    private static final String AUTH_URL_OBJ = "";
    private static final String PROJECT_OBJ = "";
    private static final String PROJECT_ID_OBJ = "";
    private static final String REGION_OBJ = "";
    private static final String USERID_OBJ = "";
    private static final String USERNAME_OBJ = "";
    private static final String PASSWORD_OBJ = "";
    private static final String DOMAIN_ID_OBJ = "";
    private static final String DOMAIN_NAME_OBJ = "";
    private static final String ROLE_OBJ = "";

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

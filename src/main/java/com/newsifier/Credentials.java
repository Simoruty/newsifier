package com.newsifier;

public class Credentials {

    // natural-language-classifier Credential

    private static final String USERNAME_NLC = "";
    private static final String PASSWORD_NLC = "";

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

    private static final String USERNAME_NLU = "";
    private static final String PASSWORD_NLU = "";

    public static String getUsernameNlu() {
        return USERNAME_NLU;
    }

    public static String getPasswordNlu() {
        return PASSWORD_NLU;
    }

    // Credential Cloudant
    private static final String USERNAME_DB_CLOUDANT = "";
    private static final String PASSWORD_DB_CLOUDANT = "";

    public static String getUsernameDbCloudant() {
        return USERNAME_DB_CLOUDANT;
    }

    public static String getPasswordDbCloudant() {
        return PASSWORD_DB_CLOUDANT;
    }

    //Credentials Object Storage service
    private static final String USERID_OBJ = "";
    private static final String USERNAME_OBJ = "";
    private static final String PASSWORD_OBJ = "";
    private static final String DOMAIN_ID_OBJ = "";
    private static final String DOMAIN_NAME_OBJ = "";
    private static final String PROJECT_ID_OBJ = "";
    private static final String PROJECT_OBJ = "";
    private static final String AUTH_URL_OBJ = "";
    private static final String REGION_OBJ = "";
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

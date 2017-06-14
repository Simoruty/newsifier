package com.newsifier;

public class Credentials {

    // natural-language-classifier Credential

    private static final String USERNAME_NLC = "5636ecdf-617a-4572-a478-4e3dfabb13ae";
    private static final String PASSWORD_NLC = "ZYzJrmbOraWg";

    private static final String CLASSIFIERNAME = "NLCClassifier";

    public static String getClassifiername() {
        return CLASSIFIERNAME;
    }

    public static String getUsernameNlc() {
        return USERNAME_NLC;
    }

    public static String getPasswordNlc() {
        return PASSWORD_NLC;
    }


    // natural-language-understanding Credential

    private static final String USERNAME_NLU = "32040e43-3d63-49b5-b983-134bdef730bb";
    private static final String PASSWORD_NLU = "Qm88hpJVDq0x";

    public static String getUsernameNlu() {
        return USERNAME_NLU;
    }

    public static String getPasswordNlu() {
        return PASSWORD_NLU;
    }

    // Credential Cloudant
    private static final String USERNAME_DB_CLOUDANT = "0eb7cef6-2fbb-45c5-8e21-ca56349b4870-bluemix";
    private static final String PASSWORD_DB_CLOUDANT = "c8e8913e64a247ae74fa0e90339335a27ed63044331d16cefdbb1138ea5739f7";

    public static String getUsernameDbCloudant() {
        return USERNAME_DB_CLOUDANT;
    }

    public static String getPasswordDbCloudant() {
        return PASSWORD_DB_CLOUDANT;
    }

    //Credentials Object Storage service
    private static final String USERID_OBJ = "373deb47a70e413d865cd14f9b23ee33";
    private static final String USERNAME_OBJ = "admin_e783b9740bd987dd1a50d5661c49283b91e84ef4";
    private static final String PASSWORD_OBJ = "H-Z^Sb7G)1cIrX19";
    private static final String DOMAIN_ID_OBJ = "26987fab99a847c89125147af010e28d";
    private static final String DOMAIN_NAME_OBJ = "795153";
    private static final String PROJECT_ID_OBJ = "9da5b76d701a4bdba5d65b15e4495022";
    private static final String PROJECT_OBJ = "object_storage_edc69694_aff8_4bd0_acbd_6d618ca7aeeb";
    private static final String AUTH_URL_OBJ = "https://lon-identity.open.softlayer.com";
    private static final String REGION_OBJ = "london";
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

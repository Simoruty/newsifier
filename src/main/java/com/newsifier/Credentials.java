package com.newsifier;

public class Credentials {

    // natural-language-classifier Credential

    private static final String USERNAME_NLC = "54755500-2b59-4aac-86a8-d15fb58a56fc";
    private static final String PASSWORD_NLC = "N1dTkoikwNkq";

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
    private static final String USERID_OBJ = "7627741b299f4541afbc3e18ee62e498";
    private static final String USERNAME_OBJ = "admin_2d7a1f0c87bc98e2c6a7935a9e0227ed94e2a83c";
    private static final String PASSWORD_OBJ = "vSSlx(.u3et/SA5b";
    private static final String DOMAIN_ID_OBJ = "26987fab99a847c89125147af010e28d";
    private static final String DOMAIN_NAME_OBJ = "795153";
    private static final String PROJECT_ID_OBJ = "36d1d6cdd212453a9de944badb87f22f";
    private static final String PROJECT_OBJ = "object_storage_cd39e834_ad71_41a9_b117_84e9d0ea9801";
    private static final String AUTH_URL_OBJ = "https://lon-identity.open.softlayer.com";
    private static final String REGION_OBJ = "london";
    private static final String ROLE_OBJ = "admin";


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

package com.amdocs.oss.ldap;
import javax.naming.directory.DirContext;

public interface LDAPConnection {
    /**
     * Base URL (AD)
     */
    public static final String LDAP_PROVIDER_URL = "ldap://raadc1.corp.amdocs.com:389";
    /**
     * Basic authentication (Simple)
     */
    public static final String LDAP_SECURITY_AUTHENTICATION_SIMPLE = "simple";
    /**
     * Basic authentication (None)
     */
    public static final String LDAP_SECURITY_AUTHENTICATION_NONE = "none";
    /**
     * Basic authentication security
     */
    public static final String LDAP_SECURITY_PRINCIPAL = "ntnet\\";
    /**
     * Base DN
     */
    public static String LDAP_BASE_DN ="DC=corp,DC=amdocs,DC=com";

    /**
     * Quey user
     */
    public static String LDAP_QUERY_USER = "(&(objectClass=user)(sAMAccountName=%s))";

    /**
     * Group Query
     */
    public static String LDAP_QUERY_USER_GROUP = "(&(objectClass=group)(sAMAccountName=%s))";




    /**
     * Basic authentication connection
     * @param user
     * @param passwor
     */
    public DirContext connect(String user,String passwor,String domain);
    /**
     * Basic authentication principal
     * @param user
     * @param domain
     * @return
     */
    public String SecurtiyPrincipal(String user,String domain);



}

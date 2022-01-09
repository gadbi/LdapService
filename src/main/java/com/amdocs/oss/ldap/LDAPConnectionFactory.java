package com.amdocs.oss.ldap;

import com.amdocs.oss.json.JsonObject;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class LDAPConnectionFactory implements LDAPConnection {

    String message;
    int code;
    public LDAPConnectionFactory() {

    }

    public String SecurtiyPrincipal(String user, String domain) {

        if (domain == null) {
            //return LDAP_SECURITY_PRINCIPAL + user;
            return LdapMain.application.getLdapSecurityPrincipal() + user;
        } else {

            return domain + "\\" + user;
        }

    }

    public DirContext connect(String user, String password, String domain) {


        DirContext ctx = null;
        Hashtable env = new Hashtable();
        System.out.println(LdapMain.application.getLdapProviderURL());
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
       // env.put(Context.PROVIDER_URL,"ldap://" + LdapMain.application.getLdapProviderURL());
        env.put(Context.PROVIDER_URL,"ldap://10.232.217.1:389");
        env.put(Context.SECURITY_AUTHENTICATION,
                LDAP_SECURITY_AUTHENTICATION_SIMPLE);
        env.put(Context.SECURITY_PRINCIPAL, SecurtiyPrincipal(user, domain));
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.REFERRAL,"follow");

        try {

            ctx = new InitialDirContext(env);
            String uenv = ctx.getEnvironment().toString();
            return ctx;

        } catch (AuthenticationNotSupportedException ex) {
            message = " The authentication is not supported by the server " + ex.getMessage();
            System.out.println(message);
            System.out.println(ex);
        } catch (AuthenticationException ex) {
            message = " incorrect password or username " + ex.getMessage();
            System.out.println(message);
            System.out.println(ex);
        } catch (NamingException ex) {

            message = " error when trying to create the context " + ex.getMessage();
            System.out.println(message);
            System.out.println(ex);

        }
        return ctx;
    }
    /**
     *
     * @param ctx
     * @param userName
     * @return
     * @throws NamingException
     */
    public String getUserFullName(DirContext ctx,String userName)
    {
        NamingEnumeration<SearchResult> enumResult = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String[] attrIDs ={"cn","ou","uid", "givenname", "sn", "mail"};
        controls.setReturningAttributes(attrIDs);
        try {
            enumResult =  ctx.search(LdapMain.application.getLdapBaseDn(),
                    String.format(LDAP_QUERY_USER,userName),
                    controls);

        } catch (NamingException e1) {
            System.out.println(e1.getMessage());
        }
        if(enumResult != null)
        {
            try {
                if (enumResult.hasMore()) {
                    Attributes attrs = ((SearchResult) enumResult.next()).getAttributes();
                    String lname =  attrs.get("givenname").toString();
                    String fname =  attrs.get("sn").toString();
                    return lname.split(":")[1] + fname.split(":")[1];
                }else{
                    try {
                        return userName;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (NamingException e) {
                System.out.println(e.getMessage());
            }

        }

        return userName;


    }

    public JsonObject getResulatLdapQuery(DirContext ctx,Query query)
    {
        JsonObject json = new JsonObject();
        NamingEnumeration<SearchResult> enumResult = null;
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        if(query.getReturnAttributes() == null){
            controls.setReturningAttributes(null);
        }else{
            String [] attrIDs = query.getReturnAttributes();
            controls.setReturningAttributes(attrIDs);
        }


        try {
            enumResult =  ctx.search(LdapMain.application.getLdapBaseDn(),
                    query.getQuery(),
                    controls);

        } catch (NamingException e1) {

        }
        if(enumResult != null)
        {
            try {
                if (enumResult.hasMore()) {
                    Attributes attrs = ((SearchResult) enumResult.next()).getAttributes();
                    if(query.getReturnAttributes() != null && query.getReturnAttributes().length > 0) {
                        for (String attr : query.getReturnAttributes()) {
                            if(attrs.get(attr) != null) {
                                json.put(attr, attrs.get(attr).toString());
                            }else{
                                json.put(attr, attr+ "=");
                            }
                        }
                    }else{
                        NamingEnumeration<String> names =  attrs.getIDs();
                        while(names.hasMore())
                        {
                            String id = names.next();
                            json.put(id, attrs.get(id).toString());
                        }
                    }
                }else{
                    try {
                        return json;
                    } catch (Exception e) {

                    }
                }
            } catch (NamingException e) {

            }

        }

        return json;
    }


    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}

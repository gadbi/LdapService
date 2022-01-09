package com.amdocs.oss.ldap;

import com.amdocs.oss.annotation.GET;
import com.amdocs.oss.annotation.MethodParameters;
import com.amdocs.oss.annotation.POST;
import com.amdocs.oss.annotation.Path;
import com.amdocs.oss.common.Resualt;
import com.amdocs.oss.json.JsonObject;
import com.amdocs.oss.json.ObjectMapper;

import javax.annotation.PostConstruct;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

public class Ldap {


    @Path(path = "/login")
    @POST
    public Resualt login(@MethodParameters String jsonAsString) throws InstantiationException, IllegalAccessException, NamingException, ClassNotFoundException {
        Resualt resualt = new Resualt();
        ObjectMapper objectMapper = new ObjectMapper();
        User user =  objectMapper.readValue(jsonAsString,User.class);
        LDAPConnectionFactory ldap = new LDAPConnectionFactory();
        DirContext ctx = ldap.connect(user.getUser(), user.getPassword(), null);

        if(ctx != null){
            JsonObject json = new JsonObject();
            String userfullname = ldap.getUserFullName(ctx, user.getUser());
            json.put("userfullname",userfullname);
            resualt.setJson(json);
            ctx.close();


        }else {
            resualt.setSuccess(false);
            resualt.setCode(500);
            resualt.setMessage(ldap.getMessage());
        }

         return resualt;
    }

    @Path(path = "/query")
    @POST
    public Resualt getResualtQuery(@MethodParameters String jsonAsString) throws IllegalAccessException, ClassNotFoundException, InstantiationException, NamingException {
        Resualt resualt = new Resualt();
        ObjectMapper objectMapper = new ObjectMapper();
        Query query =  objectMapper.readValue(jsonAsString,Query.class);
        LDAPConnectionFactory ldap = new LDAPConnectionFactory();
        DirContext ctx = ldap.connect(query.getUser(), query.getPassword(), null);
        if(ctx != null){
            JsonObject json = ldap.getResulatLdapQuery(ctx,query);
            resualt.setJson(json);
            ctx.close();

        }else {
            resualt.setSuccess(false);
            resualt.setCode(500);
            resualt.setMessage(ldap.getMessage());
        }

        return resualt;
    }

}

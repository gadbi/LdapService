package com.amdocs.oss.ldap;

import com.amdocs.oss.annotation.Setter;
import com.amdocs.oss.common.Box;
import com.amdocs.oss.common.Pair;
import com.amdocs.oss.json.ObjectMapper;

import java.util.*;

public class Query {

    String [] returnAttributes  = null;
    String query;
    String user;
    String password;
    HashMap<?,?> parameters  = null;


    public String[] getReturnAttributes() {
        return returnAttributes;
    }
    @Setter(path = "attributes")
    public void setReturnAttributes(String [] returnAttributes) {
        this.returnAttributes = returnAttributes;
    }

    public String getQuery() {

        if(parameters != null) {
            for (Map.Entry<?, ?> item : parameters.entrySet()) {
                query = query.replace(String.format("${%s}", item.getKey().toString().replaceAll("([\b \t \n \f \r \\\" \\' \\\\])","")), item.getValue().toString().replaceAll("([\b \t \n \f \r \\\" \\' \\\\])",""));
            }
        }
        return query.replaceAll("([\b \t \n \f \r \\\" \\' \\\\])","");
    }

    @Setter(path = "query")
    public void setQuery(String query) {
        this.query = query;
    }

    public String getUser() {
        return user;
    }
    @Setter(path = "user")
    public void setUser(String user) {
        this.user = user.replaceAll("([\b \t \n \f \r \\\" \\' \\\\])","");;
    }

    public String getPassword() {
        return password;
    }

    @Setter(path = "password")
    public void setPassword(String password) {
        this.password = password.replaceAll("([\b \t \n \f \r \\\" \\' \\\\])","");;
    }


    public HashMap<?,?> getParameters() {
        return parameters;
    }

    @Setter(path = "parameters")
    public void setParameters(HashMap<?,?> parameters) {
        this.parameters = parameters;
    }
}

package com.amdocs.oss.ldap;

import com.amdocs.oss.annotation.Setter;

public class User {

    private String user;
    private String password;

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
}

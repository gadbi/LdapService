package com.amdocs.oss.common;

import java.io.InputStream;
import java.util.Properties;

public class Application implements Configuration{


    Properties prop = null;
    InputStream inputStream = null;
    public Application()
    {
        if(prop == null)
        {
          prop = new Properties();
        }
        if(inputStream == null){

            try {
                inputStream = getConfigFilePath();
                prop.load(inputStream);
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }
    }

    private InputStream getConfigFilePath(){
           //System.out.println(new File("").getAbsolutePath());
       return this.getClass().getClassLoader().getResourceAsStream(APPLICATION_FILE_PROPERTIES);

    }
    public int getPort(){
        return Integer.parseInt(prop.getProperty(APPLICATION_PORT));
    }
    public String getLdapProviderURL()
    {
        return prop.getProperty(LDAP_PROVIDER_URL);
    }
    public String getLdapSecurityPrincipal()
    {
        return prop.getProperty(LDAP_SECURITY_PRINCIPAL);
    }
    public String getLdapBaseDn()
    {
        return prop.getProperty(LDAP_BASE_DN);
    }

}

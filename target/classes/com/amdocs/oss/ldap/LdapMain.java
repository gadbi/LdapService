package com.amdocs.oss.ldap;

import com.amdocs.oss.annotation.POST;
import com.amdocs.oss.annotation.Path;
import com.amdocs.oss.common.Application;
import com.amdocs.oss.common.Resualt;
import com.amdocs.oss.json.JsonObject;
import com.amdocs.oss.service.EService;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;

public class LdapMain {

    public static Application application;
    public static HttpServer server;
    public static void main(String[] args) throws Exception {
        application = new Application();
        server = HttpServer.create(new InetSocketAddress(application.getPort()), 0);
        System.out.println("Application ON, Port : " +  application.getPort()  + " registered ");
        HttpContext context = server.createContext("/");
        context.setHandler(LdapMain::handleRequest);
        server.start();
    }

    /**
     *
     * @param exchange
     * @throws IOException
     */
    private static void handleRequest(HttpExchange exchange) throws IOException {
        EService eService = new EService();
        URI requestURI = exchange.getRequestURI();
        String method  = exchange.getRequestMethod();
        if(requestURI.getPath().toLowerCase().startsWith("/stop")){
            Resualt resualt = new Resualt("Success : Application OFF, Port : " +  application.getPort()  + " unregistered ",200);
            eService.handleResponeMessage(exchange,resualt);
            shotdown();
            return;
        }
        eService.execute(exchange);


    }


    /**
     * Shotdown Service
     */
    private static void shotdown()
    {
        System.out.println("Application OFF, Port : " +  application.getPort()  + " unregistered ");
        server.stop(0);
    }


}

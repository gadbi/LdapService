package com.amdocs.oss.service;

import com.amdocs.oss.annotation.POST;
import com.amdocs.oss.annotation.Path;
import com.amdocs.oss.common.Resualt;
import com.amdocs.oss.ldap.Ldap;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class EService implements IService {

    @Override
    public CompletableFuture<Void> execute(HttpExchange exchange) {
        return CompletableFuture.runAsync(() -> handleRequest(exchange),getExecutorService());
    }


    private void handleRequest(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        if(method.equalsIgnoreCase("POST"))
        {
            try {
                try {
                    handlePostRequest(exchange);
                } catch (IOException e) {
                    Resualt resualt = new Resualt("Failure : " + e.getCause().toString(),e.getLocalizedMessage(),false,500);
                    try {
                        handleResponeMessage(exchange,resualt);
                    } catch (IOException e1) {
                       e1.printStackTrace();
                    }
                }
            } catch (InvocationTargetException e) {
                Resualt resualt = new Resualt("Failure : " + e.getCause().toString(),e.getLocalizedMessage(),false,500);
                try {
                    handleResponeMessage(exchange,resualt);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                Resualt resualt = new Resualt("Failure : " + e.getCause().toString(),e.getLocalizedMessage(),false,500);
                try {
                    handleResponeMessage(exchange,resualt);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }else{

            try {
                Resualt resualt = new Resualt("Failure : Only POST allow ","Please check your Method and use POST only ",false,8888);
                handleResponeMessage(exchange,resualt);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void handleResponeMessage(HttpExchange exchange, Resualt resualt) throws IOException {

        String response = resualt.getResponse();
        exchange.sendResponseHeaders(resualt.getCode(), response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException, InvocationTargetException, IllegalAccessException {
        Ldap ldap = new Ldap();
        URI requestURI = exchange.getRequestURI();
        Method[] methods = Ldap.class.getDeclaredMethods();
        boolean methodFound = false;
        for(Method method :methods)
        {
            POST postMethod = method.getAnnotation(POST.class);
            Path pathMetod  = method.getAnnotation(Path.class);
            if(pathMetod.path().equalsIgnoreCase(requestURI.getPath()))
            {
                methodFound = true;
                String body = getRequestBody(exchange);
                Resualt resualt = (Resualt)method.invoke(ldap,body);
                handleResponeMessage(exchange,resualt);

            }
        }

        if(!methodFound){
            Resualt resualt = new Resualt("Failure : Method or Path not found  ","Please check your Method and use Path in your URI ",false,8889);
            handleResponeMessage(exchange,resualt);
        }

    }

    private static String getRequestBody(HttpExchange exchange) {
        try {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder();
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            br.close();
            isr.close();
            return buf.toString();

        } catch (Exception e) {
            return "";
        }


    }
}

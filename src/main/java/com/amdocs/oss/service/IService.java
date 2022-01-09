package com.amdocs.oss.service;

import com.sun.net.httpserver.HttpExchange;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface  IService {

    CompletableFuture<Void> execute(HttpExchange exchange);
    default ExecutorService getExecutorService(){
      return Executors.newFixedThreadPool(10);
    }
}

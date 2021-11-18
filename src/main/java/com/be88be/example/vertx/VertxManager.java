package com.be88be.example.vertx;



/*
 * Copyright 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */


import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class VertxManager {
    @Value("${vertx.cluster.host}")
    private String clusterHost;

    @Value("${vertx.group.name}")
    private String groupName;

    @Value("${vertx.group.pass}")
    private String groupPass;

    @Value("${vertx.worker.pool.size}")
    private int workerPoolSize;
    
    @Value("${vertx.event.loop.size}")
    private int eventLoopSize;
    
    private Vertx vertx;
    
    @PostConstruct
    private void init() throws Exception {
        CompletableFuture<Vertx> future = new CompletableFuture<>();
        Vertx.clusteredVertx(getVertxOptions()
        		, ar -> {
		            if (ar.succeeded()) {
		            	future.complete(ar.result());
		            	log.info("vertx clustered complete {}", ar.result());
		            } else {
		                future.completeExceptionally(ar.cause());
		                log.error("vertx clustered failed {}", ar.cause().getMessage());
		            }
        		});
        vertx = future.get();
        
//        vertx.exceptionHandler(error -> log.error("ERROR", error));
//        Json.mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    private VertxOptions getVertxOptions() {
        GroupConfig group = new GroupConfig()
        		.setName(groupName)
        		.setPassword(groupPass);

        VertxOptions options = new VertxOptions()
                .setClusterManager(new HazelcastClusterManager(new Config()
                		.setGroupConfig(group)))
                .setClusterHost(clusterHost)
                .setClustered(false)
                .setWorkerPoolSize(workerPoolSize)
                .setEventLoopPoolSize(eventLoopSize);
        return options; 
    }
    
    @Bean
    private Vertx vertx() {
        return vertx;
    }
    
    @PreDestroy
    private void close() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        vertx.close(ar -> future.complete(null));
        future.get();
        log.info("Shutdown vertx completed.");
    }
}
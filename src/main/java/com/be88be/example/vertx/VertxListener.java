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


import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import lombok.extern.slf4j.Slf4j;

import com.be88be.example.common.annotation.MainVerticle;
import com.be88be.example.common.annotation.WorkerVerticle;
import com.be88be.example.common.utils.SpringContextHelper;
import com.be88be.example.common.service.SpringVerticleFactory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


import java.util.Map;


@Component
@Slf4j
public class VertxListener {
    private final Vertx vertx;

    public VertxListener(Vertx vertx) {
        this.vertx = vertx;
    }

    @EventListener
    public void deployVerticles(ApplicationReadyEvent event) throws Exception {
        VerticleFactory verticleFactory = SpringContextHelper.getBean(SpringVerticleFactory.class);
        vertx.registerVerticleFactory(verticleFactory);

//        DeploymentOptions options = new DeploymentOptions().setInstances(2);

        for (Map.Entry<String, Object> entry : SpringContextHelper.getBeansWithAnnotation(MainVerticle.class).entrySet()) {
            vertx.deployVerticle(verticleFactory.prefix() + ":" + entry.getValue().getClass().getName()
                    , ar -> {
                        if (ar.succeeded()) {
                            log.info("deployment mainVerticle id is: " + ar.result());
                        } else {
                            log.error("deployment mainVerticle failed {}", ar.cause().getMessage());
                        }
                    });
        }

        for (Map.Entry<String, Object> entry : SpringContextHelper.getBeansWithAnnotation(WorkerVerticle.class).entrySet()) {
            vertx.deployVerticle((Verticle) SpringContextHelper.getBean(entry.getValue().getClass()), new DeploymentOptions().setWorker(true)
                    , ar -> {
                        if (ar.succeeded()) {
                            log.info("deployment workVerticle id is: " + ar.result());
                        } else {
                            log.error("deployment workVerticle failed {}", ar.cause().getMessage());
                        }
                    });
        }

    }
}
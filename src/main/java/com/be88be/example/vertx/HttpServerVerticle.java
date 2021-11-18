package com.be88be.example.vertx;


import org.springframework.beans.factory.annotation.Value;

import com.be88be.example.common.annotation.MainVerticle;
import com.be88be.example.common.model.enums.Api;
import com.be88be.example.common.model.enums.ResponseStatus;
import com.be88be.example.common.utils.VertxUtil;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import lombok.extern.slf4j.Slf4j;

@MainVerticle
@Slf4j
public class HttpServerVerticle extends AbstractVerticle {
    @Value("${vertx.server.port}")
    private int serverPort;
    private Router router;

    @Override
    public void start() {
        router = Router.router(vertx);
        corsHandler();
        router.route().handler(BodyHandler.create());
        failureHandler();

        for (Api api : Api.getApiValues()) {
            router.route()
                    .method(api.getHttpMethod())
                    .path(api.getValue())
                    .handler(ctx -> {
                        VertxUtil.eventBusSend(vertx, ctx);
                    });
        }

        router.getRoutes().forEach(r -> {
            log.info("mapped: [{}] {}", r.getPath(), r);
        });

        vertx.createHttpServer(new HttpServerOptions().setLogActivity(true).setTcpKeepAlive(true))
                .requestHandler(router).listen(serverPort, ar -> {
            if (ar.succeeded()) {
                log.info("http server is now listening!" + " (" + serverPort + ")");
            } else {
                log.error("http server listening failed {}", ar.cause().getMessage());
            }
        });

//        Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    private void failureHandler() {
        router.route().last().handler(c -> c.fail(ResponseStatus.HTTP_NOT_FOUND.getCode()))
                .failureHandler(ctx -> {
                    int errorCode = ctx.statusCode() > 0 ? ctx.statusCode() : ResponseStatus.HTTP_SERVER_ERROR.getCode();

                    if (ResponseStatus.HTTP_NOT_FOUND.getCode() == errorCode) {
                        VertxUtil.errorResponse(ctx, errorCode, ResponseStatus.HTTP_NOT_FOUND.getMessage());
                        log.error("route failed : " + ctx.request().path() + " : {}", ResponseStatus.HTTP_NOT_FOUND.getMessage());
                    }

                    if (ctx.failed() && ctx.failure() instanceof Exception) {
                        VertxUtil.errorResponse(ctx, errorCode, ResponseStatus.HTTP_SERVER_ERROR.getMessage());
                        log.error("route failed : {}", ctx.failure().getMessage());
                    }
                });
    }

    private void corsHandler() {
        router.route().handler(
                CorsHandler.create("*")
                        .allowedMethod(HttpMethod.GET)
                        .allowedMethod(HttpMethod.POST)
                        .allowedHeader("*")
                        .allowedHeader("Access-Control-Request-Method")
                        .allowedHeader("Access-Control-Allow-Credentials")
                        .allowedHeader("Access-Control-Allow-Origin")
                        .allowedHeader("Access-Control-Allow-Headers")
                        .allowedHeader("Content-Type")
        );
    }
    
    /*        // Health check handlers
    HealthCheckHandler healthHandler = HealthCheckHandler.createWithHealthChecks(HealthChecks.create(vertx));
    healthHandler.register("server-online", statusFuture -> statusFuture.complete(Status.OK()));
    // A single procedure should check one aspect of the system, you can group by category,groups can be nested
    HealthCheckHandler healthCheckWithProcedures = HealthCheckHandler.create(vertx);
    healthCheckWithProcedures.register("memory/memory-usage", statusFuture -> {
      // Check memory status if ok ...
     boolean memoryCheck = true;
      if (memoryCheck) {
        statusFuture.complete(Status.OK(new JsonObject().put("memory-usage", 90)));
      } else {
        // If fails send status KO
        statusFuture.complete(Status.KO());
      }
    });
    
    healthCheckWithProcedures.register("database/check", statusFuture ->
      statusFuture.complete(Status.OK(new JsonObject().put("online", "YES")))
    );
    healthCheckWithProcedures.register("memory/free-memory", 3000, statusFuture -> {
      int freeMemory = 255;// MB
      statusFuture.complete(Status.OK(new JsonObject().put("free", freeMemory)));

    });
    
    
    router.route().method(HttpMethod.GET).path("/test").handler(healthHandler);
    router.route().method(HttpMethod.GET).path("/test1").handler(healthCheckWithProcedures);
    
    */
}

package com.be88be.example.common.utils;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;

import com.be88be.example.common.model.enums.ResponseStatus;
import com.be88be.example.common.model.message.ResponseEntity;
import org.springframework.util.StringUtils;

@Slf4j
public class VertxUtil {
	
    public static void eventBusSend(Vertx vertx, RoutingContext ctx) {
        try {
            vertx.eventBus().<String>send(ctx.request().path(), ctx.getBody()
                    , ar -> {
                        if (ar.succeeded()) {
                            response(ctx, ar.result().body());
                        } else {
                            ReplyException ex = (ReplyException) ar.cause();
                            errorResponse(ctx, ex.failureCode(), ex.getMessage());
                        }
                    });
        } catch (Exception ex) {
            log.error("EventBusSend ERROR ", ex);
        }
    }
    
    public static void response(RoutingContext ctx, String message) {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        ctx.response().setStatusCode(ctx.response().getStatusCode());
        ctx.response().end(message);
    }
    
    public static void errorResponse(RoutingContext ctx, int statusCode, String message) {
        ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        ctx.response().setStatusCode(statusCode);
        ctx.response().end(Json.encode(ResponseEntity.error(statusCode, message)));
    }
    
    public static void relpy(Message<JsonObject> msg, Object obj) {
    	msg.reply(Json.encode(ResponseEntity.of(obj)));
    }

//    public static void relpy(Message<JsonObject> msg, String obj) {
//        msg.reply(obj);
//    }

    public static void fail(Message<JsonObject> msg) {
    	msg.fail(ResponseStatus.HTTP_SERVER_ERROR.getCode(), ResponseStatus.HTTP_SERVER_ERROR.getMessage());
    }
    
    public static void fail(Message<JsonObject> msg, String message) {
    	msg.fail(ResponseStatus.HTTP_SERVER_ERROR.getCode(), message);
    }
}



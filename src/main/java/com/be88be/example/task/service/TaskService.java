package com.be88be.example.task.service;

import com.be88be.example.common.model.enums.Api;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface TaskService {
	Handler<Message<JsonObject>> TaskServiceHandler(Api api);

}

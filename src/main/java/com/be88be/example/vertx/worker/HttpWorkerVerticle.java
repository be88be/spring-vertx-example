package com.be88be.example.vertx.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import com.be88be.example.common.annotation.WorkerVerticle;
import com.be88be.example.common.model.enums.Api;
import com.be88be.example.task.service.TaskService;

@WorkerVerticle
public class HttpWorkerVerticle extends AbstractVerticle {

    private TaskService taskService;

    public HttpWorkerVerticle(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void start() {
        for (Api api : Api.getApiValues()) {
            switch (api) {
                case GET_TASK:
                case GET_TEST:
                    vertx.eventBus().<JsonObject>consumer(api.getValue())
                            .handler(taskService.TaskServiceHandler(api));
                    break;
                default:
                    break;
            }
        }
    }
}
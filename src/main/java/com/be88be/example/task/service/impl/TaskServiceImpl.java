package com.be88be.example.task.service.impl;


import org.springframework.stereotype.Service;

import com.be88be.example.common.model.enums.Api;
import com.be88be.example.common.utils.VertxUtil;
import com.be88be.example.task.service.TaskService;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    @Override
    public Handler<Message<JsonObject>> TaskServiceHandler(Api api) {
        return msg -> {
            try {
//                RequestBody requestBody = msg.body().mapTo(RequestBody.class);

//                if (requestBody == null) {
//                    VertxUtil.fail(msg, ResponseStatus.METHOD_TYPE_NULL.getMessage());
//                }

                switch (api) {
                    case GET_TASK:
                        VertxUtil.relpy(msg, "TASK!");
                        break;
                    case GET_TEST:
                        VertxUtil.relpy(msg, "TEST!");
                        break;
                }
            } catch (Exception e) {
                VertxUtil.fail(msg);
                log.error("ERROR", e);
            }
        };
    }
}

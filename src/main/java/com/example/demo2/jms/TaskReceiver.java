package com.example.demo2.jms;

import com.example.demo2.model.Task;
import com.example.demo2.service.RecordPublishService;
import com.example.demo2.service.StudentPublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TaskReceiver {
    private static Logger logger = LoggerFactory.getLogger(TaskReceiver.class);

    @Autowired
    private RecordPublishService recordPublishService;

    @Autowired
    private StudentPublishService studentPublishService;

    @JmsListener(destination = "${internal.batch.queue}")
    public void receiveTask(Task task) {
        switch (task.getType()) {
            case RECORD:
                logger.info("process task {}", task);
                recordPublishService.publishOnDemand(task.getIds());
                break;
            case STUDENT:
                logger.info("process task {}", task);
                studentPublishService.publishOnDemand(task.getIds());
                break;
                default:
                    logger.info("task not supported: {}", task);
        }
    }
}

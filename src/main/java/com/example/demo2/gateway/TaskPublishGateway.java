package com.example.demo2.gateway;

import com.example.demo2.exception.PublishException;
import com.example.demo2.model.PublishStatus;
import com.example.demo2.model.Task;
import com.example.demo2.model.TaskPublishResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskPublishGateway {
    private static Logger logger = LoggerFactory.getLogger(TaskPublishGateway.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${internal.batch.queue}")
    private String batchQueue;

    public TaskPublishResult publish(Task task) throws PublishException {
        TaskPublishResult result = TaskPublishResult.builder()
                .task(task)
                .build();
        try {
            jmsTemplate.convertAndSend(batchQueue, task);

            logger.info("publish task to {} success: {}", batchQueue, task.getIds());
            result.setStatus(PublishStatus.SUCCESS);
        } catch (Exception e) {
            logger.info("publish task to {} failed: {}", batchQueue, task.getIds(), e);
            throw new PublishException("Publish task failed.", e);
        }

        return result;
    }
}

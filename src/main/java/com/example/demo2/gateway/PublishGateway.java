package com.example.demo2.gateway;

import com.example.demo2.model.PublishResult;
import com.example.demo2.model.PublishStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class PublishGateway {
    private static Logger logger = LoggerFactory.getLogger(PublishGateway.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${outbound.queue}")
    private String outboundQueue;

    public PublishResult publish(int id, Object data) {
        PublishResult result = PublishResult.builder()
                .id(id)
                .build();

        try {
            jmsTemplate.convertAndSend(outboundQueue, data);

            logger.info("publish to {} success: {}", outboundQueue, id);
            result.setStatus(PublishStatus.SUCCESS);
        } catch (Exception e) {
            logger.info("publish to {} failed: {}", outboundQueue, id, e);
            result.setStatus(PublishStatus.FAILED);
            result.setMessage(e.getMessage());
        }

        return result;
    }
}

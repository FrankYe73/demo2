package com.example.demo2.rest;

import com.example.demo2.model.PublishRequest;
import com.example.demo2.model.PublishSummary;
import com.example.demo2.service.RecordPublishService;
import com.example.demo2.service.StudentPublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {
    private static Logger logger = LoggerFactory.getLogger(PublishController.class);

    @Autowired
    private RecordPublishService recordPublishService;

    @Autowired
    private StudentPublishService studentPublishService;

    @PostMapping("/records/publish/onDemand")
    public PublishSummary publishRecordOnDemand(@RequestBody PublishRequest request) {
        logger.info(">>>>>>>> publishRecordOnDemand: {}", request);
        return recordPublishService.publishOnDemand(request.getIds());
    }

    @PostMapping("/records/publish")
    public PublishSummary publishAllRecords() {
        logger.info(">>>>>>>> publishAllRecords");
        return recordPublishService.publishAll();
    }

    @PostMapping("/students/publish/onDemand")
    public PublishSummary publishStudentOnDemand(@RequestBody PublishRequest request) {
        logger.info(">>>>>>>> publishStudentOnDemand: {}", request);
        return studentPublishService.publishOnDemand(request.getIds());
    }

    @PostMapping("/students/publish")
    public PublishSummary publishAllStudents() {
        logger.info(">>>>>>>> publishAllStudents");
        return studentPublishService.publishAll();
    }
}

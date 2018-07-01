package com.example.demo2.rest;

import com.example.demo2.model.PublishRequest;
import com.example.demo2.model.PublishStatusSummary;
import com.example.demo2.model.PublishSummary;
import com.example.demo2.service.RecordPublishService;
import com.example.demo2.service.StudentPublishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description="Distribute Batch Demo")
public class PublishController {
    private static Logger logger = LoggerFactory.getLogger(PublishController.class);

    @Autowired
    private RecordPublishService recordPublishService;

    @Autowired
    private StudentPublishService studentPublishService;

    @PostMapping("/records/publish/onDemand")
    @ApiOperation(value = "publish Record On Demand", response = PublishSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Publish successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    }
    )
    public PublishSummary publishRecordOnDemand(@RequestBody PublishRequest request) {
        logger.info(">>>>>>>> publishRecordOnDemand: {}", request);
        return recordPublishService.publishOnDemand(request.getIds());
    }

    @PostMapping("/records/publish")
    @ApiOperation(value = "publish All Records", response = PublishSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Publish successfully")
    }
    )
    public PublishSummary publishAllRecords() {
        logger.info(">>>>>>>> publishAllRecords");
        return recordPublishService.publishAll();
    }

    @GetMapping("/records/publish/status")
    @ApiOperation(value = "publish All Records Status", response = PublishStatusSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get status success")
    }
    )
    public PublishStatusSummary publishRecordsStatus() {
        logger.info(">>>>>>>> publishRecordsStatus");
        return recordPublishService.getPublishStatus();
    }

    @PostMapping("/students/publish/onDemand")
    @ApiOperation(value = "publish Student On Demand", response = PublishSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Publish successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    }
    )
    public PublishSummary publishStudentOnDemand(@RequestBody PublishRequest request) {
        logger.info(">>>>>>>> publishStudentOnDemand: {}", request);
        return studentPublishService.publishOnDemand(request.getIds());
    }

    @PostMapping("/students/publish")
    @ApiOperation(value = "publish All Students", response = PublishSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Publish successfully")
    }
    )
    public PublishSummary publishAllStudents() {
        logger.info(">>>>>>>> publishAllStudents");
        return studentPublishService.publishAll();
    }

    @GetMapping("/students/publish/status")
    @ApiOperation(value = "publish All Records Status", response = PublishStatusSummary.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get status success")
    }
    )
    public PublishStatusSummary publishStudentsStatus() {
        logger.info(">>>>>>>> publishStudentsStatus");
        return studentPublishService.getPublishStatus();
    }

}

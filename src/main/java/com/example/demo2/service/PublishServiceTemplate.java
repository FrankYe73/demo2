package com.example.demo2.service;

import com.example.demo2.exception.PublishException;
import com.example.demo2.gateway.PublishGateway;
import com.example.demo2.gateway.TaskPublishGateway;
import com.example.demo2.model.*;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PublishServiceTemplate<T, S> {
    private static Logger logger = LoggerFactory.getLogger(PublishServiceTemplate.class);

    @Autowired
    private PublishGateway publishGateway;

    @Autowired
    private TaskPublishGateway taskPublishGateway;

    @Value("${internal.batch.size}")
    private int batchSize;

    /****************************************************************
     * Publish On Demand
     * @return PublishSummary
     ***************************************************************/
    public PublishSummary publishOnDemand(List<Integer> ids) {
        List<T> records = retrieveRecordByIds(ids);
        if(records.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.SUCCESS)
                    .message("No records to publish.")
                    .build();
        }

        List<PublishResult> publishResultList = records.stream()
                .map(this::convertToPublishModel)
                .map(this::publishRecord)
                .collect(Collectors.toList());

        Map<Boolean, List<PublishResult>> resultMap = publishResultList.stream()
                .collect(Collectors.partitioningBy(result -> result.getStatus() == PublishStatus.SUCCESS));

        List<PublishResult> successList = resultMap.get(Boolean.TRUE);
        List<PublishResult> failedList = resultMap.get(Boolean.FALSE);

        if(!successList.isEmpty()) {
            reportSuccess(getIdsFromPublishResult(successList));
        }
        if(!failedList.isEmpty()) {
            reportFailed(getIdsFromPublishResult(failedList));
        }

        if(failedList.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.SUCCESS)
                    .message(successList.size() +  " records have been published successfully.")
                    .build();
        }

        if(successList.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.FAILED)
                    .message("Records publish failed.")
                    .failedList(getIdsFromPublishResult(failedList))
                    .build();
        }

        return PublishSummary.builder()
                .status(PublishStatus.PARTIAL_SUCCESS)
                .message(successList.size() +  " records have been published successfully.")
                .failedList(getIdsFromPublishResult(failedList))
                .build();
    }

    private PublishResult publishRecord(S data) {
        return publishGateway.publish(getId(data), data);
    }

    private List<Integer> getIdsFromPublishResult(List<PublishResult> publishResultList) {
        return publishResultList.stream().map(PublishResult::getId).collect(Collectors.toList());
    }

    protected abstract List<T> retrieveRecordByIds(List<Integer> ids);
    protected abstract S convertToPublishModel(T record);
    protected abstract int getId(S data);
    protected abstract void reportSuccess(List<Integer> successIdList);
    protected abstract void reportFailed(List<Integer> failedIdList);


    /****************************************************************
     * Publish All
     * @return PublishSummary
     ***************************************************************/
    public PublishSummary publishAll() {
        List<Integer> allIds = retrieveAllIds();
        if(allIds.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.SUCCESS)
                    .message("No record to publish or previous publish not finished yet.")
                    .build();
        }

        List<TaskPublishResult> publishResultList = Lists.partition(allIds, batchSize).stream()
                .map(ids -> Task.builder().type(getTaskType()).ids(ids).build())
                .map(this::publishTask)
                .collect(Collectors.toList());

        Map<Boolean, List<TaskPublishResult>> resultMap = publishResultList.stream()
                .collect(Collectors.partitioningBy(result -> result.getStatus() == PublishStatus.SUCCESS));

        List<TaskPublishResult> successList = resultMap.get(Boolean.TRUE);
        List<TaskPublishResult> failedList = resultMap.get(Boolean.FALSE);

        if(failedList.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.SUCCESS)
                    .message(successList.size() +  " tasks have been distributed successfully.")
                    .build();
        }

        if(successList.isEmpty()) {
            return PublishSummary.builder()
                    .status(PublishStatus.FAILED)
                    .message("Tasks distribution failed.")
                    .failedList(getIdsFromTaskPublishResult(failedList))
                    .build();
        }

        return PublishSummary.builder()
                .status(PublishStatus.PARTIAL_SUCCESS)
                .message(successList.size() +  " tasks have been distributed successfully.")
                .failedList(getIdsFromTaskPublishResult(failedList))
                .build();
    }

    private TaskPublishResult publishTask(Task task) {
        try {
            return makeAndPublish(task);
        } catch (Exception e) {
            logger.error("publish task error.", e);

            return TaskPublishResult.builder()
                    .task(task)
                    .status(PublishStatus.FAILED)
                    .build();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public TaskPublishResult makeAndPublish(Task task) throws PublishException {
        markUnderProcess(task.getIds());
        return taskPublishGateway.publish(task);
    }


    private List<Integer> getIdsFromTaskPublishResult(List<TaskPublishResult> publishResultList) {
        return publishResultList.stream()
                .flatMap(result -> result.getTask().getIds().stream()).collect(Collectors.toList());
    }


    protected abstract TaskType getTaskType();
    protected abstract List<Integer> retrieveAllIds();
    protected abstract void markUnderProcess(List<Integer> successIdList);
    public abstract PublishStatusSummary getPublishStatus();
}

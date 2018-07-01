package com.example.demo2.service;

import com.example.demo2.dao.RecordDao;
import com.example.demo2.model.Record;
import com.example.demo2.model.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordPublishService extends PublishServiceTemplate<Record, Record> {
    private static Logger logger = LoggerFactory.getLogger(RecordPublishService.class);

    @Autowired
    private RecordDao dao;

    // publish all methods -----------------------------------------------------------------
    @Override
    protected TaskType getTaskType() {
        return TaskType.RECORD;
    }

    @Override
    protected List<Integer> retrieveAllIds() {
        return dao.retrieveAllIds();
    }

    @Override
    protected void markUnderProcess(List<Integer> underProcessList) {
        dao.markUnderProcess(underProcessList);
    }


    // publish on demand methods -----------------------------------------------------------------
    @Override
    protected Record convertToPublishModel(Record record) {
        // TODO: convert
        return record;
    }

    @Override
    protected void reportFailed(List<Integer> failedList) {
        dao.markFailed(failedList);
    }

    @Override
    protected void reportSuccess(List<Integer> successList) {
        dao.markSuccess(successList);
    }

    @Override
    protected int getId(Record data) {
        return data.getId();
    }

    @Override
    protected List<Record> retrieveRecordByIds(List<Integer> ids) {
        return dao.getRecordsByIds(ids);
    }
}

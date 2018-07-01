package com.example.demo2.service;

import com.example.demo2.dao.StudentDao;
import com.example.demo2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentPublishService extends PublishServiceTemplate<Student, StudentPublishModel> {
    private static Logger logger = LoggerFactory.getLogger(StudentPublishService.class);

    @Autowired
    private StudentDao dao;

    // publish all methods -----------------------------------------------------------------
    @Override
    protected TaskType getTaskType() {
        return TaskType.STUDENT;
    }

    @Override
    protected List<Integer> retrieveAllIds() {
        return dao.retrieveAllIds();
    }

    @Override
    protected void markUnderProcess(List<Integer> underProcessList) {
        dao.markUnderProcess(underProcessList);
    }

    @Override
    public PublishStatusSummary getPublishStatus() {
        //TODO: call dao to get information
        return PublishStatusSummary.builder()
                .status(PublishStatus.UNDER_PROCESS)
                .successCount(123)
                .failedCount(4)
                .underProcessCount(16)
                .build();
    }

    // publish on demand methods -----------------------------------------------------------------
    @Override
    protected StudentPublishModel convertToPublishModel(Student student) {
        return StudentPublishModel.builder()
                .id(student.getId())
                .name(student.getName())
                .build();
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
    protected int getId(StudentPublishModel student) {
        return student.getId();
    }

    @Override
    protected List<Student> retrieveRecordByIds(List<Integer> ids) {
        return dao.getStudentsByIds(ids);
    }
}

package com.example.demo2.dao;

import com.example.demo2.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StudentDao {
    private static Logger logger = LoggerFactory.getLogger(StudentDao.class);

    public List<Student> getStudentsByIds(List<Integer> ids) {
        //TODO: retrieve Students

        List<Student> students = new ArrayList<>();
        ids.forEach(id -> {
            students.add(Student.builder()
                    .id(id)
                    .name("name " + id)
                    .age(6 + new Random().nextInt(12))
                    .build());
        });

        return students;
    }

    public void markSuccess(List<Integer> successList) {
        //TODO: mark success
        logger.info("mark as success. {}", successList);
    }

    public void markFailed(List<Integer> failedList) {
        //TODO: mark failed
        logger.info("mark as failed. {}", failedList);
    }

    public List<Integer> retrieveAllIds() {
        return IntStream.range(1, 12)
                .boxed().collect(Collectors.toList());
    }

    public void markUnderProcess(List<Integer> underProcessList) {
        //TODO: mark under process
        logger.info("mark as under process. {}", underProcessList);
    }
}

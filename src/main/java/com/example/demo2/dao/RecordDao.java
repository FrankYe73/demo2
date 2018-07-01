package com.example.demo2.dao;

import com.example.demo2.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class RecordDao {
    private static Logger logger = LoggerFactory.getLogger(RecordDao.class);

    public List<Record> getRecordsByIds(List<Integer> ids) {
        //TODO: retrieve records
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//
//        }

        List<Record> records = new ArrayList<>();
        ids.forEach(id -> {
            records.add(Record.builder()
                    .id(id)
                    .content("This is data for " + id)
                    .build());
        });

        return records;
    }

    public void markSuccess(List<Integer> successList) {
        //TODO: mark success
        logger.info("mark as success: {}", successList);
    }

    public void markFailed(List<Integer> failedList) {
        //TODO: mark failed
        logger.info("mark as failed. {}", failedList);
    }

    public List<Integer> retrieveAllIds() {
        return IntStream.range(1, 38)
                .boxed().collect(Collectors.toList());
    }

    public void markUnderProcess(List<Integer> underProcessList) {
        //TODO: mark under process
        logger.info("mark as under process. {}", underProcessList);
    }
}

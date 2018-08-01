package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class L extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            String file = jobExecution.getJobParameters().getString("file");
            Paths.get(file).toFile().deleteOnExit();
//            jdbcTemplate.query("SELECT first_name, last_name FROM people",
//                    (rs, row) -> new Person(
//                            rs.getString(1),
//                            rs.getString(2))
//            ).forEach(person -> log.info("Found <" + person + "> in the database."));
        }
    }

}
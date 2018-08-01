package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    @Override
    public Person process(final Person e) throws Exception {
        log.info("中间处理操作...");
        return e;
    }
}

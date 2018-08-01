package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@Controller
public class C {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @GetMapping("/test")
    public String test() {
        return "index";
    }

    @PostMapping("/upload")
    public String upload(MultipartFile file) throws Exception {
        log.info(file.getOriginalFilename());
        String[] split = file.getOriginalFilename().split("\\.");
        File tempFile = File.createTempFile(split[0], split[1]);
        file.transferTo(tempFile);
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("file", tempFile.getAbsolutePath())
                .toJobParameters());
        return "index";
    }

}

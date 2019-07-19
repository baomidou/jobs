package com.baomidou.jobs.sample;

import com.baomidou.jobs.starter.EnableJobs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot jobs sample
 *
 * @author jobob
 * @since 2019-06-25
 */
@EnableJobs
@SpringBootApplication
public class JobsSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobsSampleApplication.class, args);
    }
}
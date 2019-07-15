package com.baomidou.jobs.admin;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Job Admin
 *
 * @author jobob
 * @since 2019-05-31
 */
@EnableTransactionManagement
@SpringBootApplication
public class JobsAdminApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(JobsAdminApplication.class);
		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}
}
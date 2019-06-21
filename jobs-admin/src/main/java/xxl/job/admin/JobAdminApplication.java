package xxl.job.admin;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import xxl.job.web.starter.EnableXxlJobMybatisPlus;

/**
 * Job Admin
 *
 * @author 青苗
 * @since 2019-05-31
 */
@EnableXxlJobMybatisPlus
@EnableTransactionManagement
@SpringBootApplication
public class JobAdminApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(JobAdminApplication.class);
		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}
}
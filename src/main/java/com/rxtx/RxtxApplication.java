package com.rxtx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zx
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableConfigurationProperties
@EnableScheduling
@EntityScan("com/rxtx/entity")
@EnableAsync
@ServletComponentScan("com.rxtx.config.filter")
public class RxtxApplication {

	public static void main(String[] args) {
		SpringApplication.run(RxtxApplication.class, args);
	}

}

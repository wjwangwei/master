package com.nehow;

import com.nehow.services.WebserviceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@ImportResource("classpath:/spring/spring-config.xml")
@EnableConfigurationProperties({WebserviceProperties.class})
@EnableCaching
public class AgentApplication {
	public static void main(String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}
}

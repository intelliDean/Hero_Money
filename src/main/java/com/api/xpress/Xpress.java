package com.api.xpress;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@Slf4j
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class Xpress {

    public static void main(String[] args) {
        Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();

        SpringApplication application = new SpringApplication(Xpress.class);
        application.setBannerMode(Banner.Mode.LOG);

        ConfigurableApplicationContext context = application.run(args);
        Environment environment = context.getEnvironment();

        String port = environment.getProperty("local.server.port");
        String appName = environment.getProperty("spring.application.name", "Xpress");
        String profile = String.join(", ", environment.getActiveProfiles());

        log.info("""
                        
                        -----------------------------------------------
                        🚀 {} is Running!
                        🌐 URL:     http://localhost:{}
                        📄 Swagger: http://localhost:{}/swagger-ui/index.html
                        🔧 Profile: {}
                        -----------------------------------------------
                        """,
                appName, port, port, profile.isEmpty() ? "default" : profile
        );
    }
}


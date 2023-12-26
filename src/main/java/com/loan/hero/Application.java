package com.loan.hero;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        final SpringApplication application =
                new SpringApplication(Application.class);

        application.setBannerMode(Banner.Mode.LOG);
        application.run(args);
        log.info("::: Hero Server Running :::");
    }
}
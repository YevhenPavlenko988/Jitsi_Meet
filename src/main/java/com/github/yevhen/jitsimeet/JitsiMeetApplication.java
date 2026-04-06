package com.github.yevhen.jitsimeet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JitsiMeetApplication {
    public static void main(String[] args) {
        SpringApplication.run(JitsiMeetApplication.class, args);
    }
}

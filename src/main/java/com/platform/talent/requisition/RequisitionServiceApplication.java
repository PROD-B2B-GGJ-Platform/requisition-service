package com.platform.talent.requisition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class RequisitionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RequisitionServiceApplication.class, args);
    }
}


package com.mbartecki.storesimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StoreSimulatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(StoreSimulatorApplication.class, args);
  }
}

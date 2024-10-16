package com.github.gbaso.springdatamongodb4812;

import org.springframework.boot.SpringApplication;

public class TestSpringDataMongodb4812Application {

  public static void main(String[] args) {
    SpringApplication
        .from(SpringDataMongodb4812Application::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}

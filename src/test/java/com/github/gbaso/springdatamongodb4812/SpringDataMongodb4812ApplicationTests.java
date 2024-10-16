package com.github.gbaso.springdatamongodb4812;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringDataMongodb4812ApplicationTests {

  @Test
  @SuppressWarnings("java:S1186")
  void contextLoads() {
  }
}

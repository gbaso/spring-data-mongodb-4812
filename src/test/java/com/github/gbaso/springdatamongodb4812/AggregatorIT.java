package com.github.gbaso.springdatamongodb4812;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;

@Import(TestcontainersConfiguration.class)
@DataMongoTest
@ContextConfiguration(classes = Aggregator.class)
@EnableAutoConfiguration
class AggregatorIT {

  @Autowired
  MongoTemplate mongoTemplate;

  @Autowired
  Aggregator aggregator;

  @BeforeEach
  void setUp() {
    mongoTemplate.insert(new MyEntity(null, "test"));
  }

  @AfterEach
  void tearDown() {
    mongoTemplate.dropCollection(MyEntity.class);
    mongoTemplate.dropCollection("myEntityWithUUID");
  }

  @Test
  @DisplayName("copyUsingAggregationAPI should copy documents to target collection replacing ObjectId with UUID")
  void copyUsingAggregationAPI() {
    aggregator.copyUsingAggregationAPI();

    assertThat(mongoTemplate.findOne(new Query(), MyEntity.class, "myEntityWithUUID"))
        .extracting(MyEntity::id)
        .is(aValidUUID());
  }

  @Test
  @DisplayName("copyParsingJson should copy documents to target collection replacing ObjectId with UUID")
  void copyParsingJson() {
    aggregator.copyParsingJson();

    assertThat(mongoTemplate.findOne(new Query(), MyEntity.class, "myEntityWithUUID"))
        .extracting(MyEntity::id)
        .is(aValidUUID());
  }

  @Test
  @DisplayName("copyParsingJsonWithWorkaround should copy documents to target collection replacing ObjectId with UUID")
  void copyParsingJsonWithWorkaround() {
    aggregator.copyParsingJsonWithWorkaround();

    assertThat(mongoTemplate.findOne(new Query(), MyEntity.class, "myEntityWithUUID"))
        .extracting(MyEntity::id)
        .is(aValidUUID());
  }

  private static Condition<String> aValidUUID() {
    return new Condition<>(
        id -> {
          try {
            UUID.fromString(id);
            return true;
          } catch (IllegalArgumentException e) {
            return false;
          }
        },
        "a valid UUID");
  }
}

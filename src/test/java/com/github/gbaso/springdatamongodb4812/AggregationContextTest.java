package com.github.gbaso.springdatamongodb4812;

import static org.assertj.core.api.Assertions.assertThat;

import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.util.aggregation.TestAggregationContext;

class AggregationContextTest {

  @Test
  @DisplayName("getMappedObject should parse empty args as empty list")
  void getMappedObject() {
    // language=MongoDB-JSON
    Document project = Document.parse("""
        {
          $project: {
            _id: {
              $function: {
                body: "UUID().toString().slice(6, -2)",
                args: [],
                lang: "js"
              }
            },
            name: 1
          }
        }
        """);
    var context = TestAggregationContext.contextFor(MyEntity.class);
    Document mappedObject = context.getMappedObject(project);

    assertThat(mappedObject
        .get("$project", Document.class)
        .get("_id", Document.class)
        .get("$function", Document.class)
        .getList("args", String.class))
        .isNotNull()
        .isEmpty();
  }
}

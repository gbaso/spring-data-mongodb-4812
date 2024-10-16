package com.github.gbaso.springdatamongodb4812;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.out;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.ScriptOperators.Function.function;

import java.util.List;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
class Aggregator {

  private static final String OUT_COLLECTION_NAME = "myEntityWithUUID";

  private final MongoTemplate mongoTemplate;

  Aggregator(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  void copyUsingAggregationAPI() {
    var aggregation = newAggregation(
        project("name")
            .and(function("UUID().toString().slice(6, -2)")
                .args(List.of())
                .lang("js"))
            .as("_id"),
        out(OUT_COLLECTION_NAME));
    mongoTemplate.aggregate(aggregation, MyEntity.class, Document.class);
  }

  void copyParsingJson() {
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
    var aggregation = newAggregation(
        context -> context.getMappedObject(project),
        out(OUT_COLLECTION_NAME));
    mongoTemplate.aggregate(aggregation, MyEntity.class, Document.class);
  }

  void copyParsingJsonWithWorkaround() {
    // language=MongoDB-JSON
    Document project = Document.parse("""
        {
          $project: {
            _id: {
              $function: {
                body: "UUID().toString().slice(6, -2)",
                args: ["unused parameter"],
                lang: "js"
              }
            },
            name: 1
          }
        }
        """);
    var aggregation = newAggregation(
        context -> context.getMappedObject(project),
        out(OUT_COLLECTION_NAME));
    var res = mongoTemplate.aggregate(aggregation, MyEntity.class, Document.class);
    Document uniqueMappedResult = res.getUniqueMappedResult();
    System.out.println(uniqueMappedResult != null ? uniqueMappedResult.toJson() : "null");
  }
}
